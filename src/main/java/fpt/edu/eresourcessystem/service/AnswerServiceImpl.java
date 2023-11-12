package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.dto.AnswerDto;
import fpt.edu.eresourcessystem.dto.QuestionDto;
import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.model.*;
import fpt.edu.eresourcessystem.repository.AnswerRepository;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.util.List;
import java.util.Optional;

@Service("answerService")
public class AnswerServiceImpl implements AnswerService{
    private final AnswerRepository answerRepository;

    public AnswerServiceImpl(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }


    @Override
    public List<Answer> findByDocId(Document document) {
        List<Answer> answers = answerRepository.findByDocumentId(document);
        return answers;
    }

    @Override
    public List<Answer> findByDocIdAndQuestionId(Document document, Question question) {
        List<Answer> answers = answerRepository.findByDocumentIdAndQuestionId(document, question);
        return answers;
    }

    @Override
    public Answer addAnswer(Answer answer) {
        if (null == answer.getId()) {
            Answer result = answerRepository.save(answer);
            return result;
        } else if (!answerRepository.findById(answer.getId().trim()).isPresent()) {
            Answer result = answerRepository.save(answer);
            return result;
        }
        return null;
    }

    @Override
    public Answer updateAnswer(Answer answer) {
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
