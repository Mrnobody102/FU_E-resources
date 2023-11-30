package fpt.edu.eresourcessystem.controller.restcontrollers;


import fpt.edu.eresourcessystem.dto.AnswerDto;
import fpt.edu.eresourcessystem.dto.Response.QuestionResponseDto;
import fpt.edu.eresourcessystem.enums.QuestionAnswerEnum;
import fpt.edu.eresourcessystem.model.*;
import fpt.edu.eresourcessystem.dto.Response.AnswerResponseDto;
import fpt.edu.eresourcessystem.service.AnswerService;
import fpt.edu.eresourcessystem.service.DocumentService;
import fpt.edu.eresourcessystem.service.LecturerService;
import fpt.edu.eresourcessystem.service.QuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/lecturer")
public class LecturerRestController {
    private final LecturerService lecturerService;
    private final DocumentService documentService;
    private final AnswerService answerService;
    private final QuestionService questionService;

    public LecturerRestController(LecturerService lecturerService, DocumentService documentService, AnswerService answerService, QuestionService questionService) {
        this.lecturerService = lecturerService;
        this.documentService = documentService;
        this.answerService = answerService;
        this.questionService = questionService;
    }

    public Lecturer getLoggedInLecturer() {
        return lecturerService.findAll().get(0);
    }

    @PostMapping(value = "/answer/add", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    @Transactional
    public ResponseEntity<AnswerResponseDto> addQuestion(@ModelAttribute AnswerDto answerDTO,
                                                         @RequestParam String docId,
                                                         @RequestParam String quesId){
        Lecturer lecturer = getLoggedInLecturer();
        Document document = documentService.findById(docId);
        Question question = questionService.findById(quesId);
        if(null == lecturer || null == answerDTO || null==document || null == question){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        answerDTO.setLecturer(lecturer);
        answerDTO.setQuestionId(question);
        answerDTO.setDocumentId(document);
        Answer answer = answerService.addAnswer(new Answer(answerDTO));
        if(null!= answer){
            // update list answer of the question
            question.getAnswers().add(answer);
            question.setStatus(QuestionAnswerEnum.Status.REPLIED);
            questionService.updateQuestion(question);
            AnswerResponseDto answerResponseDTO = new AnswerResponseDto(answer);
            return new ResponseEntity<>(answerResponseDTO, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
            return new ResponseEntity<>(answerResponseDtos, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping(value = "/my_question/new_question", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<QuestionResponseDto>> getNewReplyQuestion(){
        Lecturer lecturer = getLoggedInLecturer();
        if(null == lecturer){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }else{
            List<QuestionResponseDto> questionResponseDto = questionService.findNewQuestionForLecturer(lecturer.getAccount().getEmail());
            ResponseEntity<List<QuestionResponseDto>> responseEntity = new ResponseEntity<>(questionResponseDto, HttpStatus.OK);
            return responseEntity;
        }

    }

    @GetMapping(value = "/my_question/replied_question", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<QuestionResponseDto>> getRepliedQuestion(){
        Lecturer lecturer = getLoggedInLecturer();
        if(null == lecturer){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }else{
            List<QuestionResponseDto> questionResponseDto = questionService.findRepliedQuestionForLecturer(lecturer.getAccount().getEmail());
            ResponseEntity<List<QuestionResponseDto>> responseEntity = new ResponseEntity<>(questionResponseDto, HttpStatus.OK);
            return responseEntity;
        }

    }


}
