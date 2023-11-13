package fpt.edu.eresourcessystem.controller.restcontrollers;


import fpt.edu.eresourcessystem.dto.AnswerDto;
import fpt.edu.eresourcessystem.dto.DocumentNoteDTO;
import fpt.edu.eresourcessystem.dto.QuestionDto;
import fpt.edu.eresourcessystem.model.*;
import fpt.edu.eresourcessystem.responseDto.AnswerResponseDto;
import fpt.edu.eresourcessystem.responseDto.QuestionResponseDto;
import fpt.edu.eresourcessystem.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/student")
public class StudentRestController {
    private final StudentService studentService;
    private final DocumentService documentService;
    private final DocumentNoteService documentNoteService;

    private final QuestionService questionService;
    private final AnswerService answerService;
    public StudentRestController(StudentService studentService, DocumentService documentService, DocumentNoteService documentNoteService, QuestionService questionService, AnswerService answerService) {
        this.studentService = studentService;
        this.documentService = documentService;
        this.documentNoteService = documentNoteService;
        this.questionService = questionService;
        this.answerService = answerService;
    }

    public Student getLoggedInStudent() {
        return studentService.findAll().get(0);
    }
    @GetMapping("/documents/{documentId}/save_document")
    public String saveDocument(@PathVariable String documentId) {
        // get account authorized
        Student student = getLoggedInStudent();
        if (null != documentService.findById(documentId)) {
            boolean result = studentService.saveADoc(student.getId(), documentId);
            if (result) {
                return "saved";
            } else {
                return "unsaved";
            }

        }
        return "exception";
    }

    @GetMapping("/documents/{documentId}/unsaved_document")
    public String unsavedDoc(@PathVariable String documentId,
                             HttpServletRequest request,
                             HttpSession session) {
        // get account authorized
        Student student = getLoggedInStudent();
        if (null != documentService.findById(documentId)) {
            boolean result =  studentService.unsavedADoc(student.getId(), documentId);
            if (result) {
                return "unsaved";
            } else {
                return "saved";
            }
        }
        return "exception";
    }

    @PostMapping("/my_note/document_notes/add")
    @Transactional
    public DocumentNoteDTO addMyNote(@ModelAttribute DocumentNoteDTO documentNoteDTO){
        Student student = getLoggedInStudent();
        if(null == student){
            return null;
        }else if(null==documentNoteDTO){
            return null;
        }
        documentNoteDTO.setStudentId(student.getId());
        DocumentNote documentNote = documentNoteService.addDocumentNote(new DocumentNote(documentNoteDTO));
        if(null!= documentNote){
            return documentNoteDTO;
        }else {
            return null;
        }
    }


    @PostMapping(value = "/question/add", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    @Transactional
    public ResponseEntity<QuestionResponseDto> addQuestion(@ModelAttribute QuestionDto questionDto, @RequestParam String docId){
        Student student = getLoggedInStudent();
        Document document = documentService.findById(docId);
        if(null == student || null==questionDto || null==document){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        questionDto.setStudent(student);
        questionDto.setDocumentId(document);
        Question question = questionService.addQuestion(new Question(questionDto));
        if(null!= question){
            System.out.println(question);
            QuestionResponseDto questionResponseDto = new QuestionResponseDto(question);
            ResponseEntity<QuestionResponseDto> responseEntity = new ResponseEntity<>(questionResponseDto, HttpStatus.OK);
            return responseEntity;
        }else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/answer/add", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    @Transactional
    public ResponseEntity<AnswerResponseDto> addQuestion(@ModelAttribute AnswerDto answerDto,
                                                         @RequestParam String docId,
                                                         @RequestParam String quesId){
        Student student = getLoggedInStudent();
        Document document = documentService.findById(docId);
        Question question = questionService.findById(quesId);
        if(null == student || null == answerDto || null==document || null == question){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        answerDto.setStudent(student);
        answerDto.setQuestionId(question);
        answerDto.setDocumentId(document);
        Answer answer = answerService.addAnswer(new Answer(answerDto));
        if(null!= answer){
//            System.out.println(question);
            // update list answer of the question
            question.getAnswers().add(answer);
            questionService.updateQuestion(question);
            AnswerResponseDto answerResponseDto = new AnswerResponseDto(answer);
            ResponseEntity<AnswerResponseDto> responseEntity = new ResponseEntity<>(answerResponseDto, HttpStatus.OK);
            return responseEntity;
        }else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/answers/get/{questionId}", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<AnswerResponseDto>> getAnswerOfQuestion(@PathVariable String questionId){
        Question question = questionService.findById(questionId);
        List<Answer> answers = answerService.findByQuestion(question);
        List<AnswerResponseDto> answerResponseDtos = new ArrayList<>();
        if(null!= answers){
            for (Answer answer: answers) {
                answerResponseDtos.add(new AnswerResponseDto(answer));
            }
            ResponseEntity<List<AnswerResponseDto>> responseEntity = new ResponseEntity<>(answerResponseDtos, HttpStatus.OK);
            return responseEntity;
        }else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

}
