package fpt.edu.eresourcessystem.controller;

import fpt.edu.eresourcessystem.dto.FeedbackDto;
import fpt.edu.eresourcessystem.dto.Response.DocumentResponseDto;
import fpt.edu.eresourcessystem.dto.Response.QuestionResponseDto;
import fpt.edu.eresourcessystem.dto.StudentNoteDto;
import fpt.edu.eresourcessystem.dto.UserLogDto;
import fpt.edu.eresourcessystem.enums.AccountEnum;
import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.enums.CourseEnum;
import fpt.edu.eresourcessystem.enums.DocumentEnum;
import fpt.edu.eresourcessystem.model.*;
import fpt.edu.eresourcessystem.model.elasticsearch.EsDocument;
import fpt.edu.eresourcessystem.service.*;
import fpt.edu.eresourcessystem.utils.CommonUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;

import static fpt.edu.eresourcessystem.constants.Constants.PAGE_SIZE;
import static fpt.edu.eresourcessystem.constants.UrlConstants.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/student")
public class StudentController {
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
    private final FeedbackService feedbackService;

    private Student getLoggedInStudent() {
        String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(loggedInEmail);
        if (null == loggedInEmail || "anonymousUser".equals(loggedInEmail)) {
            return null;
        }
        Account loggedInAccount = accountService.findByEmail(loggedInEmail);
        if (loggedInAccount != null) {
            return studentService.findByAccountId(loggedInAccount.getId());
        } else return null;
    }

    /*
        HOME
     */
    private UserLog addUserLog(String url) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Account loggedInUser = accountService.findByEmail(currentPrincipalName);
        UserLog userLog = new UserLog(new UserLogDto(url,loggedInUser.getRole()));
        userLog = userLogService.addUserLog(userLog);
        return userLog;
    }

    @GetMapping({"", "/home"})
    public String getStudentHome(@ModelAttribute Account account, final Model model) {
        Student student = getLoggedInStudent();
        if (null == student) {
            return LOGIN_REQUIRED;
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
        //
//        List<DocumentResponseDto> documentResponseDtoList
//                = documentService.findAllDocumentsByCourseAndResourceType(courseId,"65609a30382c4a70ca46f263");
//        System.out.println("student -controller - "+documentResponseDtoList.size());
//        for (int i = 0; i < documentResponseDtoList.size(); i++) {
//            System.out.println(documentResponseDtoList.get(i).toString());
//        }
//        model.addAttribute("documentResponseDtoList",documentResponseDtoList);
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
    public String viewDocumentDetail(@PathVariable String docId, final Model model) throws IOException {
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
            if(document.getCloudFileLink() != null) {
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
        if (null != document.getTopic()) {
            List<DocumentResponseDto> relevantDocuments = documentService
                    .findRelevantDocument(document.getTopic().getId(), docId);
            if (null != relevantDocuments && relevantDocuments.size() > 0) {
                model.addAttribute("relevantDocuments", relevantDocuments);
            } else {
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
        List<String> savedCourses = student != null ? student.getSavedCourses() : null;
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
        List<String> savedDocuments = student != null ? student.getSavedDocuments() : null;
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
        if(null == studentNoteId){
            return "exception/404";

        }else {
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
            addUserLog("/student/my_note/student_notes/" + studentNote.getId()+ "?success");
            return "redirect:/student/my_note/student_notes/"+ studentNote.getId() + SUCCESS_PARAM;
        } else {
            return "redirect:/student/my_note/student_notes/"+ studentNote.getId() + "?error";
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
            addUserLog("/student/my_note/student_notes/delete/" + studentNoteId+ "?success");
            return "redirect:/student/my_note/student_notes/add" + SUCCESS_PARAM;
        } else {
            return "redirect:/student/my_note/student_notes/" + studentNoteId + "?error";
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
//            page = courseService.findByCourseNameLike(search, pageIndex, PAGE_SIZE);
//        } else {
//            page = courseService.findByCourseCodeLike(search, pageIndex, PAGE_SIZE);
//        }

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
            return "student/feedback/student_feedback-add"; // Return to the form with validation errors
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

            return "redirect:/student/feedbacks/add"; // Redirect to a success page
        } else {
            return "redirect:/login"; // Redirect to the login page if the user is not logged in
        }
    }

    @GetMapping({"/chat"})
    public String goHomePage() {
        return "student/test_notification";
    }
}
