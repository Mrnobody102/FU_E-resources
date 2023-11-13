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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/librarian")
@PropertySource("web-setting.properties")
public class LibrarianController {

    @Autowired
    JavaMailSender javaMailSender;


    @Value("${page-size}")
    private Integer pageSize;
    private final AccountService accountService;
    private final LibrarianService librarianService;
    private final LecturerService lecturerService;
    private final StudentService studentService;
    private final CourseService courseService;
    private final TopicService topicService;
    private final ResourceTypeService resourceTypeService;
    private final DocumentService documentService;

    private final LecturerCourseService lecturerCourseService;

    public LibrarianController(AccountService accountService, LibrarianService librarianService, LecturerService lecturerService, StudentService studentService, CourseService courseService, TopicService topicService, ResourceTypeService resourceTypeService, DocumentService documentService, LecturerCourseService lecturerCourseService) {
        this.accountService = accountService;
        this.librarianService = librarianService;
        this.lecturerService = lecturerService;
        this.studentService = studentService;
        this.courseService = courseService;
        this.topicService = topicService;
        this.resourceTypeService = resourceTypeService;
        this.documentService = documentService;
        this.lecturerCourseService = lecturerCourseService;
    }

    /*
    DASHBOARD
     */

    /**
     * @return dashboard page
     */
    @GetMapping
    public String getLibrarianDashboard() {
        return "librarian/librarian_dashboard";
    }

    /*
        COURSES MANAGEMENT
     */

    /**
     * @param model
     * @return
     */
    @GetMapping({"/courses/add"})
    public String addCourse(final Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("statuses", CourseEnum.Status.values());
        return "librarian/course/librarian_add-course";
    }

    /**
     * @param courseDTO
     * @param lecturer
     * @return
     */
    @PostMapping("/courses/add")
    public String addCourseProcess(@ModelAttribute CourseDTO courseDTO,
                                   @RequestParam(value = "lecturer") String lecturer) {
        Course course = new Course(courseDTO, CourseEnum.Status.HIDE);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return "exception/404";
        }
        String currentPrincipalName = authentication.getName();
        Librarian librarian = librarianService.findByAccountId(accountService.findByEmail(currentPrincipalName).getId());
        course.setLibrarian(librarian);
        // check course code duplicate
        Course checkExist = courseService.findByCourseCode(course.getCourseCode());
        if (null == checkExist) {
            // check lecturer param exist
            if (lecturer != null && !"".equals(lecturer.trim())) {
                // get account by EMAIL
                Account account = accountService.findByEmail(lecturer);
                if (null != account) {
                    // get lecturer by account
                    Lecturer foundLecturer = lecturerService.findByAccountId(account.getId());
                    if (null != foundLecturer) {
                        // ADD COURSE
                        Course result = courseService.addCourse(course);
                        // Xử lý sau khi add course
                        // check lecturer exist
                        if (result != null) {
                            // Add log lecturer_course
                            // create new lecturerCourseId
                            LecturerCourseId lecturerCourseId = new LecturerCourseId();
                            lecturerCourseId.setLecturerId(foundLecturer.getId());
                            // set course Id that added
                            lecturerCourseId.setCourseId(result.getId());
                            lecturerCourseId.setCreatedDate(LocalDate.now());

                            // save new lecturer manage to the course
                            LecturerCourse lecturerCourse = new LecturerCourse();
                            lecturerCourse.setId(lecturerCourseId);

                            LecturerCourse addLecturerCourseResult = lecturerCourseService.add(lecturerCourse);
                            // check save lecturerCourse to database
                            if (null != addLecturerCourseResult) {

                                // add lecturer to  course
                                List<LecturerCourseId> lecturerCourseIds = new ArrayList<>();
                                lecturerCourseIds.add(addLecturerCourseResult.getId());

                                result.setLecturerCourseIds(lecturerCourseIds);
                                result.setLecturer(foundLecturer);
                                // update course lecturers
                                result = courseService.updateCourse(result);
                                foundLecturer.getCourses().add(course);
                                librarian.getCreatedCourses().add(course);
                                // Thêm course vào danh sách tạo của librarian
                                librarianService.updateLibrarian(librarian);
                                // Thêm course vào danh sách quản lý của lecturer
                                lecturerService.updateLecturer(foundLecturer);

                                if (null != result) {
                                    return "redirect:/librarian/courses/add?success";
                                }
                            }
                        }
                    }
                } else {
                    return "redirect:/librarian/courses/add?error";
                }

            } else {
                Course addedCourse = courseService.addCourse(course);
                librarian.getCreatedCourses().add(course);
                librarianService.updateLibrarian(librarian);
                if (null != addedCourse) {
                    return "redirect:/librarian/courses/add?success";
                }
            }
        }
        return "redirect:/librarian/courses/add?error";

    }

    /**
     * @param courseId
     * @param model
     * @return
     */
    @GetMapping({"/courses/{courseId}/update"})
    public String updateCourseProcess(@PathVariable String courseId,
                                      final Model model) {
        if (null == courseId) {
            courseId = "";
        }
        Course course = courseService.findByCourseId(courseId);
        if (null == course) {
            return "redirect:librarian/courses/update?error";
        } else {
            List<Account> lecturers = accountService.findAllLecturer();
            model.addAttribute("lecturers", lecturers);
            model.addAttribute("course", course);
            model.addAttribute("statuses", CourseEnum.Status.values());
            return "librarian/course/librarian_update-course";
        }
    }

    /**
     * @param course
     * @param model
     * @return
     */
    @PostMapping("/courses/update")
    public String updateCourse(@ModelAttribute Course course, final Model model) {
        Course checkExist = courseService.findByCourseId(course.getId());

        if (null == checkExist) {
            return "redirect:/librarian/courses/" + course.getId() + "/update?error";
        } else {
            Course checkCodeDuplicate = courseService.findByCourseCode(course.getCourseCode());
            if (checkCodeDuplicate != null &&
                    !checkExist.getCourseCode().equalsIgnoreCase(course.getCourseCode())) {
                return "redirect:/librarian/courses/" + course.getId() + "/update?error";
            }
            checkExist.setCourseCode(course.getCourseCode());
            checkExist.setCourseName(course.getCourseName());
            checkExist.setDescription(course.getDescription());
            courseService.updateCourse(checkExist);
            return "redirect:/librarian/courses/" + course.getId() + "/update?success";
        }
    }

    /**
     * @return
     */
    @GetMapping({"/courses/list"})
    public String showCourses() {
        return "librarian/course/librarian_courses";
    }

    @GetMapping("/courses/list/{pageIndex}")
    String showCoursesByPage(@PathVariable Integer pageIndex,
                             @RequestParam(required = false, defaultValue = "") String search,
                             final Model model, HttpServletRequest request) {
        Page<Course> page;
        page = courseService.findByCodeOrNameOrDescription(search, search, search, pageIndex, pageSize);
        List<Integer> pages = CommonUtils.pagingFormat(page.getTotalPages(), pageIndex);
        model.addAttribute("pages", pages);
        model.addAttribute("totalPage", page.getTotalPages());
        model.addAttribute("courses", page.getContent());
        model.addAttribute("search", search);
        model.addAttribute("currentPage", pageIndex);
        return "librarian/course/librarian_courses";
    }

    /**
     * @param courseId
     * @param model
     * @return
     */
    @GetMapping({"/courses/{courseId}"})
    public String showCourseDetail(@PathVariable String courseId, final Model model) {
        Course course = courseService.findByCourseId(courseId);
        model.addAttribute("course", course);
        return "librarian/course/librarian_course-detail";
    }

    /**
     * @param courseId
     * @return
     */
    @GetMapping("/courses/{courseId}/delete")
    public String deleteCourse(@PathVariable String courseId) {
        Course checkExistCourse = courseService.findByCourseId(courseId);
        if (null != checkExistCourse) {
            courseService.softDelete(checkExistCourse);
            return "redirect:/librarian/courses/list/1?success";
        }
        return "redirect:/librarian/courses/list/1?error";
    }

    /**
     * @param courseId
     * @param model
     * @return
     */
    @GetMapping({"/courses/{courseId}/addLecturers"})
    public String addLecturersProcess(@PathVariable String courseId, final Model model) {
        Course course = courseService.findByCourseId(courseId);
        Lecturer lecturers = lecturerService.findByCourseId(courseId);
        List<Account> accounts = accountService.findAllLecturer();
        model.addAttribute("course", course);
        model.addAttribute("lecturers", lecturers);
        model.addAttribute("accounts", accounts);
        return "librarian/course/librarian_add-lecturer-to-course";
    }

    /**
     * @param courseId
     * @param search
     * @param model
     * @return
     */
    @GetMapping({"/courses/{courseId}/updateLecturers"})
    public String updateLecturersProcess(@PathVariable String courseId, @RequestParam String search, final Model model) {
        Lecturer courseLecturers = lecturerService.findByCourseId(courseId);
        List<Account> lecturers = accountService.searchLecturer(search);
        model.addAttribute("courseLecturers", courseLecturers);
        model.addAttribute("lecturers", lecturers);
        return "librarian/course/librarian_add-lecturer-to-course";
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
        return "librarian/document/librarian_documents";
    }

    @GetMapping({"/documents/{documentId}"})
    public String showDocumentDetail(@PathVariable String documentId, final Model model) {
        Document document = documentService.findById(documentId);
        model.addAttribute("document", document);
        return "librarian/document/librarian_document-detail";
    }

    @GetMapping({"/documents/add"})
    public String addDocument(final Model model) {
        model.addAttribute("document", new Document());
        model.addAttribute("courses", courseService.findAll());
        model.addAttribute("resourceTypes", DocumentEnum.DefaultTopicResourceTypes.values());
        System.out.println(DocumentEnum.DefaultTopicResourceTypes.values());
        return "librarian/document/librarian_add-document";
    }

    @PostMapping("/documents/add")
    public String addDocumentProcess(@ModelAttribute DocumentDTO documentDTO,
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
        documentService.addDocument(documentDTO, id);
        return "redirect:/librarian/documents/add?success";
    }

    @GetMapping({"/documents/update/{documentId}"})
    public String updateDocumentProcess(@PathVariable(required = false) String documentId, final Model model) {
        if (null == documentId) {
            documentId = "";
        }
        Document document = documentService.findById(documentId);
        if (null == document) {
            return "redirect:librarian/documents/update?error";
        } else {
            model.addAttribute("document", new Document());
            model.addAttribute("courses", courseService.findAll());
            return "librarian/document/librarian_update-document";
        }
    }

//    @GetMapping({"/lectures/{username}"})
//    public String findLecturerByUsername(@PathVariable String username, final Model model) {
//        Account account = accountService.findByUsername(username);
//        Lecturer lecturer = lecturerService.findByAccountId(account.getId());
//        model.addAttribute("lecturer", lecturer);
//        return "librarian_lecture-detail";
//    }


    @GetMapping({"/courses/{courseId}/add-lecture"})
    public String addLecturer(@PathVariable String courseId, final Model model) {
        Course course = courseService.findByCourseId(courseId);
        List<Account> accounts = accountService.findAllLecturer();
        model.addAttribute("course", course);
        model.addAttribute("accounts", accounts);
        return "librarian/course/librarian_course-detail";
    }

    @GetMapping({"/courses/{courseId}/remove-lecture"})
    public String removeLecture(@PathVariable String courseId, final Model model) {
        Course course = courseService.findByCourseId(courseId);
        boolean removed = lecturerService.removeCourse(course.getLecturer(), course);
        boolean removed1 = courseService.removeLecture(courseId);
        if (true == removed && removed1 == true) {
            return "redirect:/librarian/courses/{courseId}/add-lecture?success";
        } else return "redirect:/librarian/courses/{courseId}/add-lecture?error";
    }

    /**
     * @param courseId course id
     * @param model    model attribute
     * @return
     */
    @PostMapping({"/courses/{courseId}/add-lecture"})
    public String addLecturer(@PathVariable String courseId, @RequestParam String lecturerEmail, final Model model) {
        Account account = accountService.findByEmail(lecturerEmail);
        Lecturer lecturer = lecturerService.findByAccountId(account.getId());
        Course course = courseService.updateLectureId(courseId, lecturer);

        lecturer.getCourses().add(course);
        lecturerService.updateLecturer(lecturer);

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("maihoa362001@gmail.com");
        message.setTo("ngaydoidemcho93@gmail.com");
        message.setSubject("Subject : Thông báo quản lý môn học " + course.getCourseCode());
        message.setText("Body : " +
                "Tên môn học: " + course.getCourseName());

        javaMailSender.send(message);

//        if (null != lecturer) {
//            Account accountLecturer = lecturer.getAccount();
//            model.addAttribute("accountLecturer", accountLecturer);
//        }
        model.addAttribute("course", course);
//        model.addAttribute("topics", topics);
        return "librarian/course/librarian_course-detail";
    }

    @GetMapping({"/lectures"})
    public String showLectures(final Model model) {
//        Course course = courseService.findByCourseId(courseId);
//        List<Topic> topics = topicService.findByCourseId(courseId);
//        Lecturer lecturer = lecturerService.findCurrentCourseLecturer(courseId);
//
//        if (null != lecturer) {
//            Account accountLecturer = accountService.findById(lecturer.getAccount().getId());
//            model.addAttribute("accountLecturer", accountLecturer);
//        }
        List<Lecturer> lecturers = lecturerService.findAll();
        model.addAttribute("lecturers", lecturers);
        return "librarian/lecture/librarian_lectures";
//        return  "librarian/course/detailCourseTest";
    }

    @GetMapping({"/lectures/list"})
    public String showLecture() {
        return "librarian/lecture/librarian_lectures";
    }

    @GetMapping("/lectures/list/{pageIndex}")
    String showLectureByPage(@PathVariable Integer pageIndex,
                             @RequestParam(required = false, defaultValue = "") String search,
                             final Model model, HttpServletRequest request) {
//        Page<Course> page;
//        page = courseService.findByCodeOrNameOrDescription(search, search, search, pageIndex, pageSize);
//        List<Integer> pages = CommonUtils.pagingFormat(page.getTotalPages(), pageIndex);
//        model.addAttribute("pages", pages);
//        model.addAttribute("totalPage", page.getTotalPages());
//        model.addAttribute("courses", page.getContent());
//        model.addAttribute("search", search);
//        model.addAttribute("currentPage", pageIndex);
        return "librarian/lecture/librarian_lectures";
    }


    @GetMapping({"/lectures/{lectureId}"})
    public String showLectureDetail(@PathVariable String lectureId, final Model model) {

//        if (null != lecturer) {
//            Account accountLecturer = accountService.findById(lecturer.getAccount().getId());
//            model.addAttribute("accountLecturer", accountLecturer);
//        }
        Lecturer lecturer = lecturerService.findLecturerById(lectureId);
        List<LecturerCourse> lecturerCourseList = lecturerCourseService.findLecturerCoursesById(lecturer);
//        lecturer.setLecturerCourses(lecturerCourseList);
        model.addAttribute("lecturer", lecturer);

        return "librarian/lecture/librarian_lecture-detail";
    }

    @GetMapping("/lectures/create-lecture")
    public String showAddLectureForm(Model model) {
        List<Course> allCourses = courseService.findAll(); // Retrieve all available courses
        model.addAttribute("allCourses", allCourses);
        model.addAttribute("lecture", new Lecturer());
        return "librarian/lecture/librarian_add-lecture";
    }


    @PostMapping("/lectures/create-lecture")
    public String addLectureCourse(@ModelAttribute Lecturer lecturer, @RequestParam String email) {
        Account lecture = accountService.findByEmail(email);
        if (lecture != null) {
            lecturer.setAccount(lecture);
            if (lecturerService.findLecturerByAccount(lecture) == null) {
                lecturerService.addLecturer(lecturer);
//        lecturer.getAccount().setRole();
                for (int i = 0; i < lecturer.getCourses().size(); i++) {
                    courseService.updateLectureId(String.valueOf(lecturer.getCourses().get(i)), lecturer);
                }
                return "redirect:/librarian/lectures/create-lecture?success";
            } else
                return "redirect:/librarian/lectures/create-lecture?error";
        } else
            return "redirect:/librarian/lectures/create-lecture?error";

    }
}
