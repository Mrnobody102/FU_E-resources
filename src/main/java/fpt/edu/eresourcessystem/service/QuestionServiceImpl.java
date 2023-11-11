package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.dto.QuestionDto;
import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.model.*;
import fpt.edu.eresourcessystem.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("questionService")
public class QuestionServiceImpl implements QuestionService{
    private final QuestionRepository questionRepository;
    private final AnswerService answerService;

    public QuestionServiceImpl(QuestionRepository questionRepository, AnswerService answerService) {
        this.questionRepository = questionRepository;
        this.answerService = answerService;
    }

    @Override
    public List<Question> findByDocId(String docId) {
        List<Question> questions = questionRepository.findByDocumentId(docId);
        return questions;
    }

    @Override
    public List<Question> findByDocIdAndStudentId(String docId, String studentId) {
        List<Question> questions = questionRepository.findByDocumentIdAndStudent(docId,studentId);
        return questions;
    }

    @Override
    public Question addQuestion(QuestionDto questionDto) {
        Question question = new Question(questionDto);
        if(null!=question && null==question.getId()){
            if(null!=questionRepository.findById(question.getId())){
                return null;
            }else {
                Question result = questionRepository.save(question);
                return result;
            }
        }
        return null;
    }

    @Override
    public Question updateQuestion(QuestionDto questionDto) {
        Question question = new Question(questionDto);
        Optional<Question> savedQuestion = questionRepository.findById(questionDto.getId());
        if(savedQuestion.isPresent()){
            Question result =  questionRepository.save(question);
            return result;
        }
        return null;
    }

    @Override
    public boolean deleteQuestion(Question question) {
        Optional<Question> check = questionRepository.findById(question.getId());
        if(check.isPresent()){
            // Soft delete topic first
            for(Answer answer:question.getAnswers()){
                answerService.deleteAnswer(answer);
            }

            // SOFT DELETE Course
            question.setDeleteFlg(CommonEnum.DeleteFlg.DELETED);
            questionRepository.save(question);
            return true;
        }
        return false;
    }
}
