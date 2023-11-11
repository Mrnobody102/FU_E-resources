package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.dto.AnswerDto;
import fpt.edu.eresourcessystem.dto.QuestionDto;
import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.model.Admin;
import fpt.edu.eresourcessystem.model.Answer;
import fpt.edu.eresourcessystem.model.Course;
import fpt.edu.eresourcessystem.model.Question;
import fpt.edu.eresourcessystem.repository.AnswerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("answerService")
public class AnswerServiceImpl implements AnswerService{
    private final AnswerRepository answerRepository;

    public AnswerServiceImpl(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }


    @Override
    public List<Answer> findByDocId(String docId) {
        List<Answer> answers = answerRepository.findByDocumentId(docId);
        return answers;
    }

    @Override
    public List<Answer> findByDocIdAndQuestionId(String docId, String questionId) {
        List<Answer> answers = answerRepository.findByDocumentIdAndQuestionId(docId, questionId);
        return answers;
    }

    @Override
    public Answer addAnswer(AnswerDto answerDto) {
        Answer answer = new Answer(answerDto);
        if(null!=answer && null==answer.getId()){
            if(null!=answerRepository.findById(answer.getId())){
                return null;
            }else {
                Answer result = answerRepository.save(answer);
                return result;
            }
        }
        return null;
    }

    @Override
    public Answer updateAnswer(AnswerDto answerDto) {
        Answer answer = new Answer(answerDto);
        Optional<Answer> savedAnswer = answerRepository.findById(answer.getId());
        if(savedAnswer.isPresent()){
            Answer result =  answerRepository.save(answer);
            return result;
        }
        return null;
    }

    @Override
    public boolean deleteAnswer(Answer answer) {
        Optional<Answer> check = answerRepository.findById(answer.getId());
        if(check.isPresent()){
            Answer deleteAnswer = check.get();
            deleteAnswer.setDeleteFlg(CommonEnum.DeleteFlg.DELETED);
            answerRepository.save(deleteAnswer);
            return true;
        }
        return false;
    }
}
