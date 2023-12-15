package fpt.edu.eresourcessystem.controller;

import fpt.edu.eresourcessystem.dto.FeedbackDto;
import fpt.edu.eresourcessystem.dto.Response.NotificationResponseDto;
import fpt.edu.eresourcessystem.dto.Response.QuestionResponseDto;
import fpt.edu.eresourcessystem.dto.StudentNoteDto;
import fpt.edu.eresourcessystem.dto.UserLogDto;
import fpt.edu.eresourcessystem.enums.AccountEnum;
import fpt.edu.eresourcessystem.enums.CourseEnum;
import fpt.edu.eresourcessystem.enums.DocumentEnum;
import fpt.edu.eresourcessystem.enums.QuestionAnswerEnum;
import fpt.edu.eresourcessystem.model.*;
import fpt.edu.eresourcessystem.model.elasticsearch.EsCourse;
import fpt.edu.eresourcessystem.model.elasticsearch.EsDocument;
import fpt.edu.eresourcessystem.service.*;
import fpt.edu.eresourcessystem.service.elasticsearch.EsCourseService;
import fpt.edu.eresourcessystem.service.elasticsearch.EsDocumentService;
import fpt.edu.eresourcessystem.utils.CommonUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.print.Doc;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static fpt.edu.eresourcessystem.constants.Constants.PAGE_SIZE;
import static fpt.edu.eresourcessystem.constants.UrlConstants.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/student")
public class StudentController {
    private final AccountService accountService;
    private final CourseService courseService;
    private final EsCourseService esCourseService;
    private final StudentService studentService;
    private final TopicService topicService;
    //    private final CourseLogService courseLogService;
    private final DocumentService documentService;
    private final EsDocumentService esDocumentService;
    private final StudentNoteService studentNoteService;
    private final DocumentNoteService documentNoteService;
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserLogService userLogService;
    private final FeedbackService feedbackService;
    private final NotificationService notificationService;

    private Student getLoggedInStudent() {
        String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (null == loggedInEmail || "anonymousUser".equals(loggedInEmail)) {
            return null;
        }
        Account loggedInAccount = accountService.findByEmail(loggedInEmail);
        if (loggedInAccount != null) {
            return studentService.findByAccountId(loggedInAccount.getId());
        } else return null;
    }

    private String getLoggedInStudentMail() {
        String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return loggedInEmail;
    }

    /*
        HOME
     */
    private UserLog addUserLog(String url) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Account loggedInUser = accountService.findByEmail(currentPrincipalName);
        UserLog userLog = new UserLog(new UserLogDto(url, getLoggedInStudent().getAccount().getEmail(), AccountEnum.Role.STUDENT));
        userLog = userLogService.addUserLog(userLog);
        System.out.println(userLog);
        return userLog;
    }

    @GetMapping({"", "/home"})
    public String getStudentHome(@ModelAttribute Account account, final Model model) {
        Student student = getLoggedInStudent();
        if (null == student) {
            return LOGIN_REQUIRED;
        }
        List<Course> recentCourses = userLogService.findStudentRecentView(student.getAccount().getEmail());
        model.addAttribute("recentCourses", recentCourses);
        return "student/student_home";
    }

    /*
        COURSE
     */

    /**
     * Display 5 recent course
     *
     * @param account account
     * @return recent courses
     */
    @GetMapping("/courses")
    public String getStudentCourse(@ModelAttribute Account account) {
        return "student/course/student_courses";
    }

    @GetMapping({"/courses/{courseId}"})
    public String viewCourseDetail(@PathVariable(required = false) String courseId, final Model model) {
        // auth
        Student student = getLoggedInStudent();
        Course course = courseService.findByCourseId(courseId);
        if (null == student) {
            return LOGIN_REQUIRED;
        } else if (null == course) {
            return "exception/404";
        } else if (CourseEnum.Status.PUBLISH != course.getStatus()) {
            return "exception/404";
        }
        model.addAttribute("course", course);
        if (studentService.checkCourseSaved(student.getId(), courseId)) {
            model.addAttribute("saved", true);
        } else model.addAttribute("saved", false);
        // add log
        addUserLog("/student/courses/" + courseId);
        return "student/course/student_course-detail";
    }

    /*
        DOCUMENT
    */
    @GetMapping({"/documents/{docId}"})
    public String viewDocumentDetail(@PathVariable String docId,
                                     @RequestParam(required = false) String questionId,
                                     final Model model) throws IOException {
        // auth
        Student student = getLoggedInStudent();
        Document document = documentService.findById(docId);
        if (null == student) {
            return LOGIN_REQUIRED;
        } else if (null == document) {
            return "exception/404";
        } else if (DocumentEnum.DocumentStatusEnum.HIDE == document.getDocStatus()) {
            return "exception/404";
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

        // Need to optimize
        Account account = accountService.findByEmail(document.getCreatedBy());

        DocumentNote documentNote = documentNoteService.findByDocIdAndStudentId(docId, student.getId());
        if (null != documentNote) {
            model.addAttribute("documentNote", documentNote);
        } else model.addAttribute("documentNote", new DocumentNote());

        List<QuestionResponseDto> myQuestionResponseDtos = new ArrayList<>();
        if (null != questionId) {
            myQuestionResponseDtos.add(new QuestionResponseDto(questionService.findById(questionId)));
        } else {
            questionService.findByStudentLimitAndSkip(student, document, 3, 0);
        }

        List<QuestionResponseDto> questionResponseDtos = questionService.findByOtherStudentLimitAndSkip(student, document, 3, 0);
        System.out.println(questionResponseDtos.size());
        //        // get others doc
//        if (null != document.getTopic()) {
//            List<DocumentResponseDto> relevantDocuments = documentService
//                    .findRelevantDocument(document.getTopic().getId(), docId);
//            if (null != relevantDocuments && relevantDocuments.size() > 0) {
//                model.addAttribute("relevantDocuments", relevantDocuments);
//            } else {
//                relevantDocuments = new ArrayList<>();
//                relevantDocuments.add(new DocumentResponseDto(document));
//                model.addAttribute("relevantDocuments", relevantDocuments);
//            }
//        }
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
    public String viewCourseSaved(@RequestParam(required = false, defaultValue = "1") int pageIndex,
                                  @RequestParam(required = false, defaultValue = "") String search,
                                  final Model model) {
        // get account authorized
        Student student = getLoggedInStudent();
        List<String> savedCourses = student != null ? student.getSavedCourses() : null;
        if (null != savedCourses) {
//            List<Course> courses = courseService.findByListId(savedCourses);
            Page<Course> courses = courseService.findByListCourseIdAndSearch(search, savedCourses, pageIndex, PAGE_SIZE);
            model.addAttribute("coursesSaved", courses.getContent());
            model.addAttribute("totalPages", courses.getTotalPages());
            model.addAttribute("totalItems", courses.getTotalElements());
            System.out.println(courses.getContent().size());
        }
        model.addAttribute("search", search);
        model.addAttribute("currentPage", pageIndex);
        return "student/library/student_saved_courses";
    }

    @GetMapping({"/my_library/saved_documents"})
    public String viewDocumentSaved(@RequestParam(required = false, defaultValue = "1") int pageIndex,
                                    @RequestParam(required = false, defaultValue = "") String search,
                                    final Model model) {
        // get account authorized
        Student student = getLoggedInStudent();
        List<String> savedDocuments = student != null ? student.getSavedDocuments() : null;
        if (null != savedDocuments) {
//            List<Document> documents = documentService.findByListId(savedDocuments);
            Page<Document> documents = documentService.findByListDocumentIdAndSearch(search, savedDocuments, pageIndex, PAGE_SIZE);
            model.addAttribute("totalPages", documents.getTotalPages());
            System.out.println(documents.getContent().size());
            model.addAttribute("documentsSaved", documents.getContent());
            model.addAttribute("totalItems", documents.getTotalElements());

        }
        model.addAttribute("search", search);
        model.addAttribute("currentPage", pageIndex);
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
        if (student != null && studentService.checkCourseSaved(student.getId(), course.getId())) {
            model.addAttribute("saved", true);
        }
        // add log
        addUserLog("/student/topics/" + topicId);
        return "student/topic/student_topic-detail";
    }

    @GetMapping({"/search_course/{pageIndex}"})
    public String viewSearchCourse(@PathVariable Integer pageIndex,
                                   @RequestParam(required = false, defaultValue = "") String search,
//                                   @RequestParam(required = false, defaultValue = "all") String filter,
                                   final Model model) {
        // search in mongodb
        Page<Course> page = courseService.findByCourseNameOrCourseCode(search, search, pageIndex, PAGE_SIZE);
        // search by elastic search
//        SearchPage<EsCourse> page = courseService.searchCourse(search, pageIndex, PAGE_SIZE);
//        List<Integer> pages = CommonUtils.pagingFormat(page.getTotalPages(), pageIndex);
//        model.addAttribute("pages", pages);
        model.addAttribute("totalPages", page.getTotalPages());
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
    public String viewMyNote(@RequestParam(required = false, defaultValue = "") String search,
                             @PathVariable Integer pageIndex, final Model model) {
        // get account authorized
        Student student = getLoggedInStudent();
        Page<StudentNote> page = null;
        if (student != null) {
            page = studentNoteService.getNoteByStudent(student.getId(), pageIndex, PAGE_SIZE);
        }
        List<DocumentNote> studentDocumentNotes = documentNoteService.findByStudent(student.getId());
        System.out.println(studentDocumentNotes.size());
        if (null != page) {
            List<Integer> pages = CommonUtils.pagingFormat(page.getTotalPages(), pageIndex);
            model.addAttribute("pages", pages);
            model.addAttribute("totalPage", page.getTotalPages());
            model.addAttribute("studentNotes", page.getContent());
            model.addAttribute("search", search);
            model.addAttribute("roles", AccountEnum.Role.values());
            model.addAttribute("currentPage", pageIndex);
            model.addAttribute("search", search);
            // document notes model
            model.addAttribute("studentDocumentNotes", studentDocumentNotes);
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
    public String addMyNote(@ModelAttribute StudentNoteDto studentNoteDTO,
                            BindingResult result) {
        Student student = getLoggedInStudent();
        if (null == student) {
            return LOGIN_REQUIRED;
        } else if (null == studentNoteDTO || result.hasErrors()) {
            return "redirect:/student/my_note/student_notes/add?error";
        }
        studentNoteDTO.setStudentId(student.getId());
        StudentNote studentNote = studentNoteService.addStudentNote(studentNoteDTO);
        if (null != studentNote) {
            // add log
            addUserLog("/student/my_note/student_notes/add/" + studentNote);
            return "redirect:/student/my_note/student_notes/add" + SUCCESS_PARAM;
        } else {
            return "redirect:/student/my_note/student_notes/add?error";
        }
    }

    @GetMapping({"/my_note/student_notes/{studentNoteId}", "/my_note/student_notes/{studentNoteId}/updateProcess"})
    public String viewMyNoteProcess(@PathVariable String studentNoteId, final Model model) {
        StudentNote studentNote = studentNoteService.findById(studentNoteId);
        System.out.println(studentNoteId);
        System.out.println(studentNote);
        if (null == studentNoteId) {
            return "exception/404";

        } else {
            model.addAttribute("studentNote", studentNote);
            System.out.println(studentNote);
            return "student/library/student_my-note_detail";
        }

    }

    @PostMapping("/my_note/student_notes/update")
    @Transactional
    public String updateMyNote(@ModelAttribute StudentNote studentNote,
                               BindingResult result) {
        Student student = getLoggedInStudent();
        StudentNote checkExist = studentNoteService.findById(studentNote.getId());
        if (null == student) {
            return LOGIN_REQUIRED;
        } else if (null == checkExist || result.hasErrors()) {
            return "redirect:/student/my_note/student_notes/" + studentNote.getId() + "?error";
        }
        checkExist.setTitle(studentNote.getTitle());
        checkExist.setDescription(studentNote.getDescription());
        checkExist.setEditorContent(studentNote.getEditorContent());
        studentNote = studentNoteService.updateStudentNote(checkExist);
        if (null != studentNote) {
            // add log
            addUserLog("/student/my_note/student_notes/" + studentNote.getId() + "?success");
            return "redirect:/student/my_note/student_notes/" + studentNote.getId() + SUCCESS_PARAM;
        } else {
            return "redirect:/student/my_note/student_notes/" + studentNote.getId() + "?error";
        }
    }

    @PostMapping("/my_note/student_notes/delete")
    @Transactional
    public String deleteMyNote(@RequestParam String studentNoteId) {
        Student student = getLoggedInStudent();
        StudentNote checkExist = studentNoteService.findById(studentNoteId);
        if (null == student) {
            return LOGIN_REQUIRED;
        } else if (null == checkExist) {
            return "redirect:/student/my_note/student_notes/" + studentNoteId + "?error";
        }
        boolean checkDeleted = studentNoteService.softDeleteStudentNote(checkExist);
        if (checkDeleted) {
            // add log
            addUserLog("/student/my_note/student_notes/delete/" + studentNoteId + "?success");
            return "redirect:/student/my_note/student_notes/add" + SUCCESS_PARAM;
        } else {
            return "redirect:/student/my_note/student_notes/" + studentNoteId + "?error";
        }
    }

    // tối ưu
    @GetMapping("/my_library/my_questions/history")
    public String viewMyQuestions(@RequestParam(required = false, defaultValue = "1") int pageIndex,
                                  @RequestParam(required = false, defaultValue = "") String search,
                                  @RequestParam(required = false, defaultValue = "all") String status,
                                  final Model model) {
        Student student = getLoggedInStudent();
        QuestionAnswerEnum.Status findStatus = null;
        if ("new-reply".equals(status)) {
            findStatus = QuestionAnswerEnum.Status.REPLIED;
        } else if ("wait-reply".equals(status)) {
            findStatus = QuestionAnswerEnum.Status.CREATED;
        }
        Page<Question> questions = (student != null) ? questionService.findByStudentAndSearch(student, search, findStatus, pageIndex, PAGE_SIZE) : null;
        model.addAttribute("totalPages", questions.getTotalPages());
        model.addAttribute("documentsSaved", questions.getContent());
        model.addAttribute("studentQuestions", questions.getContent());
        // add log
        addUserLog("/my_library/my_questions/history");
        model.addAttribute("search", search);
        model.addAttribute("currentPage", pageIndex);
        model.addAttribute("status", status);
        model.addAttribute("totalItems", questions.getTotalElements());
        return "student/library/student_my-questions-and-answers";
    }


    /*
        SEARCH
     */

    @GetMapping({"/search"})
    public String getSearchResults(@RequestParam(required = false, value = "search") String search,
                                   final Model model) {
        List<EsDocument> esDocuments = esDocumentService.searchDocument(search, 0).stream().toList();
        model.addAttribute("foundDocuments", esDocuments);
        model.addAttribute("search", search);
        return "student/student_search-results";
    }

    @GetMapping("/feedbacks/add")
    public String showFeedbackForm(Model model) {
        model.addAttribute("feedback", new Feedback());
        return "student/feedback/student_feedback-add";
    }

    // Method to handle the form submission
    @PostMapping("/feedbacks/add")
    public String processFeedbackForm(@ModelAttribute("feedback") @Valid FeedbackDto feedback,
                                      BindingResult result) {
        if (result.hasErrors()) {
            return "student/feedback/student_feedback-add?error"; // Return to the form with validation errors
        }

        // Get the logged-in user (you need to implement your user authentication mechanism)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return LOGIN_REQUIRED;
        }
        String currentPrincipalName = authentication.getName();
        Account loggedInUser = accountService.findByEmail(currentPrincipalName);
        //  User loggedInUser = getCurrentLoggedInUser(); // Replace with your authentication logic

        if (loggedInUser != null) {
            feedback.setAccount(loggedInUser);
            feedback.setStatus("Pending");
            Feedback feedback1 = feedbackService.saveFeedback(new Feedback(feedback));

            if (loggedInUser.getRole().equals(AccountEnum.Role.LECTURER))
                return "redirect:/lecturer/feedbacks/add?success"; // Redirect to a success page
            else
                return "redirect:/student/feedbacks/add?success"; // Redirect to a success page
        } else {
            return "redirect:/login"; // Redirect to the login page if the user is not logged in
        }
    }

    @GetMapping({"/notifications"})
    public String getNotifications(final Model model) {
        String studentMail = getLoggedInStudentMail();
        List<NotificationResponseDto> notificationResponseDtos = notificationService.findAllByToAccount(studentMail);
        model.addAttribute("notifications", notificationResponseDtos);
        return "student/student_notifications";
    }

}
