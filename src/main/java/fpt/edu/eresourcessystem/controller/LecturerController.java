package fpt.edu.eresourcessystem.controller;

import fpt.edu.eresourcessystem.controller.advices.GlobalControllerAdvice;
import fpt.edu.eresourcessystem.dto.DocumentDto;
import fpt.edu.eresourcessystem.dto.FeedbackDto;
import fpt.edu.eresourcessystem.dto.Response.NotificationResponseDto;
import fpt.edu.eresourcessystem.dto.Response.QuestionResponseDto;
import fpt.edu.eresourcessystem.dto.Response.NotificationResponseDto;
import fpt.edu.eresourcessystem.enums.CourseEnum;
import fpt.edu.eresourcessystem.enums.DocumentEnum;
import fpt.edu.eresourcessystem.model.*;
import fpt.edu.eresourcessystem.service.*;
import fpt.edu.eresourcessystem.service.s3.StorageService;
import fpt.edu.eresourcessystem.utils.CommonUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import static fpt.edu.eresourcessystem.constants.Constants.PAGE_SIZE;
import static fpt.edu.eresourcessystem.utils.CommonUtils.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/lecturer")
public class LecturerController {
    private final GlobalControllerAdvice globalControllerAdvice;
    private final CourseService courseService;
    private final AccountService accountService;
    private final LecturerService lecturerService;
    private final StudentService studentService;
    private final TopicService topicService;
    private final ResourceTypeService resourceTypeService;
    private final DocumentService documentService;
    private final QuestionService questionService;
    private final FeedbackService feedbackService;
    private final StorageService storageService;
    private final CourseLogService courseLogService;
    private final MultiFileService multiFileService;
    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;

    private Lecturer getLoggedInLecturer() {
        String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (null == loggedInEmail || "anonymousUser".equals(loggedInEmail)) {
            return null;
        }
        Account loggedInAccount = accountService.findByEmail(loggedInEmail);
        if (loggedInAccount != null) {
            return lecturerService.findByAccountId(loggedInAccount.getId());
        } else return null;
    }

    private String getLoggedInLecturerMail() {
        String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (null == loggedInEmail || "anonymousUser".equals(loggedInEmail)) {
            return null;
        }
        return loggedInEmail;
    }

    private void addCourseLog(String courseId, String courseCode, String courseName,
                              CourseEnum.LecturerAction action,
                              CourseEnum.CourseObject object,
                              String objectId,
                              String objectName,
                              String email,
                              String oldContent,
                              String newContent) {
        CourseLog courseLog = new CourseLog(courseId, courseCode, courseName, action, object, objectId, objectName, email, oldContent, newContent);
        courseLogService.addCourseLog(courseLog);
    }

    @GetMapping
    public String getLecturerHome(@ModelAttribute Account account, final Model model) {
        Lecturer lecturer = getLoggedInLecturer();
        List<Course> recentCourses = courseService.findNewCoursesByLecturer(lecturer);
//        List<Course> recentCourses = courseService.findByListId(courseLogs);
        model.addAttribute("recentCourses", recentCourses);
        return "lecturer/lecturer";
    }

    /*
        LECTURER COURSES
     */

    /**
     * @param pageIndex page index
     * @param model     model
     * @return lecturer courses
     */
    @GetMapping({"/courses/list/{status}/{pageIndex}"})
    public String viewCourseManaged(@PathVariable(required = false) Integer pageIndex, final Model model, @PathVariable String status) {
        // get account authorized
        Lecturer lecturer = getLoggedInLecturer();
        if (null == lecturer || "".equalsIgnoreCase(status)) {
            return "common/login";
        }
        Page<Course> page = lecturerService.findListManagingCourse(lecturer, status, pageIndex, PAGE_SIZE);
        List<Integer> pages = CommonUtils.pagingFormat(page.getTotalPages(), pageIndex);
        model.addAttribute("pages", pages);
        model.addAttribute("totalPage", page.getTotalPages());
        model.addAttribute("courses", page.getContent());
        model.addAttribute("status", status);

        return "lecturer/course/lecturer_courses";
    }

    @PostMapping("/courses/{courseID}/changeStatus")
    @Transactional
    public String changeCourseStatus(@PathVariable String courseID, @RequestParam String status) {
        Course course = courseService.findByCourseId(courseID);
        String oldContent = course.getStatus().toString();
        switch (status.toUpperCase()) {
            case "PUBLISH" -> course.setStatus(CourseEnum.Status.PUBLISH);
            case "DRAFT" -> course.setStatus(CourseEnum.Status.DRAFT);
            case "HIDE" -> course.setStatus(CourseEnum.Status.HIDE);
        }
        courseService.updateCourse(course);
        //add course log
        addCourseLog(course.getId(),
                course.getCourseCode(),
                course.getCourseName(),
                CourseEnum.LecturerAction.CHANGE_STATUS,
                CourseEnum.CourseObject.COURSE,
                courseID,
                course.getCourseName(),
                getLoggedInLecturer().getAccount().getEmail(),
                oldContent, status);
        return "redirect:/lecturer/courses/" + courseID;

    }

    @GetMapping({"/courses/{courseId}/{getBy}", "/courses/{courseId}"})
    public String showCourseDetail(@PathVariable String courseId, final Model model, @PathVariable(required = false) String getBy) {
        Lecturer lecturer = getLoggedInLecturer();
        Course course = courseService.findByCourseId(courseId);
        if (null == course || null == lecturer) {
            return "exception/404";
        }

        // add course log
//        CourseLog courseLog = new CourseLog(course, CommonEnum.Action.VIEW);
//        courseLogService.addCourseLog(courseLog);
        model.addAttribute("course", course);
        model.addAttribute("courseStatus", CourseEnum.ChangeableStatus.values());
        if (getBy == null || getBy.equalsIgnoreCase("topics")) {
            List<Topic> topics = course.getTopics();
            model.addAttribute("topics", topics);
        } else {
            List<ResourceType> resourceTypes = course.getResourceTypes();
            model.addAttribute("resourceTypes", resourceTypes);
        }
        model.addAttribute("getBy", getBy);

        return "lecturer/course/lecturer_course-detail";
    }

    @GetMapping({"/courses/{courseId}/add_topic"})
    public String addTopicProcess(@PathVariable String courseId, final Model model) {
        Course course = courseService.findByCourseId(courseId);
        List<Topic> topics = course.getTopics();
        Topic modelTopic = new Topic();
        modelTopic.setCourse(course);
        model.addAttribute("course", course);
        model.addAttribute("topics", topics);
        model.addAttribute("topic", modelTopic);
        return "lecturer/topic/lecturer_add-topic-to-course";
    }

    @PostMapping({"topics/add_topic"})
    @Transactional
    public String addTopic(@ModelAttribute Topic topic, final Model model) {
        topic = topicService.addTopic(topic);
        courseService.addTopic(topic);
        Course course = courseService.findByCourseId(topic.getCourse().getId());
        List<Topic> topics = course.getTopics();
        Topic modelTopic = new Topic();
        modelTopic.setCourse(course);
        //add course log
        addCourseLog(course.getId(),
                course.getCourseCode(),
                course.getCourseName(),
                CourseEnum.LecturerAction.ADD,
                CourseEnum.CourseObject.TOPIC,
                topic.getId(),
                topic.getTopicTitle(),
                getLoggedInLecturer().getAccount().getEmail(),
                null, null);
        model.addAttribute("course", course);
        model.addAttribute("topics", topics);
        model.addAttribute("topic", modelTopic);
        model.addAttribute("success", "success");
        return "lecturer/topic/lecturer_add-topic-to-course";
    }

    @GetMapping({"/topics/{topicId}/update"})
    public String editTopicProcess(@PathVariable String topicId, final Model model) {
        Topic topic = topicService.findById(topicId);
        Course course = courseService.findByCourseId(topic.getCourse().getId());
        List<Topic> topics = course.getTopics();
        model.addAttribute("course", course);
        model.addAttribute("topics", topics);
        model.addAttribute("topic", topic);
        model.addAttribute("topicId", topicId);
        return "lecturer/topic/lecturer_update-topic-of-course";
    }


    @PostMapping({"/topics/{topicId}/update"})
    @Transactional
    public String editTopic(@PathVariable String topicId, @ModelAttribute Topic topic) {
        Topic checkTopicExist = topicService.findById(topicId);
        if (null != checkTopicExist) {
            checkTopicExist.setTopicTitle(topic.getTopicTitle());
            checkTopicExist.setTopicDescription(topic.getTopicDescription());
            topicService.updateTopic(checkTopicExist);
            //add course log
            Course course = checkTopicExist.getCourse();
            addCourseLog(course.getId(),
                    course.getCourseCode(),
                    course.getCourseName(),
                    CourseEnum.LecturerAction.UPDATE,
                    CourseEnum.CourseObject.TOPIC,
                    topic.getId(),
                    topic.getTopicTitle(),
                    getLoggedInLecturer().getAccount().getEmail(),
                    null, null);

        }
        return "redirect:/lecturer/topics/" + topicId + "/update?success";
    }

    @GetMapping({"/topics/{topicId}/delete_topic"})
    public String deleteTopic(@PathVariable String topicId, final Model model) {
        Topic topic = topicService.findById(topicId);
        if (null != topic) {
            courseService.removeTopic(topic.getCourse().getId(), new ObjectId(topicId));
            topicService.softDelete(topic);
            //add course log
            Course course = topic.getCourse();
            addCourseLog(course.getId(),
                    course.getCourseCode(),
                    course.getCourseName(),
                    CourseEnum.LecturerAction.UPDATE,
                    CourseEnum.CourseObject.DOCUMENT,
                    topic.getId(),
                    topic.getTopicTitle(),
                    getLoggedInLecturer().getAccount().getEmail(),
                    null, null);
        }
        return "redirect:/lecturer/courses/" + topic.getCourse().getId();
    }

    @GetMapping("/topics/{topicId}")
    public String viewTopicDetail(@PathVariable String topicId, final Model model) {
        Topic topic = topicService.findById(topicId);
        model.addAttribute("courseId", topic.getCourse().getId());
        model.addAttribute("courseName", topic.getCourse().getCourseName());
        model.addAttribute("courseCode", topic.getCourse().getCourseCode());
        model.addAttribute("documents", topic.getDocuments());
        model.addAttribute("topic", topic);
        return "lecturer/topic/lecturer_topic-detail";
    }

    @GetMapping({"/courses/{courseId}/add_resource_type"})
    public String addResourceTypeProcess(@PathVariable String courseId, final Model model) {
        Course course = courseService.findByCourseId(courseId);
        List<ResourceType> resourceTypes = course.getResourceTypes();
        ResourceType modelResourceType = new ResourceType();
        modelResourceType.setCourse(course);
        model.addAttribute("course", course);
        model.addAttribute("resourceTypes", resourceTypes);
        model.addAttribute("resourceType", modelResourceType);
        return "lecturer/resource_type/lecturer_add-resource-type-to-course";
    }

    @PostMapping({"resource_types/add_resource_type"})
    @Transactional
    public String addResourceType(ResourceType resourceType, final Model model) {
        ResourceType resourcetype = resourceTypeService.addResourceType(resourceType);
        courseService.addResourceType(resourcetype);
        Course course = courseService.findByCourseId(resourcetype.getCourse().getId());
        List<ResourceType> resourceTypes = course.getResourceTypes();
        ResourceType modelResourceType = new ResourceType();
        modelResourceType.setCourse(course);
        //add course log
        addCourseLog(course.getId(),
                course.getCourseCode(),
                course.getCourseName(),
                CourseEnum.LecturerAction.ADD,
                CourseEnum.CourseObject.RESOURCE_TYPE,
                resourceType.getId(),
                resourceType.getResourceTypeName(),
                getLoggedInLecturer().getAccount().getEmail(),
                null, null);

        model.addAttribute("course", course);
        model.addAttribute("resourceTypes", resourceTypes);
        model.addAttribute("resourceType", modelResourceType);
        model.addAttribute("success", "success");
        return "lecturer/resource_type/lecturer_add-resource-type-to-course";
    }

    @GetMapping({"/resource_types/{resourceTypeId}/update"})
    public String editResourceTypeProcess(@PathVariable String resourceTypeId, final Model model) {
        ResourceType resourcetype = resourceTypeService.findById(resourceTypeId);
        Course course = resourcetype.getCourse();
        List<ResourceType> resourceTypes = course.getResourceTypes();
        model.addAttribute("course", course);
        model.addAttribute("resourceTypes", resourceTypes);
        model.addAttribute("resourceType", resourcetype);
        return "lecturer/resource_type/lecturer_update-resource-type-of-course";
    }


    @PostMapping({"/resource_types/{resourceTypeId}/update"})
    @Transactional
    public String editResourceType(@PathVariable String resourceTypeId, @ModelAttribute ResourceType resourcetype) {
        ResourceType checkResourceTypeExist = resourceTypeService.findById(resourceTypeId);
        if (null != checkResourceTypeExist) {
            String oldContent = resourcetype.getResourceTypeName();
            checkResourceTypeExist.setResourceTypeName(resourcetype.getResourceTypeName());
            checkResourceTypeExist = resourceTypeService.updateResourceType(checkResourceTypeExist);
            //add course log
            Course course = checkResourceTypeExist.getCourse();
            addCourseLog(course.getId(),
                    course.getCourseCode(),
                    course.getCourseName(),
                    CourseEnum.LecturerAction.UPDATE,
                    CourseEnum.CourseObject.RESOURCE_TYPE,
                    checkResourceTypeExist.getId(),
                    checkResourceTypeExist.getResourceTypeName(),
                    getLoggedInLecturer().getAccount().getEmail(),
                    oldContent, null);
            return "redirect:/lecturer/resource_types/" + resourceTypeId + "/update?success";
        }
        return "redirect:/lecturer/resource_types/" + resourceTypeId + "/update?error";

    }

    @GetMapping({"resource_types/{resourceTypeId}/delete"})
    @Transactional
    public String deleteResourceType(@PathVariable String resourceTypeId) {
        ResourceType resourcetype = resourceTypeService.findById(resourceTypeId);
        if (null != resourcetype) {
            courseService.removeResourceType(resourcetype.getCourse().getId(), new ObjectId(resourceTypeId));
            resourceTypeService.softDelete(resourcetype);
            //add course log
            Course course = resourcetype.getCourse();
            addCourseLog(course.getId(),
                    course.getCourseCode(),
                    course.getCourseName(),
                    CourseEnum.LecturerAction.DELETE,
                    CourseEnum.CourseObject.RESOURCE_TYPE,
                    resourceTypeId,
                    resourcetype.getResourceTypeName(),
                    getLoggedInLecturer().getAccount().getEmail(),
                    null, null);
        }
        return "redirect:/lecturer/courses/" + resourcetype.getCourse().getId() + "/resource_types";
    }

    @GetMapping("/resource_types/{resourceTypeId}")
    public String viewResourceTypeDetail(@PathVariable String resourceTypeId, final Model model) {
        ResourceType resourcetype = resourceTypeService.findById(resourceTypeId);
        model.addAttribute("course", resourcetype.getCourse());
        model.addAttribute("resourceType", resourcetype);
        return "lecturer/resource_type/lecturer_resource-type-detail";
    }

    /*
        DOCUMENTS
     */

    /**
     * List all documents
     *
     * @return list documents
     */
    @GetMapping({"/documents/list"})
    public String showDocuments() {
        return "librarian/document/librarian_documents";
    }

    @GetMapping("/documents/list/{pageIndex}")
    String showDocumentsByPage(@PathVariable Integer pageIndex,
                               @RequestParam(required = false, defaultValue = "") String search,
                               @RequestParam(required = false, defaultValue = "all") String course,
                               @RequestParam(required = false, defaultValue = "all") String topic,
                               final Model model) {
        Page<Document> page = documentService.filterAndSearchDocument(course, topic, search, search, pageIndex, PAGE_SIZE);

        List<Integer> pages = CommonUtils.pagingFormat(page.getTotalPages(), pageIndex);
        model.addAttribute("pages", pages);
        model.addAttribute("totalPage", page.getTotalPages());
        model.addAttribute("currentPage", pageIndex);
        model.addAttribute("documents", page.getContent());
        model.addAttribute("search", search);
        model.addAttribute("selectedCourse", course);
        model.addAttribute("courses", courseService.findAll());
        return "lecturer/document/lecturer_documents";
    }

    @GetMapping({"/documents/{documentId}"})
    public String viewDocument(@PathVariable(required = false) String documentId,
                               @RequestParam(required = false) String questionId,
                               final Model model) throws IOException {
        Document document = documentService.findById(documentId);
        if (null == document) {
            model.addAttribute("errorMessage", "Could not found document.");
            return "exception/404";
        } else {
            // get list question
            List<QuestionResponseDto> questions = questionService.findByDocumentLimitAndSkip(document, 5, 0);
            if (null != questionId) {
                QuestionResponseDto question = new QuestionResponseDto(questionService.findById(questionId));
                if (question != null) {
                    boolean check = false;
                    for (int i = 0; i < questions.size(); i++) {
                        if (questions.get(i).getQuestionId().equals(questionId)) {
                            check = true;
                            break;
                        }
                    }
                    if (check) {
                        questions.add(new QuestionResponseDto(questionService.findById(questionId)));
                    }
                }
            }

            if (document.isDisplayWithFile() == true) {
                String data;
                if (document.getCloudFileLink() != null) {
                    data = document.getCloudFileLink();
                } else {
                    byte[] file = documentService.getGridFSFileContent(document.getContentId());
                    data = Base64.getEncoder().encodeToString(file);
                }
                model.addAttribute("data", data);
            }
            model.addAttribute("newAnswer", new Answer());
            model.addAttribute("document", document);
            model.addAttribute("topic", document.getTopic());
            model.addAttribute("questions", questions);
            return "lecturer/document/lecturer_document-detail";
        }
    }


    @GetMapping({"topics/{topicId}/documents/add"})
    public String addDocumentInTopic(@PathVariable String topicId, final Model model) {
        Topic topic = topicService.findById(topicId);
        model.addAttribute("document", new Document());
        model.addAttribute("topic", topic);

        List<ResourceType> resourceTypesByCourse = topic.getCourse().getResourceTypes();
        List<String> resourceTypes = resourceTypesByCourse.stream()
                .map(ResourceType::getResourceTypeName)
                .collect(Collectors.toList());

        model.addAttribute("resourceTypes", resourceTypes);
        return "lecturer/document/lecturer_add-document";
    }

    @GetMapping({"resource_types/{resourceTypeId}/documents/add"})
    public String addDocumentInResourceType(@PathVariable String resourceTypeId, final Model model) {
        ResourceType resourceType = resourceTypeService.findById(resourceTypeId);
        model.addAttribute("document", new Document());
        model.addAttribute("resourceType", resourceType);
        List<Topic> topics = resourceType.getCourse().getTopics();
        model.addAttribute("topics", topics);
        return "lecturer/document/lecturer_add-document-to-resource-type";
    }

    @PostMapping("/documents/add")
    @Transactional
    public String addDocumentProcess(@ModelAttribute DocumentDto documentDTO,
                                     @RequestParam(value = "topicId") String topicId,
                                     @RequestParam(value = "respondResourceType") String respondResourceType,
                                     @RequestParam(value = "file", required = false) MultipartFile file,
                                     @RequestParam(value = "files", required = false) MultipartFile[] files) throws Exception {
        // set topic vào document
        Topic topic = topicService.findById(topicId);
        Course course = topic.getCourse();
        documentDTO.setTopic(topic);
        ResourceType resourceType = new ResourceType(respondResourceType, course);

        // Thêm resource type
        List<ResourceType> resourceTypesInCourse = course.getResourceTypes();
        boolean checkResourceTypeExist = true;
        ResourceType existedResourceType = null;
        for (ResourceType resourceTypeObject : resourceTypesInCourse) {
            if (resourceTypeObject.getResourceTypeName().equalsIgnoreCase(respondResourceType)) {
                checkResourceTypeExist = false;
                existedResourceType = resourceTypeObject;
                break;
            }
        }
        if (checkResourceTypeExist) {
            ResourceType addedResourceType = resourceTypeService.addResourceType(resourceType);
            documentDTO.setResourceType(addedResourceType);
            courseService.addResourceTypeToCourse(course, new ObjectId(addedResourceType.getId()));
        } else {
            documentDTO.setResourceType(existedResourceType);
        }


        String id = "fileNotFound";
        if (String.valueOf(documentDTO.isDisplayWithFile()).equalsIgnoreCase("true")) {
            documentDTO.setDisplayWithFile(true);
            // Xử lý file
            // thêm check file trước khi add
            String message = "";
            if (file != null && !file.isEmpty() && file.getSize() < 104857600) {

                String filename = file.getOriginalFilename();
                String fileExtension = StringUtils.getFilenameExtension(filename);
                DocumentEnum.DocumentFormat docType = DocumentEnum.DocumentFormat.getDocType(fileExtension);

                if (docType != DocumentEnum.DocumentFormat.OTHER) {
                    documentDTO.setContent(extractTextFromFile(file.getInputStream()));
                } else {
                    documentDTO.setContent(null);
                }
                if (file.getSize() < 1048576 && docType != DocumentEnum.DocumentFormat.MS_DOC
                        && docType != DocumentEnum.DocumentFormat.OTHER && docType != DocumentEnum.DocumentFormat.AUDIO) {
                    id = documentService.addFile(file);
                } else {
                    id = "uploadToCloud";
                    try {
                        String link = storageService.uploadFile(file);

                        documentDTO.setCloudFileLink(link);
                        documentDTO.setFileName(filename);
                        documentDTO.setSuffix(fileExtension);
                    } catch (Exception e) {
                        message = "Can not upload file to server! Error: " + e.getMessage();
                        return "redirect:/lecturer/topics/" + topicId + "/documents/add?error";
                    }
                }
            } else {
                documentDTO.setDisplayWithFile(false);
            }
        } else {
            documentDTO.setContent(convertToPlainText(documentDTO.getEditorContent()));
        }

        List<MultiFile> multiFiles = new ArrayList<>();
        // Check if files were uploaded
        if (files != null && files.length > 0 && files.length < 4) {
            for (MultipartFile supportFile : files) {
                // Handle each uploaded file
                if (!supportFile.isEmpty()) {
                    try {

                        // Get the original file name
                        String originalFileName = supportFile.getOriginalFilename();
                        // Generate a unique file name
                        String uniqueFileName = System.currentTimeMillis() + "_" + FilenameUtils.getBaseName(originalFileName) + "." + FilenameUtils.getExtension(originalFileName);
                        // Process the uploaded file as needed
                        String link = storageService.uploadFileWithName(supportFile, uniqueFileName);
                        MultiFile multiFile = new MultiFile(originalFileName, uniqueFileName, link);
                        MultiFile addedFile = multiFileService.addMultiFile(multiFile);
                        multiFiles.add(addedFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                        // Handle any exceptions that occur during file upload
                    }
                }
            }
            documentDTO.setMultiFiles(multiFiles);
        }

        Document document = documentService.addDocument(documentDTO, id);

        // Xử lý sau khi add document
        resourceTypeService.addDocumentToResourceType(document.getResourceType().getId(), new ObjectId(document.getId()));

        // Thêm document vào topic
        topicService.addDocumentToTopic(topicId, new ObjectId(document.getId()));

        //add course log
        addCourseLog(course.getId(),
                course.getCourseCode(),
                course.getCourseName(),
                CourseEnum.LecturerAction.ADD,
                CourseEnum.CourseObject.DOCUMENT,
                document.getId(),
                document.getTitle(),
                getLoggedInLecturer().getAccount().getEmail(),
                null, null);

        // Notify student that save this course
        Notification notification;
        for(String student: course.getStudents()){
            notification = new Notification(
                    getLoggedInLecturerMail(),
                    student,
                    getLoggedInLecturerMail() + " updated new document in " + course.getCourseName(),
                    "/student/courses/" + course.getId()
                    );
            notificationService.addNotificationWhenUpdateDocument(notification);
        }


        return "redirect:/lecturer/topics/" + topicId + "/documents/add?success";
    }

    @GetMapping({"/documents/{documentId}/update"})
    public String updateDocument(@PathVariable(required = false) String documentId, final Model model) throws IOException {
        Document document = documentService.findById(documentId);
        if (null == document) {
            return "redirect:lecturer/documents/update?error";
        } else {
            model.addAttribute("document", document);
            if (document.getTopic() != null) {
                model.addAttribute("resourceTypes", document.getTopic().getCourse().getResourceTypes());
                model.addAttribute("topics", document.getTopic());
            }
            if (document.getFileName() != null) {
                model.addAttribute("file", document.getFileName());
            }
            return "lecturer/document/lecturer_update-document";
        }
    }

    @PostMapping("/documents/update")
    @Transactional
    public String updateDocumentProcess(@ModelAttribute DocumentDto document,
                                        @RequestParam(value = "deleteCurrentFile", required = false) String deleteCurrentFile,
                                        @RequestParam(value = "file", required = false) MultipartFile file,
                                        @RequestParam(value = "files", required = false) MultipartFile[] files)
            throws Exception {
        Document checkExist = documentService.findById(document.getId());
        if (null == checkExist) {
            return "redirect:/lecturer/documents/" + document.getId() + "/update?error";
        } else {
            checkExist.setTitle(document.getTitle());
            checkExist.setDescription(document.getDescription());
            String id = "fileNotFound";
            String message = "";
            if (checkExist.isDisplayWithFile() == false) {
                checkExist.setEditorContent(document.getEditorContent());
                checkExist.setContent(convertToPlainText(document.getEditorContent()));
                documentService.updateDocument(checkExist, null, id);
            } else {
                if (file != null && !file.isEmpty() && file.getSize() < 104857600) {
                    String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                    String fileExtension = StringUtils.getFilenameExtension(filename);
                    DocumentEnum.DocumentFormat docType = DocumentEnum.DocumentFormat.getDocType(fileExtension);
                    checkExist.setFileName(file.getOriginalFilename());
                    if (docType != DocumentEnum.DocumentFormat.OTHER) {
                        checkExist.setContent(extractTextFromFile(file.getInputStream()));
                    } else {
                        checkExist.setContent(null);
                    }
                    if (file.getSize() < 1048576 && docType != DocumentEnum.DocumentFormat.MS_DOC
                            && docType != DocumentEnum.DocumentFormat.OTHER && docType != DocumentEnum.DocumentFormat.AUDIO) {
                        checkExist.setCloudFileLink(null);
                        id = documentService.addFile(file);
                    } else {
                        id = "uploadToCloud";
                        try {
                            checkExist.setContentId(null);
                            String link = storageService.uploadFile(file);
                            checkExist.setCloudFileLink(link);
                            checkExist.setFileName(filename);
                            checkExist.setSuffix(fileExtension);
                            checkExist.setDocType(DocumentEnum.DocumentFormat.getDocType(fileExtension));
                        } catch (Exception e) {
                            message = "Can not upload file to server! Error: " + e.getMessage();
                            return "redirect:/lecturer/topics/" + document.getTopic().getId() + "/documents/update?error";
                        }
                    }
                }
                if (checkExist.getCloudFileLink() != null && checkExist.getContentId() == null) {
                    storageService.deleteFile(checkExist.getFileName());
                    documentService.updateDocument(checkExist, null, id);
                } else {
                    documentService.updateDocument(checkExist, String.valueOf(checkExist.getContentId()), id);
                }
            }
            //add course log
            Course course = checkExist.getTopic().getCourse();
            addCourseLog(course.getId(),
                    course.getCourseCode(),
                    course.getCourseName(),
                    CourseEnum.LecturerAction.UPDATE,
                    CourseEnum.CourseObject.DOCUMENT,
                    document.getId(),
                    document.getTitle(),
                    getLoggedInLecturer().getAccount().getEmail(),
                    null, null);

            return "redirect:/lecturer/documents/" + document.getId() + "/update?success";
        }
    }

    @PostMapping("/{documentId}/update_supporting_files")
    @Transactional
    public String updateSupportingFiles(@RequestParam(value = "files", required = false) MultipartFile[] files,
                              @RequestParam(value = "supportingFiles", required = false) String[] supportingFiles, @PathVariable String documentId) {
        Document document = documentService.findById(documentId);
        List<MultiFile> multiFiles = document.getMultipleFiles();
        if (supportingFiles == null){
            supportingFiles = new String[]{""};
        }
        int supportingFilesNumber = supportingFiles != null ? supportingFiles.length : 0;
        int filesNumber = files != null ? files.length : 0;

        int total = supportingFilesNumber + filesNumber;

        if(total < 4) {
            if (supportingFiles != null) {
                List<MultiFile> existedMultiFiles = document.getMultipleFiles();
                for (MultiFile existedMultiFile : existedMultiFiles) {
                    if (!Arrays.asList(supportingFiles).contains(existedMultiFile.getCloudFileName())) {
                        // xóa id trong document
                        MultiFile multiFile = multiFileService.findByCloudFileName(existedMultiFile.getCloudFileName());
                        documentService.removeMultiFile(documentId, new ObjectId(multiFile.getId()));

                        // xóa file có sẵn
                        storageService.deleteFile(existedMultiFile.getCloudFileName());
                        multiFileService.hardDeleteMultiFile(existedMultiFile.getCloudFileName());

                    }
                }
            }

            if (files != null && filesNumber > 0) {
                for (MultipartFile file : files) {
                    // Get the original file name
                    String originalFileName = file.getOriginalFilename();
                    // Generate a unique file name
                    String uniqueFileName = System.currentTimeMillis() + "_" + FilenameUtils.getBaseName(originalFileName) + "." + FilenameUtils.getExtension(originalFileName);
                    // Process the uploaded file as needed
                    String link = storageService.uploadFileWithName(file, uniqueFileName);
                    MultiFile multiFile = new MultiFile(originalFileName, uniqueFileName, link);
                    MultiFile addedFile = multiFileService.addMultiFile(multiFile);
                    // push file mới vào doc
                    multiFiles.add(addedFile);
                }
            }


            document.setMultipleFiles(multiFiles);
            documentService.updateDoc(document);
        }
        return "redirect:/lecturer/documents/" + documentId;
    }

    @GetMapping("/documents/{documentId}/delete")
    public String deleteDocument(@PathVariable String documentId) {
        Document document = documentService.findById(documentId);
        if (null != document) {
            topicService.removeDocumentFromTopic(document.getTopic().getId(), new ObjectId(documentId));
            resourceTypeService.removeDocumentFromResourceType(document.getTopic().getId(), new ObjectId(documentId));
            documentService.softDelete(document);

            // Add course log
            Course course = document.getTopic().getCourse();
            addCourseLog(course.getId(),
                    course.getCourseCode(),
                    course.getCourseName(),
                    CourseEnum.LecturerAction.DELETE,
                    CourseEnum.CourseObject.DOCUMENT,
                    document.getId(),
                    document.getTitle(),
                    getLoggedInLecturer().getAccount().getEmail(),
                    null, null);
            return "redirect:/lecturer/topics/" + document.getTopic().getId() + "?success";
        }
        return "redirect:/lecturer/documents/{documentId}?error";
    }

    /*
        My document
    */
    @GetMapping({"/my_documents/list/{status}/{pageIndex}"})
    public String viewUploadedDocuments(@PathVariable(required = false) Integer pageIndex, final Model model, @PathVariable String status) {
        // get account authorized
        Lecturer lecturer = getLoggedInLecturer();
        if (null == lecturer || "".equalsIgnoreCase(status)) {
            return "common/login";
        }
        Page<Document> page = lecturerService.findListDocuments(lecturer, status, pageIndex, PAGE_SIZE);
        List<Integer> pages = CommonUtils.pagingFormat(page.getTotalPages(), pageIndex);
        model.addAttribute("pages", pages);
        model.addAttribute("totalPage", page.getTotalPages());
        model.addAttribute("documents", page.getContent());
        model.addAttribute("status", status);
        return "lecturer/document/lecturer_my-documents";
    }

    /*
        Question ans
    */
    @GetMapping({"/questions/list/{status}/{pageIndex}"})
    public String viewListOfQuestions(@PathVariable(required = false) Integer pageIndex, final Model model, @PathVariable String status) {
        List<Question> questions = questionService.findByLecturerMail(getLoggedInLecturerMail());
        model.addAttribute("studentQuestions", questions);
        // add log
//        addUserLog("/my_library/my_questions/history");
        model.addAttribute("status", status);
        return "lecturer/document/lecturer_questions";
    }

    /*
        Feedback
    */
    @GetMapping("/feedbacks/add")
    public String showFeedbackForm(Model model) {
        model.addAttribute("feedback", new Feedback());
        return "lecturer/feedback/lecturer_feedback-add";
    }

    // Method to handle the form submission
    @PostMapping("/feedbacks/add")
    public String processFeedbackForm(@ModelAttribute("feedback") @Valid FeedbackDto feedback,
                                      BindingResult result) {

        // Get the logged-in user (you need to implement your user authentication mechanism)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return "exception/404";
        }
        String currentPrincipalName = authentication.getName();
        Account loggedInUser = accountService.findByEmail(currentPrincipalName);
        //  User loggedInUser = getCurrentLoggedInUser(); // Replace with your authentication logic

        if (loggedInUser != null) {
            feedback.setAccount(loggedInUser);
            feedback.setStatus("Pending");
            // Save the feedback to the database
            feedbackService.saveFeedback(new Feedback(feedback));
            return "redirect:/admin/feedbacks/list"; // Redirect to a success page
        } else {
            return "redirect:/login"; // Redirect to the login page if the user is not logged in
        }
    }

    /*
        Login as student
     */

    @GetMapping("/login_as_student")
    public String loginAsStudent() {
        Account loggedInAccount = globalControllerAdvice.getLoggedInAccount();
        if (loggedInAccount != null) {
            Student existStudent = studentService.findByAccountId(loggedInAccount.getId());
            if (existStudent == null) {
                Student student = new Student();
                student.setAccount(loggedInAccount);
                studentService.addStudent(student);
            }
        }
        return "redirect:/student";
    }

    @GetMapping({"/notifications"})
    public String getNotifications(final Model model) {
        String lecturerMail = getLoggedInLecturerMail();
        List<NotificationResponseDto> notificationResponseDtos = notificationService.findAllByToAccount(lecturerMail);
        model.addAttribute("notifications", notificationResponseDtos);
        return "lecturer/lecturer_notifications";
    }

}
