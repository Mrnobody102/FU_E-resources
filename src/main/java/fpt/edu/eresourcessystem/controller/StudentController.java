package fpt.edu.eresourcessystem.controller;

import fpt.edu.eresourcessystem.dto.Response.DocumentResponseDto;
import fpt.edu.eresourcessystem.dto.StudentNoteDTO;
import fpt.edu.eresourcessystem.dto.UserLogDto;
import fpt.edu.eresourcessystem.enums.AccountEnum;
import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.enums.CourseEnum;
import fpt.edu.eresourcessystem.enums.DocumentEnum;
import fpt.edu.eresourcessystem.model.*;
import fpt.edu.eresourcessystem.model.elasticsearch.EsCourse;
import fpt.edu.eresourcessystem.model.elasticsearch.EsDocument;
import fpt.edu.eresourcessystem.dto.Response.QuestionResponseDto;
import fpt.edu.eresourcessystem.service.*;
import fpt.edu.eresourcessystem.utils.CommonUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;

@Controller
@RequestMapping("/student")
@PropertySource("web-setting.properties")
public class StudentController {
    @Value("${page-size}")
    private Integer pageSize;

    private final AccountService accountService;
    private final CourseService courseService;

    private final StudentService studentService;

    private final TopicService topicService;
    private final CourseLogService courseLogService;
    private final DocumentService documentService;
    private final StudentNoteService studentNoteService;

    private final DocumentNoteService documentNoteService;

    private final QuestionService questionService;

    private final AnswerService answerService;
    private final UserLogService userLogService;

    public StudentController(AccountService accountService, CourseService courseService, StudentService studentService, TopicService topicService, CourseLogService courseLogService, DocumentService documentService, StudentNoteService studentNoteService, DocumentNoteService documentNoteService, QuestionService questionService, AnswerService answerService, UserLogService userLogService) {
        this.accountService = accountService;
        this.courseService = courseService;
        this.studentService = studentService;
        this.topicService = topicService;
        this.courseLogService = courseLogService;
        this.documentService = documentService;
        this.studentNoteService = studentNoteService;
        this.documentNoteService = documentNoteService;
        this.questionService = questionService;
        this.answerService = answerService;
        this.userLogService = userLogService;
    }

    private Student getLoggedInStudent() {
        String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Account loggedInAccount = accountService.findByEmail(loggedInEmail);
        Student loggedInStudent = studentService.findByAccountId(loggedInAccount.getId());
        return loggedInStudent;
    }

    /*
        HOME
     */
    private UserLog addUserLog(String url) {
        UserLog userLog = new UserLog(new UserLogDto(url));
        userLog = userLogService.addUserLog(userLog);
        return userLog;
    }

    @GetMapping({"", "/home"})
    public String getStudentHome(@ModelAttribute Account account, final Model model) {
        Student student = getLoggedInStudent();
        if(null == student){
            return "common/login";
        }
        List<Course> recentCourses = courseLogService.findStudentRecentView(student.getAccount().getEmail());
//        System.out.println(courseLogs);
//        List<Course> recentCourses = courseService.findByListId(courseLogs);
        model.addAttribute("recentCourses", recentCourses);
        return "student/student_home";
    }

    /*
        COURSE
     */

    /**
     * Display 5 recent course
     *
     * @param account
     * @param model
     * @return recent courses
     */
    @GetMapping("/courses")
    public String getStudentCourse(@ModelAttribute Account account, final Model model) {
        return "student/course/student_courses";
    }

    @GetMapping({"/courses/{courseId}"})
    public String viewCourseDetail(@PathVariable(required = false) String courseId, final Model model) {
        // auth
        Student student = getLoggedInStudent();
        Course course = courseService.findByCourseId(courseId);
        if (null == student) {
            return "common/login";
        } else if (null == course) {
            return "exception/404";
        } else if (null == course.getStatus() || CourseEnum.Status.PUBLISH != course.getStatus()) {
            return "exception/404";
        }

        // add course log
        CourseLog courseLog = new CourseLog(course, CommonEnum.Action.VIEW);
        courseLog = courseLogService.addCourseLog(courseLog);
        model.addAttribute("course", course);
        if (studentService.checkCourseSaved(student.getId(), courseId)) {
            model.addAttribute("saved", true);
        } else model.addAttribute("saved", false);
        // add log
        addUserLog("/student/course/" + courseId);
        return "student/course/student_course-detail";
    }

    /*
        DOCUMENT
    */
    @GetMapping({"/documents/{docId}"})
    public String viewDocumentDetail(@PathVariable String docId, final Model model) {
        // auth
        Student student = getLoggedInStudent();
        Document document = documentService.findById(docId);
        if (null == student) {
            return "common/login";
        } else if (null == document) {
            return "exception/404";
        } else if (DocumentEnum.DocumentStatusEnum.HIDE == document.getDocStatus()) {
            return "exception/404";
        }
        if (!document.getDocType().toString().equalsIgnoreCase("UNKNOWN")) {
            String base64EncodedData = Base64.getEncoder().encodeToString(document.getContent());
            model.addAttribute("data", base64EncodedData);
        }

        // Need to optimize
        Account account = accountService.findByEmail(document.getCreatedBy());

        DocumentNote documentNote = documentNoteService.findByDocIdAndStudentId(docId, student.getId());
        if (null != documentNote) {
            model.addAttribute("note", documentNote);
        }
        model.addAttribute("documentNote", new DocumentNote());
        // get list questions
        List<Question> questions = questionService.findByDocId(document);
        List<QuestionResponseDto> questionResponseDtos = new ArrayList<>();
        List<QuestionResponseDto> myQuestionResponseDtos = new ArrayList<>();

        // Need to optimize - dùng AJAX ik =)))))))))))))))))))))))))))))))))))))))))))))))))))
        for (Question q : questions) {
            if (!q.getStudent().getId().equals(student.getId())) {
                questionResponseDtos.add(new QuestionResponseDto(q));
            } else {
                myQuestionResponseDtos.add(new QuestionResponseDto(q));
            }
        }

        // get others doc
        if(null!= document.getTopic()){
            List<DocumentResponseDto> relevantDocuments = documentService
                    .findRelevantDocument(document.getTopic().getId(),docId);
            if(null!= relevantDocuments && relevantDocuments.size()> 0){
                model.addAttribute("relevantDocuments", relevantDocuments);
            }else {
                relevantDocuments = new ArrayList<>();
                relevantDocuments.add(new DocumentResponseDto(document));
                model.addAttribute("relevantDocuments", relevantDocuments);
            }
        }
        model.addAttribute("questions", questionResponseDtos);
        model.addAttribute("myQuestions", myQuestionResponseDtos);
        model.addAttribute("document", document);
        model.addAttribute("account", account);
        model.addAttribute("newQuestion", new Question());
        model.addAttribute("newAnswer", new Answer());
        if (studentService.checkDocSaved(student.getId(), docId)) {
            model.addAttribute("saved", true);
        } else model.addAttribute("saved", false);
        // add log
        addUserLog("/student/documents/" + docId);
        return "student/course/student_document-detail";
    }

    /*
        STUDENT - MY LIBRARY
     */

    @GetMapping({"/my_library/saved_courses", "/my_library"})
    public String viewCourseSaved(final Model model) {
        // get account authorized
        Student student = getLoggedInStudent();
        List<String> savedCourses = student.getSavedCourses();
        if (null != savedCourses) {
            List<Course> courses = courseService.findByListId(savedCourses);
            model.addAttribute("coursesSaved", courses);
        }
        return "student/library/student_saved_courses";
    }

    @GetMapping({"/my_library/saved_documents"})
    public String viewDocumentSaved(final Model model) {
        // get account authorized
        Student student = getLoggedInStudent();
        List<String> savedDocuments = student.getSavedDocuments();
        if (null != savedDocuments) {
            List<Document> documents = documentService.findByListId(savedDocuments);
            model.addAttribute("documentsSaved", documents);
        }
        return "student/library/student_saved_documents";
    }

    @GetMapping({"/topics/{topicId}"})
    public String viewTopicDetail(@PathVariable(required = false) String topicId, final Model model) {
        // get account authorized
        Student student = getLoggedInStudent();
        Topic topic = topicService.findById(topicId);
        Course course = topic.getCourse();
        model.addAttribute("course", course);
        model.addAttribute("topic", topic);
        course.getTopics().remove(topic);
        List<Topic> topics = course.getTopics();
        model.addAttribute("topics", topics);
        if (studentService.checkCourseSaved(student.getId(), course.getId())) {
            model.addAttribute("saved", true);
        } else model.addAttribute("saved", false);
        // add log
        addUserLog("/student/topics/" + topicId);
        return "student/topic/student_topic-detail";
    }

    @GetMapping({"/search_course/{pageIndex}"})
    public String viewSearchCourse(@PathVariable Integer pageIndex,
                                   @RequestParam(required = false, defaultValue = "") String search,
//                                   @RequestParam(required = false, defaultValue = "all") String filter,
                                   final Model model) {
//        // auth
        Student student = getLoggedInStudent();
        // search in mongodb
        Page<Course> page = courseService.findByCourseNameOrCourseCode(search, search, pageIndex, pageSize);
        // search by elastic search
//        SearchPage<EsCourse> page = courseService.searchCourse(search, pageIndex, pageSize);
        List<Integer> pages = CommonUtils.pagingFormat(page.getTotalPages(), pageIndex);
        model.addAttribute("pages", pages);
        model.addAttribute("totalPage", page.getTotalPages());
        model.addAttribute("courses", page.getContent());
        model.addAttribute("search", search);
        model.addAttribute("roles", AccountEnum.Role.values());
//        model.addAttribute("filter", filter);
        model.addAttribute("currentPage", pageIndex);
        model.addAttribute("search", search);
        // add log
        addUserLog("/student/search_course/" + pageIndex + "?search=" + search);
        return "student/course/student_courses";
    }

    /**
     * STUDENT - MY NOTES
     */

    @GetMapping({"/my_library/my_notes/{pageIndex}"})
    public String viewMyNote(@RequestParam(required = false, defaultValue = "") String search, @PathVariable Integer pageIndex, final Model model) {
        // get account authorized
        Student student = getLoggedInStudent();
        Page<StudentNote> page = studentNoteService.getNoteByStudent(student.getId(), pageIndex, pageSize);
        if (null != page) {
            List<Integer> pages = CommonUtils.pagingFormat(page.getTotalPages(), pageIndex);
            model.addAttribute("pages", pages);
            model.addAttribute("totalPage", page.getTotalPages());
            model.addAttribute("studentNotes", page.getContent());
            model.addAttribute("search", search);
            model.addAttribute("roles", AccountEnum.Role.values());
            model.addAttribute("currentPage", pageIndex);
            model.addAttribute("search", search);
        }
        return "student/library/student_my-notes";
    }


    @GetMapping("/my_note/student_notes/add")
    public String addMyNoteProcess(final Model model) {
        model.addAttribute("studentNote", new StudentNote());
        return "student/library/student_add-student-note";
    }

    @PostMapping("/my_note/student_notes/add")
    @Transactional
    public String addMyNote(@ModelAttribute StudentNoteDTO studentNoteDTO,
                            BindingResult result) {
        Student student = getLoggedInStudent();
        if (null == student) {
            return "common/login";
        } else if (null == studentNoteDTO || result.hasErrors()) {
            return "redirect:/student/my_note/student_notes/add?error";
        }
        studentNoteDTO.setStudentId(student.getId());
        StudentNote studentNote = studentNoteService.addStudentNote(studentNoteDTO);
        if (null != studentNote) {
            // add log
            addUserLog("/student/my_note/student_notes/add/" + studentNote);
            return "redirect:/student/my_note/student_notes/add?success";
        } else {
            return "redirect:/student/my_note/student_notes/add?error";
        }
    }

    // tối ưu
    @GetMapping("/my_library/my_questions/history")
    public String viewMyQuestions(final Model model) {
        Student student = getLoggedInStudent();
        List<Question> questions = questionService.findByStudent(student);
        for (Question q : questions) {
            q.setAnswers(new HashSet<>(answerService.findByStudentAnsQuestion(student, q)));
        }
        model.addAttribute("studentQuestions", questions);
        // add log
        addUserLog("/my_library/my_questions/history");
        return "student/library/student_my-questions-and-answers";
    }


    /*
        SEARCH
     */

    @GetMapping({"/search"})
    public String getSearchResults(@RequestParam(required = false, value = "search") String search,
                                   @RequestParam(required = false, defaultValue = "all") String filter,
                                   final Model model) {
        Iterable<EsDocument> esDocuments;
        esDocuments = documentService.searchDocument(search.trim());
//        if (null == filter || "all".equals(filter)) {
//            esDocuments = documentService.searchDocument(search);
//        }
//        else if ("e_resource".equals(filter)) {
//            page = courseService.findByCourseNameLike(search, pageIndex, pageSize);
//        } else {
//            page = courseService.findByCourseCodeLike(search, pageIndex, pageSize);
//        }

        model.addAttribute("foundDocuments", esDocuments);
        model.addAttribute("search", search);
        return "student/student_search-results";
    }

    @GetMapping({"/chat"})
    public String goHomePage() {
        return "student/test_notification";
    }
}
