package fpt.edu.eresourcessystem.controller;

import fpt.edu.eresourcessystem.dto.CourseDTO;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
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

    private final DocumentService documentService;
    private final CourseLogService courseLogService;

    public LecturerController(CourseService courseService, AccountService accountService, LecturerService lecturerService, TopicService topicService, DocumentService documentService, CourseLogService courseLogService) {
        this.courseService = courseService;
        this.accountService = accountService;
        this.lecturerService = lecturerService;
        this.topicService = topicService;
        this.documentService = documentService;
        this.courseLogService = courseLogService;
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
     *
     * @param pageIndex
     * @param model
     * @return
     */
    @GetMapping({"/courses/list/{pageIndex}", "/courses/list"})
    public String viewCourseManaged(@PathVariable(required = false) Integer pageIndex, final Model model) {
        // get account authorized
        Lecturer lecturer = getLoggedInLecturer();
        if (null == lecturer){
            return "common/login";
        }
        Page<Course> page = lecturerService.findListManagingCourse(lecturer, pageIndex, pageSize);
        List<Integer> pages = CommonUtils.pagingFormat(page.getTotalPages(), pageIndex);
        model.addAttribute("pages", pages);
        model.addAttribute("totalPage", page.getTotalPages());
        model.addAttribute("courses", page.getContent());
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
        if(null==course || null == lecturer){
            return "exception/404";
        }

        // add course log
        CourseLog courseLog = new CourseLog(new CourseLogId(lecturer.getAccount().getId(), courseId, CommonEnum.Action.VIEW, LocalDateTime.now()));
        courseLogService.addCourseLog(courseLog);

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
    @PostMapping({"/add_topic"})
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
        return "lecturer/topic/lecturer_add-topic-to-course";
    }

    // Cần tối ưu
    @GetMapping({"/{topicId}/update_topic"})
    public String editTopicProcess(@PathVariable String topicId, final Model model) {
        Topic topic = topicService.findById(topicId);
        Course course = courseService.findByCourseId(topic.getCourse().getId());
        List<Topic> topics = course.getTopics();
        model.addAttribute("course", course);
        model.addAttribute("topics", topics);
        model.addAttribute("topic", topic);
        return "lecturer/topic/lecturer_update-topic-of-course";
    }


    @PostMapping({"{topicId}/update_topic"})
    public String editTopic(@PathVariable String topicId, @ModelAttribute Topic topic) {
        Topic checkTopicExist = topicService.findById(topicId);
        if (null != checkTopicExist) {
            topicService.updateTopic(topic);
            return "redirect:/lecturer/courses/updateTopic/" + topicId;
        }
        return "lecturer/topic/lecturer_add-topic-to-course";

    }

    @GetMapping({"{topicId}/delete_topic/"})
    public String deleteTopic(@PathVariable String courseId, @PathVariable String topicId, final Model model) {
        Topic topic = topicService.findById(topicId);
        if (null != topic) {
            courseService.removeTopic(topic);
            topicService.delete(topicId);
        }
        return "redirect:/lecturer/" + topic.getCourse().getId();
    }

    @GetMapping("/topics/{topicId}")
    public String viewTopicDetail(@PathVariable String topicId, final Model model) {
        Topic topic = topicService.findById(topicId);
        Course course = courseService.findByCourseId(topic.getCourse().getId());
        List<Document> documents = topic.getDocuments();
//        for(ResourceType resourceType : topic.getResourceTypes()) {
//            documents = documentService.findDocumentsByResourceTypeId(resourceType.getId());
//        }
        model.addAttribute("course", course);
        model.addAttribute("topic", topic);
        model.addAttribute("documents", documents);
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
    public String showDocumentDetail(@PathVariable String documentId, final Model model) {
        Document document = documentService.findById(documentId);
        model.addAttribute("document", document);
        return "lecturer/document/lecturer_document-detail";
    }

    @GetMapping({"/documents/add"})
    public String addDocument(final Model model) {
        model.addAttribute("document", new Document());
        model.addAttribute("courses", courseService.findAll());
        model.addAttribute("resourceTypes", DocumentEnum.DefaultTopicResourceTypes.values());
//        System.out.println(DocumentEnum.DefaultTopicResourceTypes.values());
        return "lecturer/document/lecturer_add-document";
    }

    @PostMapping("/documents/add")
    public String addDocumentProcess(@ModelAttribute Document document,
//                                     @RequestParam(value="topicIds") String topicIds,
//                                     @RequestParam(value="respondResourceType") String respondResourceType,
                                     @RequestParam("file") MultipartFile file) throws IOException {
//        List<ResourceType> resourceTypes = new ArrayList<>();
//        List<String> selectedTopicIds = Arrays.asList(topicIds.split(","));
//        for(String topic:selectedTopicIds) {
//            ResourceType resourceType = new ResourceType();
//            resourceType.setResourceTypeName(respondResourceType);
//            resourceType.setTopicId(topic);
//            resourceType.getDocuments().add(document.getDocumentId());
//            // Add resource type
//            resourceTypes.add(resourceTypeService.addResourceType(resourceType));
//        }
//        List<String> resourceTypeIds = resourceTypes.stream()
//                .map(ResourceType::getResourceTypeId)
//                .collect(Collectors.toList());
//        document.setResourceTypeId(resourceTypeIds);

        // thêm check file trước khi add
        String id = documentService.addFile(file);
        documentService.addDocument(document, id);
        return "redirect:/librarian/documents/add?success";
    }

    @GetMapping({"/documents/update/{documentId}"})
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
    }}
