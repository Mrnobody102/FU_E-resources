package fpt.edu.eresourcessystem.controller;

import fpt.edu.eresourcessystem.controller.advices.GlobalControllerAdvice;
import fpt.edu.eresourcessystem.dto.DocumentDto;
import fpt.edu.eresourcessystem.dto.FeedbackDto;
import fpt.edu.eresourcessystem.enums.CourseEnum;
import fpt.edu.eresourcessystem.enums.DocumentEnum;
import fpt.edu.eresourcessystem.model.*;
import fpt.edu.eresourcessystem.service.*;
import fpt.edu.eresourcessystem.service.s3.StorageService;
import fpt.edu.eresourcessystem.utils.CommonUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static fpt.edu.eresourcessystem.constants.Constants.PAGE_SIZE;

@Controller
@AllArgsConstructor
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
    private final AnswerService answerService;
    private final FeedbackService feedbackService;
    private final StorageService storageService;

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
     * @param model model
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

    @GetMapping({"/courses/{courseId}/update"})
    public String updateCourseProcess(@PathVariable(required = false) String courseId, final Model model) {
        if (null == courseId) {
            courseId = "";
        }
        Course course = courseService.findByCourseId(courseId);
        if (null == course) {
            return "redirect:lecturer/courses/update?error";
        } else {
            List<Account> lecturers = accountService.findAllLecturer();
            model.addAttribute("lecturers", lecturers);
            model.addAttribute("course", course);
            return "lecturer/course/lecturer_update-course";
        }
    }

    @PostMapping("/courses/{courseID}/changeStatus")
    public String changeCourseStatus(@PathVariable String courseID, @RequestParam String status) {
        Course course = courseService.findByCourseId(courseID);
        switch (status.toUpperCase()) {
            case "PUBLISH" -> course.setStatus(CourseEnum.Status.PUBLISH);
            case "DRAFT" -> course.setStatus(CourseEnum.Status.DRAFT);
            case "HIDE" -> course.setStatus(CourseEnum.Status.HIDE);
        }
        courseService.updateCourse(course);
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
    public String addTopic(@ModelAttribute Topic topic, final Model model) {
        topic = topicService.addTopic(topic);
        courseService.addTopic(topic);
        Course course = courseService.findByCourseId(topic.getCourse().getId());
        List<Topic> topics = course.getTopics();
        Topic modelTopic = new Topic();
        modelTopic.setCourse(course);
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
    public String editTopic(@PathVariable String topicId, @ModelAttribute Topic topic) {
        Topic checkTopicExist = topicService.findById(topicId);
        if (null != checkTopicExist) {
            checkTopicExist.setTopicTitle(topic.getTopicTitle());
            checkTopicExist.setTopicDescription(topic.getTopicDescription());
            topicService.updateTopic(checkTopicExist);

        }
        return "redirect:/lecturer/topics/" + topicId + "/update?success";
    }

    @GetMapping({"/topics/{topicId}/delete_topic"})
    public String deleteTopic(@PathVariable String topicId, final Model model) {
        Topic topic = topicService.findById(topicId);
        if (null != topic) {
            courseService.removeTopic(topic.getCourse().getId(), new ObjectId(topicId));
            topicService.softDelete(topic);
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
    public String addResourceType(ResourceType resourceType, final Model model) {
        ResourceType resourcetype = resourceTypeService.addResourceType(resourceType);
        courseService.addResourceType(resourcetype);
        Course course = courseService.findByCourseId(resourcetype.getCourse().getId());
        List<ResourceType> resourceTypes = course.getResourceTypes();
        ResourceType modelResourceType = new ResourceType();
        modelResourceType.setCourse(course);
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
    public String editResourceType(@PathVariable String resourceTypeId, @ModelAttribute ResourceType resourcetype) {
        ResourceType checkResourceTypeExist = resourceTypeService.findById(resourceTypeId);
        if (null != checkResourceTypeExist) {
            checkResourceTypeExist.setResourceTypeName(resourcetype.getResourceTypeName());
            resourceTypeService.updateResourceType(checkResourceTypeExist);
            return "redirect:/lecturer/resource_types/" + resourceTypeId + "/update?success";
        }
        return "redirect:/lecturer/resource_types/" + resourceTypeId + "/update?error";

    }

    @GetMapping({"resource_types/{resourceTypeId}/delete"})
    public String deleteResourceType(@PathVariable String resourceTypeId) {
        ResourceType resourcetype = resourceTypeService.findById(resourceTypeId);
        if (null != resourcetype) {
            courseService.removeResourceType(resourcetype.getCourse().getId(), new ObjectId(resourceTypeId));
            resourceTypeService.softDelete(resourcetype);
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
    public String viewDocument(@PathVariable(required = false) String documentId, final Model model) throws IOException {
        Document document = documentService.findById(documentId);
        if (null == document) {
            model.addAttribute("errorMessage", "Could not found document.");
            return "exception/404";
        } else {
            // get list question
            List<Question> questions = questionService.findByDocId(document);

            if (document.isDisplayWithFile()) {
                String data;
                if(document.getCloudFileLink() != null) {
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
//        System.out.println(DocumentEnum.DefaultTopicResourceTypes.values());
        return "lecturer/document/lecturer_add-document";
    }

    @GetMapping({"resource_types/{resourceTypeId}/documents/add"})
    public String addDocumentInResourceType(@PathVariable String resourceTypeId, final Model model) {
        ResourceType resourceType = resourceTypeService.findById(resourceTypeId);
        model.addAttribute("document", new Document());
        model.addAttribute("resourceType", resourceType);
        List<Topic> topics = resourceType.getCourse().getTopics();
        model.addAttribute("topics", topics);
//        System.out.println(DocumentEnum.DefaultTopicResourceTypes.values());
        return "lecturer/document/lecturer_add-document-to-resource-type";
    }

    @PostMapping("/documents/add")
    @Transactional
    public String addDocumentProcess(@ModelAttribute DocumentDto documentDTO,
                                     @RequestParam(value = "topicId") String topicId,
                                     @RequestParam(value = "respondResourceType") String respondResourceType,
                                     @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        // set topic vào document
        Topic topic = topicService.findById(topicId);
        documentDTO.setTopic(topic);
        ResourceType resourceType = new ResourceType(respondResourceType, topic.getCourse());

        // Thêm resource type
        List<ResourceType> resourceTypesInCourse = topic.getCourse().getResourceTypes();
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
        } else {
            documentDTO.setResourceType(existedResourceType);
        }

        // Xử lý file
        // thêm check file trước khi add
        String id = "fileNotFound";
        String message = "";
        if (file != null && !file.isEmpty()) {
            documentDTO.setFileName(file.getOriginalFilename());
            if(file.getSize() < 1048576) {
                id = documentService.addFile(file);
            } else {
                id = "uploadToCloud";
                try {
                    String link = storageService.uploadFile(file);
                    documentDTO.setCloudFileLink(link);
                    String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                    String fileExtension = StringUtils.getFilenameExtension(filename);
                    documentDTO.setSuffix(fileExtension);
                } catch (Exception e) {
                    message = "Can not upload file to server! Error: " + e.getMessage();
                    return "redirect:/lecturer/topics/" + topicId + "/documents/add?error";
                }
            }
        }
        Document document = documentService.addDocument(documentDTO, id);

        // Xử lý sau khi add document
        resourceTypeService.addDocumentToResourceType(document.getResourceType().getId(), new ObjectId(document.getId()));

        // Thêm document vào topic
        topicService.addDocumentToTopic(topicId, new ObjectId(document.getId()));

        return "redirect:/lecturer/topics/" + topicId + "/documents/add?success";
    }

    @GetMapping({"/documents/{documentId}/update"})
    public String updateDocumentProcess(@PathVariable(required = false) String documentId, final Model model) throws IOException {
        if (null == documentId) {
            documentId = "";
        }
        Document document = documentService.findById(documentId);
        if (null == document) {
            return "redirect:lecturer/documents/update?error";
        } else {
            model.addAttribute("document", document);
            if(document.getTopic() != null) {
                model.addAttribute("resourceTypes", document.getTopic().getCourse().getResourceTypes());
                model.addAttribute("topics", document.getTopic());
            }
            if(document.getFileName() != null) {
                model.addAttribute("file", document.getFileName());
            }
            return "lecturer/document/lecturer_update-document";
        }
    }

    @PostMapping("/documents/update")
    public String updateDocument(@ModelAttribute DocumentDto document,
                                 @RequestParam(value = "deleteCurrentFile",  required = false) String deleteCurrentFile,
                                 @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        Document checkExist = documentService.findById(document.getId());
        if (null == checkExist) {
            return "redirect:/lecturer/documents/" + document.getId() + "/update?error";
        } else {
            checkExist.setTitle(document.getTitle());
            checkExist.setDescription(document.getDescription());
            checkExist.setEditorContent(document.getEditorContent());
            String id = "fileNotFound";
            String message = "";
            if (file != null && !file.isEmpty()) {
                checkExist.setFileName(file.getOriginalFilename());
                if(file.getSize() < 1048576) {
                    checkExist.setCloudFileLink(null);
                    id = documentService.addFile(file);
                } else {
                    id = "uploadToCloud";
                    try {
                        String link = storageService.uploadFile(file);
                        checkExist.setCloudFileLink(link);
                        String filename = System.currentTimeMillis() + "_" +file.getOriginalFilename();
                        String fileExtension = StringUtils.getFilenameExtension(filename);
                        checkExist.setFileName(filename);
                        checkExist.setSuffix(fileExtension);
                        checkExist.setDocType(DocumentEnum.DocumentFormat.getDocType(fileExtension));
                    } catch (Exception e) {
                        message = "Can not upload file to server! Error: " + e.getMessage();
                        return "redirect:/lecturer/topics/" + document.getTopic().getId() + "/documents/update?error";
                    }
                }
            }
            if(deleteCurrentFile != null && deleteCurrentFile.equals("on")) {
                checkExist.setDisplayWithFile(false);
                checkExist.setFileName(null);
                if(checkExist.getCloudFileLink() != null) {
                    storageService.deleteFile(checkExist.getFileName());
                    documentService.updateDocument(checkExist, null, id);
                } else {
                    documentService.updateDocument(checkExist, String.valueOf(checkExist.getContentId()), id);
                }
            } else {
                checkExist.setDisplayWithFile(true);
                documentService.updateDocument(checkExist, null, id);
            }
            return "redirect:/lecturer/documents/" + document.getId() + "/update?success";
        }
    }

    @GetMapping("/documents/{documentId}/delete")
    public String deleteDocument(@PathVariable String documentId) {
        Document document = documentService.findById(documentId);
        if (null != document) {
            topicService.removeDocumentFromTopic(document.getTopic().getId(), new ObjectId(documentId));
            resourceTypeService.removeDocumentFromResourceType(document.getTopic().getId(), new ObjectId(documentId));
            documentService.softDelete(document);
            return "redirect:/topics/" + document.getTopic().getId() + "?success";
        }
        return "redirect:/documents/{documentId}?error";
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
        // get account authorized
        Lecturer lecturer = getLoggedInLecturer();
        List<Question> questions = questionService.findByLecturer(lecturer);
        for (Question q : questions) {
            q.setAnswers(new HashSet<>(answerService.findByQuestion(q)));
        }
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
//        if (result.hasErrors()) {
//            return "student/feedback/student_feedback-add"; // Return to the form with validation errors
//        }

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

}
