package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.model.*;
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
    public Answer findById(String answerId) {
        Optional<Answer> answer = answerRepository.findById(answerId);
        return answer.isPresent() ? answer.get() : null;
    }

    @Override
    public List<Answer> findByDocId(Document document) {
        List<Answer> answers = answerRepository
                .findByDocumentIdAndDeleteFlg(document, CommonEnum.DeleteFlg.PRESERVED);
        return answers;
    }

    @Override
    public List<Answer> findByDocIdAndQuestionId(Document document, Question question) {
        List<Answer> answers = answerRepository
                .findByDocumentIdAndQuestionIdAndDeleteFlg(document,
                        question, CommonEnum.DeleteFlg.PRESERVED);
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
        Answer savedAnswer = answerRepository
                .findByIdAndDeleteFlg(answer.getId(), CommonEnum.DeleteFlg.PRESERVED);
        if(null != savedAnswer){
            Answer result =  answerRepository.save(answer);
            return result;
        }
        return null;
    }

    @Override
    public boolean deleteAnswer(Answer answer) {
        Answer check = answerRepository
                .findByIdAndDeleteFlg(answer.getId(), CommonEnum.DeleteFlg.PRESERVED);
        if(null != check){
            check.setDeleteFlg(CommonEnum.DeleteFlg.DELETED);
            answerRepository.save(check);
            return true;
        }
        return false;
    }
    @Override
    public List<Answer> findByQuestion(Question question) {
        List<Answer> answers = answerRepository
                .findByQuestionAndDeleteFlg(question, CommonEnum.DeleteFlg.PRESERVED);
        return answers;
    }

    @Override
    public List<Answer> findByStudentAnsQuestion(Student student, Question question) {
        List<Answer> answers = answerRepository
                .findByStudentAndQuestionAndDeleteFlg(student, question, CommonEnum.DeleteFlg.PRESERVED);
        return answers;
    }
}
