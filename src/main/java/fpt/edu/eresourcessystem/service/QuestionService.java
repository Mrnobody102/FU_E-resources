package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.dto.QuestionDto;
import fpt.edu.eresourcessystem.model.Question;
import fpt.edu.eresourcessystem.model.Student;

import java.util.List;

public interface QuestionService {
    List<Question> findByDocId(String docId);
    List<Question> findByDocIdAndStudentId(String docId, String studentId);

    Question addQuestion(QuestionDto questionDto);

    Question updateQuestion(QuestionDto questionDto);
    boolean deleteQuestion(Question question);
}
