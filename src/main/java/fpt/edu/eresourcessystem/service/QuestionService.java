package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.dto.Response.QuestionResponseDto;
import fpt.edu.eresourcessystem.model.Document;
import fpt.edu.eresourcessystem.model.Question;
import fpt.edu.eresourcessystem.model.Student;

import java.util.List;

public interface QuestionService {
    List<Question> findByDocId(Document document);
    List<Question> findByDocIdAndStudentId(Document document, Student student);
    List<QuestionResponseDto> findWaitReplyQuestion(String studentId);
    List<QuestionResponseDto> findNewReplyQuestion(String studentId);
    List<Question> findByStudent(Student student);
    Question findById(String quesId);
    Question addQuestion(Question question);

    Question updateQuestion(Question question);
    boolean deleteQuestion(Question question);
}
