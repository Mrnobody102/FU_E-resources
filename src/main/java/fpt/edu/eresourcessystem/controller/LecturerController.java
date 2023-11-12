package fpt.edu.eresourcessystem.controller;

import fpt.edu.eresourcessystem.dto.CourseDTO;
import fpt.edu.eresourcessystem.dto.DocumentDTO;
import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.enums.CourseEnum;
import fpt.edu.eresourcessystem.enums.DocumentEnum;
import fpt.edu.eresourcessystem.model.*;
import fpt.edu.eresourcessystem.service.*;
import fpt.edu.eresourcessystem.utils.CommonUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/lecturer")
@PropertySource("web-setting.properties")
public class LecturerController {
    @Value("${page-size}")
    private Integer pageSize;
    private final CourseService courseService;
    private final AccountService accountService;
    private final LecturerService lecturerService;
    private final TopicService topicService;
    private final ResourceTypeService resourceTypeService;

    private final DocumentService documentService;
    private final CourseLogService courseLogService;

    private final QuestionService questionService;

    public LecturerController(CourseService courseService, AccountService accountService, LecturerService lecturerService, TopicService topicService, ResourceTypeService resourceTypeService, DocumentService documentService, CourseLogService courseLogService, QuestionService questionService) {
        this.courseService = courseService;
        this.accountService = accountService;
        this.lecturerService = lecturerService;
        this.topicService = topicService;
        this.resourceTypeService = resourceTypeService;
        this.documentService = documentService;
        this.courseLogService = courseLogService;
        this.questionService = questionService;
    }


    public Lecturer getLoggedInLecturer() {
        return lecturerService.findAll().get(0);
    }

    @GetMapping
    public String getLibraryManageDashboard(@ModelAttribute Account account) {
        return "lecturer/lecturer";
    }

    /*
        LECTURER COURSES
     */

    /**
     * @param pageIndex
     * @param model
     * @return
     */
    @GetMapping({"/courses/list/{status}/{pageIndex}"})
    public String viewCourseManaged(@PathVariable(required = false) Integer pageIndex, final Model model, @PathVariable String status) {
        // get account authorized
        Lecturer lecturer = getLoggedInLecturer();
        if (null == lecturer || "".equalsIgnoreCase(status)) {
            return "common/login";
        }
        Page<Course> page = lecturerService.findListManagingCourse(lecturer, status, pageIndex, pageSize);
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

    @PostMapping("/update")
    public String updateCourse(@ModelAttribute CourseDTO courseDTO, final Model model) { //thêm tham số course state
        // if status.equals("PUBLISH")
        Course course = new Course(courseDTO, CourseEnum.Status.PUBLISH);
        // else ...
        Course checkExist = courseService.findByCourseId(course.getId());
        if (null == checkExist) {
            model.addAttribute("errorMessage", "Course not exist.");
            return "exception/404";
        } else {
            Course checkCodeDuplicate = courseService.findByCourseCode(course.getCourseCode());
            if (checkCodeDuplicate != null &&
                    !checkExist.getCourseCode().equalsIgnoreCase(course.getCourseCode())) {
                //
            }
            courseService.updateCourse(course);
            List<Account> lecturers = accountService.findAllLecturer();
            model.addAttribute("course", course);
            model.addAttribute("lecturers", lecturers);
            model.addAttribute("success", "");
            return "lecturer/course/lecturer_update-course";
        }
    }

    @GetMapping({"/courses/{courseId}"})
    public String showCourseDetail(@PathVariable String courseId, final Model model) {
        Lecturer lecturer = getLoggedInLecturer();
        Course course = courseService.findByCourseId(courseId);
        if (null == course || null == lecturer) {
            return "exception/404";
        }

        // add course log
        CourseLog courseLog = new CourseLog(courseId, CommonEnum.Action.VIEW);
        courseLogService.addCourseLog(courseLog);
//        CourseLog courseLog = new CourseLog(new CourseLogId(lecturer.getAccount().getId(), courseId, CommonEnum.Action.VIEW, LocalDateTime.now()));
//        courseLogService.addCourseLog(courseLog);

        List<Topic> topics = course.getTopics();
        model.addAttribute("course", course);
        model.addAttribute("topics", topics);
        return "lecturer/course/lecturer_course-detail";
    }

    @GetMapping("/{courseId}/delete")
    public String delete(@PathVariable String courseId) {
        Course checkExist = courseService.findByCourseId(courseId);
        if (null != checkExist) {
            for (Topic topic : checkExist.getTopics()) {
                topicService.delete(topic.getId());
            }
            courseService.delete(checkExist);
            return "redirect:/lecturer/courses/list?success";
        }
        return "redirect:/lecturer/courses/list?error";
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

    // Cần tối ưu
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

    // Cần tối ưu
    @GetMapping({"topics/{topicId}/update_topic"})
    public String editTopicProcess(@PathVariable String topicId, final Model model) {
        Topic topic = topicService.findById(topicId);
        Course course = courseService.findByCourseId(topic.getCourse().getId());
        List<Topic> topics = course.getTopics();
        model.addAttribute("course", course);
        model.addAttribute("topics", topics);
        model.addAttribute("topic", topic);
        return "lecturer/topic/lecturer_update-topic-of-course";
    }


    @PostMapping({"topics/{topicId}/update_topic"})
    public String editTopic(@PathVariable String topicId, @ModelAttribute Topic topic) {
        Topic checkTopicExist = topicService.findById(topicId);
        if (null != checkTopicExist) {
            topicService.updateTopic(topic);
            return "redirect:/lecturer/courses/updateTopic/" + topicId;
        }
        return "lecturer/topic/lecturer_add-topic-to-course";

    }

    @GetMapping({"topics/{topicId}/delete_topic/"})
    public String deleteTopic(@PathVariable String courseId, @PathVariable String topicId, final Model model) {
        Topic topic = topicService.findById(topicId);
        if (null != topic) {
            courseService.removeTopic(topic);
            topicService.softDelete(topic);
        }
        return "redirect:/lecturer/" + topic.getCourse().getId();
    }

    @GetMapping("/topics/{topicId}")
    public String viewTopicDetail(@PathVariable String topicId, final Model model) {
        Topic topic = topicService.findById(topicId);
        Course course = courseService.findByCourseId(topic.getCourse().getId());
        model.addAttribute("course", course);
        model.addAttribute("topic", topic);
        return "lecturer/topic/lecturer_topic-detail";
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
                               final Model model, HttpServletRequest request) {
        Page<Document> page = documentService.filterAndSearchDocument(course, topic, search, search, pageIndex, pageSize);

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
    public String viewDocument(@PathVariable(required = false) String documentId, final Model model) {
        Document document = documentService.findById(documentId);
        if (null == document) {
            model.addAttribute("errorMessage", "Could not found document.");
            return "exception/404";
        } else {
            // get list question
            List<Question> questions =  questionService.findByDocId(document);

            if(!document.getDocType().toString().equalsIgnoreCase("UNKNOWN")){
                String base64EncodedData = Base64.getEncoder().encodeToString(document.getContent());
                model.addAttribute("data", base64EncodedData);
            }
            model.addAttribute("newAnswer", new Answer());
            model.addAttribute("document", document);
            model.addAttribute("questions", questions);
            return "lecturer/document/lecturer_document-detail";
        }
    }


    @GetMapping({"topics/{topicId}/documents/add"})
    public String addDocumentInTopic(@PathVariable String topicId, final Model model) {
        Topic topic = topicService.findById(topicId);
        model.addAttribute("document", new Document());
        model.addAttribute("topic", topic);
        model.addAttribute("resourceTypes", DocumentEnum.DefaultTopicResourceTypes.values());
//        System.out.println(DocumentEnum.DefaultTopicResourceTypes.values());
        return "lecturer/document/lecturer_add-document";
    }

    @PostMapping("/documents/add")
    @Transactional
    public String addDocumentProcess(@ModelAttribute DocumentDTO documentDTO,
                                     @RequestParam(value = "topicId") String topicId,
                                     @RequestParam(value = "respondResourceType") String respondResourceType,
                                     @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        // Thêm resource type
        ResourceType resourceType = resourceTypeService.addResourceType(respondResourceType);
        // set resource type vào document
        documentDTO.setResourceType(resourceType);
        // set topic vào document
        Topic topic = topicService.findById(topicId);
        documentDTO.setTopic(topic);

        // Xử lý file
        // thêm check file trước khi add
        String id = "fileNotFound";
        if(file != null && !file.isEmpty()) {
            id = documentService.addFile(file);
        }
        Document document = documentService.addDocument(documentDTO, id);
        // Xử lý sau khi add document
        List<Document> resourceTypeDocuments = resourceType.getDocuments();
        if (resourceTypeDocuments == null) {
            resourceTypeDocuments = new ArrayList<>();
        }
        resourceTypeDocuments.add(document);
        // Thêm document vào resource type
        resourceType.setDocuments(resourceTypeDocuments);
        resourceTypeService.updateResourceType(resourceType);

        // Thêm document vào topic
        List<Document> topicDocuments = topic.getDocuments();
        if (topicDocuments == null) {
            topicDocuments = new ArrayList<>();
        }
        topicDocuments.add(document);
        topic.setDocuments(topicDocuments);
        topicService.updateTopic(topic);

        return "redirect:/lecturer/topics/" + topicId + "/documents/add?success";
    }

    @GetMapping({"/documents/{documentId}/update"})
    public String updateDocumentProcess(@PathVariable(required = false) String documentId, final Model model) {
        if (null == documentId) {
            documentId = "";
        }
        Document document = documentService.findById(documentId);
        if (null == document) {
            return "redirect:lecturer/documents/update?error";
        } else {
            model.addAttribute("document", new Document());
            model.addAttribute("courses", courseService.findAll());
            return "lecturer/document/";
        }
    }

    @PostMapping("/courses/update")
    @Transactional
    public String updateCourse(@ModelAttribute Course course, final Model model) {
        Course checkExist = courseService.findByCourseId(course.getId());

        if (null == checkExist) {
            return "redirect:/librarian/courses/" + course.getId()+ "/update?error";
        } else {
            Course checkCodeDuplicate = courseService.findByCourseCode(course.getCourseCode());
            if (checkCodeDuplicate != null &&
                    !checkExist.getCourseCode().equalsIgnoreCase(course.getCourseCode())) {
                return "redirect:/librarian/courses/" + course.getId()+ "/update?error";
            }
            checkExist.setCourseCode(course.getCourseCode());
            checkExist.setCourseName(course.getCourseName());
            checkExist.setDescription(course.getDescription());
            courseService.updateCourse(checkExist);
            return "redirect:/librarian/courses/" + course.getId()+ "/update?success";
        }
    }

    @GetMapping("/documents/{documentId}/delete")
    public String deleteDocument(@PathVariable String documentId) {
        Document document = documentService.findById(documentId);
        if (null != document) {
            topicService.removeDocuments(document);
            documentService.softDelete(document);
            return "redirect:/librarian/courses/list/1?success";
        }
        return "redirect:/librarian/courses/list/1?error";
    }

}
