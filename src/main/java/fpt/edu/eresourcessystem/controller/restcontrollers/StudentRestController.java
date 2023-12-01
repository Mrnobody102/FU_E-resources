package fpt.edu.eresourcessystem.controller.restcontrollers;


import fpt.edu.eresourcessystem.dto.AnswerDto;
import fpt.edu.eresourcessystem.dto.QuestionDto;
import fpt.edu.eresourcessystem.dto.Response.DocumentResponseDto;
import fpt.edu.eresourcessystem.dto.UserLogDto;
import fpt.edu.eresourcessystem.enums.QuestionAnswerEnum;
import fpt.edu.eresourcessystem.model.*;
import fpt.edu.eresourcessystem.dto.Response.AnswerResponseDto;
import fpt.edu.eresourcessystem.dto.Response.QuestionResponseDto;
import fpt.edu.eresourcessystem.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/student")
public class StudentRestController {
    private final StudentService studentService;
    private final DocumentService documentService;
    private final DocumentNoteService documentNoteService;

    private final CourseService courseService;

    private final QuestionService questionService;
    private final AnswerService answerService;

    private final UserLogService userLogService;
    private final AccountService accountService;

    private final TopicService topicService;
    private final LecturerService lecturerService;

    public StudentRestController(StudentService studentService, DocumentService documentService, DocumentNoteService documentNoteService, CourseService courseService, QuestionService questionService, AnswerService answerService, UserLogService userLogService, AccountService accountService, TopicService topicService, LecturerService lecturerService) {
        this.studentService = studentService;
        this.documentService = documentService;
        this.documentNoteService = documentNoteService;
        this.courseService = courseService;
        this.questionService = questionService;
        this.answerService = answerService;
        this.userLogService = userLogService;
        this.accountService = accountService;
        this.topicService = topicService;
        this.lecturerService = lecturerService;
    }

    private UserLog addUserLog(String url) {
        UserLog userLog = new UserLog(new UserLogDto(url));
        userLog = userLogService.addUserLog(userLog);
        return userLog;
    }

    public Student getLoggedInStudent() {
        String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Account loggedInAccount = accountService.findByEmail(loggedInEmail);
        Student loggedInStudent = studentService.findByAccountId(loggedInAccount.getId());
        return loggedInStudent;
    }
    @PostMapping("/documents/{documentId}/save_document")
    @Transactional
    public String saveDocument(@PathVariable String documentId) {
        // get account authorized
        Student student = getLoggedInStudent();
        if (null != documentService.findById(documentId)) {
            boolean result = studentService.saveADoc(student.getId(), documentId);
            if (result) {
                // add log
                addUserLog("/api/student/documents/" + documentId + "/save_document");
                return "saved";
            } else {
                return "unsaved";
            }

        }
        return "exception";
    }

    @PostMapping("/documents/{documentId}/unsaved_document")
    @Transactional
    public String unsavedDoc(@PathVariable String documentId,
                             HttpServletRequest request,
                             HttpSession session) {
        // get account authorized
        Student student = getLoggedInStudent();
        if (null != documentService.findById(documentId)) {
            boolean result = studentService.unsavedADoc(student.getId(), documentId);
            if (result) {
                // add log
                addUserLog("/api/student/documents/" + documentId + "/unsaved_document");
                return "unsaved";
            } else {
                return "saved";
            }
        }
        return "exception";
    }

    @PostMapping(value = "/question/add", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    @Transactional
    public ResponseEntity<QuestionResponseDto> addQuestion(@ModelAttribute QuestionDto questionDTO, @RequestParam String docId) {
        Student student = getLoggedInStudent();
        Document document = documentService.findById(docId);
        if (null == student || null == questionDTO || null == document) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        questionDTO.setStudent(student);
        questionDTO.setLecturer(document.getCreatedBy());
        questionDTO.setDocumentId(document);
        Question question = questionService.addQuestion(new Question(questionDTO));
        if (null != question) {
//            System.out.println(question);
            // add log
            addUserLog("/api/student/question/add" + question.getId());
            QuestionResponseDto questionResponseDTO = new QuestionResponseDto(question);
            ResponseEntity<QuestionResponseDto> responseEntity = new ResponseEntity<>(questionResponseDTO, HttpStatus.OK);
            return responseEntity;
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/answer/add", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    @Transactional
    public ResponseEntity<AnswerResponseDto> addQuestion(@ModelAttribute AnswerDto answerDTO,
                                                         @RequestParam String docId,
                                                         @RequestParam String quesId) {
        Student student = getLoggedInStudent();
        Document document = documentService.findById(docId);
        Question question = questionService.findById(quesId);
        if (null == student || null == answerDTO || null == document || null == question) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        answerDTO.setStudent(student);
        answerDTO.setQuestionId(question);
        answerDTO.setDocumentId(document);
        Answer answer = answerService.addAnswer(new Answer(answerDTO));
        if (null != answer) {
//            System.out.println(question);
            // update list answer of the question
            question.getAnswers().add(answer);
            questionService.updateQuestion(question);
            // add log
            addUserLog("/api/student/answer/add/" + answer.getId());
            AnswerResponseDto answerResponseDTO = new AnswerResponseDto(answer);
            ResponseEntity<AnswerResponseDto> responseEntity = new ResponseEntity<>(answerResponseDTO, HttpStatus.OK);
            return responseEntity;
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/answers/get/{questionId}", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<AnswerResponseDto>> getAnswerOfQuestion(@PathVariable String questionId) {
        Question question = questionService.findById(questionId);
        if(null == question){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        List<Answer> answers = answerService.findByQuestion(question);
        if (null != answers) {
//            //update question status
            question.setStatus(QuestionAnswerEnum.Status.READ);
            questionService.updateQuestion(question);
            // add log
            addUserLog("/api/student/answers/get/" + questionId);
            // change to response object
            List<AnswerResponseDto> answerResponseDtos = answers.stream()
                    .map(entity -> new AnswerResponseDto(entity))
                    .collect(Collectors.toList());
            ResponseEntity<List<AnswerResponseDto>> responseEntity = new ResponseEntity<>(answerResponseDtos, HttpStatus.OK);
            return responseEntity;
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/courses/{courseId}/save_course")
    @Transactional
    public String saveCourse(@PathVariable String courseId) {
        // get account authorized
        Student student = getLoggedInStudent();
        if (null != courseService.findByCourseId(courseId)) {
            boolean result = studentService.saveACourse(student.getId(), courseId);
            if (result) {
                // add log
                addUserLog("/student/course/" + courseId + "/save_course");
                return "saved";
            } else {
                return "unsaved";
            }

        }
        return "exception";
    }

    @PostMapping("/courses/{courseId}/unsaved_course")
    @Transactional
    public String unsavedCourse(@PathVariable String courseId) {
        // get account authorized
        Student student = getLoggedInStudent();
        if (null != courseService.findByCourseId(courseId)) {
            boolean result = studentService.unsavedACourse(student.getId(), courseId);
            if (result) {
                // add log
                addUserLog("/api/student/course/" + courseId + "/unsaved_course");
                return "unsaved";
            } else {
                return "saved";
            }
        }
        return "exception";
    }

    @PostMapping(value = "/document_note/add/{documentId}", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    @Transactional
    public ResponseEntity<DocumentNote> addNewNote(@RequestParam String noteContent,
                                                   @PathVariable String documentId) {
        Student student = getLoggedInStudent();
        System.out.println(noteContent);
        Document document = documentService.findById(documentId);
        if (null == student || null == noteContent || "".equals(noteContent.trim()) || null == document) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        DocumentNote documentNote = new DocumentNote();
        documentNote.setStudentId(student.getId());
        documentNote.setDocId(documentId);
        documentNote.setNoteContent(noteContent);
        DocumentNote result = documentNoteService.addDocumentNote(documentNote);
        if (null != result) {
            // add log
            addUserLog("/api/student/document_note/add/" + documentId);
            System.out.println(result);
            ResponseEntity<DocumentNote> responseEntity = new ResponseEntity<>(result, HttpStatus.OK);
            return responseEntity;
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/document_note/update/{documentId}", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    @Transactional
    public ResponseEntity<DocumentNote> updateNoteDocument(@RequestParam String noteContent,
                                                           @PathVariable String documentId) {
        Student student = getLoggedInStudent();
        System.out.println(noteContent);
        Document document = documentService.findById(documentId);
        if (null == student || null == noteContent || "".equals(noteContent.trim()) || null == document) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        DocumentNote documentNote = documentNoteService.findByDocIdAndStudentId(documentId, student.getId());
        documentNote.setNoteContent(noteContent);
        DocumentNote result = documentNoteService.updateDocumentNote(documentNote);
        if (null != result) {
            // add log
            addUserLog("/api/student/document_note/update/" + documentId);
            ResponseEntity<DocumentNote> responseEntity = new ResponseEntity<>(result, HttpStatus.OK);
            return responseEntity;
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/documents/get_by_topic/{topicId}", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<DocumentResponseDto>> getDocumentOfTopic(@PathVariable String topicId) {
        System.out.println(topicId);
        Topic topic = topicService.findById(topicId);
        if (null == topic) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        List<DocumentResponseDto> documents = topicService.findByTopic(topicId);
        if (null != documents) {
            // add log
            addUserLog("/api/student/documents/get_by_topic/" + topicId);
            ResponseEntity<List<DocumentResponseDto>> responseEntity = new ResponseEntity<>(documents, HttpStatus.OK);
            System.out.println(documents);
            return responseEntity;
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/my_question/new_question", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<QuestionResponseDto>> getWaitQuestion() {
        Student student = getLoggedInStudent();
        if (null == student) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } else {
            // add log
            addUserLog("/api/student/my_question/new_question/");
            List<QuestionResponseDto> questionResponseDtos = questionService.findWaitReplyQuestionForStudent(student.getId());
            ResponseEntity<List<QuestionResponseDto>> responseEntity = new ResponseEntity<>(questionResponseDtos, HttpStatus.OK);
            return responseEntity;
        }

    }

    @PostMapping(value = "/my_question/{questionId}/update", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    @Transactional
    public ResponseEntity<QuestionResponseDto> updateQuestion(@PathVariable String questionId,
                                                              @RequestParam String questionContent) {
        System.out.println(questionContent);
        Question question = questionService.findById(questionId);
        if (null == question) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } else {
            if (null != questionContent && "" != questionContent.trim()) {
                question.setContent(questionContent);
                question = questionService.updateQuestion(question);
                System.out.println(question.getContent());
                // add log
                addUserLog("/api/student/my_question/"+questionId+"/update");
                ResponseEntity<QuestionResponseDto> responseEntity = new ResponseEntity<>(new QuestionResponseDto(question), HttpStatus.OK);
                return responseEntity;
            } else {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }

        }

    }

    @PostMapping(value = "/my_question/{questionId}/delete")
    @Transactional
    public ResponseEntity<String> deleteQuestion(@PathVariable String questionId) {
        Question question = questionService.findById(questionId);
        if (null == question) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } else {
            boolean check = questionService.deleteQuestion(question);
            if (check) {
                // add log
                addUserLog("/api/student/my_question/"+questionId+"/delete");
                return new ResponseEntity("Delete successfully.", HttpStatus.OK);
            } else {
                return new ResponseEntity("Delete failed.", HttpStatus.NOT_FOUND);
            }

        }

    }


    @GetMapping(value = "/my_question/new_replies", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<QuestionResponseDto>> getNewReplyQuestion() {
        Student student = getLoggedInStudent();
        if (null == student) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } else {
            // add log
            addUserLog("/api/student/my_question/new_replies");
            List<QuestionResponseDto> questionResponseDtos = questionService.findNewReplyQuestionStudent(student.getId());
            ResponseEntity<List<QuestionResponseDto>> responseEntity = new ResponseEntity<>(questionResponseDtos, HttpStatus.OK);
            return responseEntity;
        }

    }

    @PostMapping(value = "/my_question/replies/{answerId}/update", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    @Transactional
    public ResponseEntity<AnswerResponseDto> updateReply(@PathVariable String answerId, @RequestParam String answerContent) {
        Answer answer = answerService.findById(answerId);
        if (null == answer) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } else {
            if (null != answerContent && "" != answerContent.trim()) {
                answer.setAnswer(answerContent);
                answer.setStatus(QuestionAnswerEnum.Status.READ);
                answer = answerService.updateAnswer(answer);
                // add log
                addUserLog("/api/student/my_question/replies/"+answerId+"/update");
                ResponseEntity<AnswerResponseDto> responseEntity = new ResponseEntity<>(new AnswerResponseDto(answer), HttpStatus.OK);
                return responseEntity;
            } else {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        }
    }

    @PostMapping(value = "/my_question/replies/{answerId}/delete", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    @Transactional
    public ResponseEntity<AnswerResponseDto> deleteReply(@PathVariable String answerId) {
        Answer answer = answerService.findById(answerId);
        if (null == answer) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } else {
            boolean check = answerService.deleteAnswer(answer);
            if (check) {
                // add log
                addUserLog("/api/student/my_question/replies/"+answerId+"/delete");
                return new ResponseEntity(HttpStatus.OK);
            } else {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        }
    }

}
