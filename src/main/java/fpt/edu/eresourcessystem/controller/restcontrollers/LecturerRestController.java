package fpt.edu.eresourcessystem.controller.restcontrollers;


import fpt.edu.eresourcessystem.dto.AnswerDTO;
import fpt.edu.eresourcessystem.model.Answer;
import fpt.edu.eresourcessystem.model.Document;
import fpt.edu.eresourcessystem.model.Lecturer;
import fpt.edu.eresourcessystem.model.Question;
import fpt.edu.eresourcessystem.dto.Response.AnswerResponseDTO;
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
    public ResponseEntity<AnswerResponseDTO> addQuestion(@ModelAttribute AnswerDTO answerDTO,
                                                         @RequestParam String docId,
                                                         @RequestParam String quesId){
        Lecturer lecturer = getLoggedInLecturer();
        Document document = documentService.findById(docId);
        Question question = questionService.findById(quesId);
        if(null == lecturer || null == answerDTO || null==document || null == question){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        answerDTO.setLecturer(lecturer);
        answerDTO.setQuestionId(question);
        answerDTO.setDocumentId(document);
        Answer answer = answerService.addAnswer(new Answer(answerDTO));
        if(null!= answer){
            // update list answer of the question
            question.getAnswers().add(answer);
            questionService.updateQuestion(question);
            AnswerResponseDTO answerResponseDTO = new AnswerResponseDTO(answer);
            ResponseEntity<AnswerResponseDTO> responseEntity = new ResponseEntity<>(answerResponseDTO, HttpStatus.OK);
            return responseEntity;
        }else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/answers/get/{questionId}", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<AnswerResponseDTO>> getAnswerOfQuestion(@PathVariable String questionId){
        Question question = questionService.findById(questionId);
        List<Answer> answers = answerService.findByQuestion(question);
        List<AnswerResponseDTO> answerResponseDTOs = new ArrayList<>();
        if(null!= answers){
            for (Answer answer: answers) {
                answerResponseDTOs.add(new AnswerResponseDTO(answer));
            }
            ResponseEntity<List<AnswerResponseDTO>> responseEntity = new ResponseEntity<>(answerResponseDTOs, HttpStatus.OK);
            return responseEntity;
        }else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }


}
