package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.dto.AnswerDto;
import fpt.edu.eresourcessystem.dto.QuestionDto;
import fpt.edu.eresourcessystem.model.Answer;
import fpt.edu.eresourcessystem.model.Question;

import java.util.List;

public interface AnswerService {
    List<Answer> findByDocId(String docId);
    List<Answer> findByDocIdAndQuestionId(String docId, String questionId);

    Answer addAnswer(AnswerDto answerDto);

    Answer updateAnswer(AnswerDto answerDto);
    boolean deleteAnswer(Answer answer);
}
