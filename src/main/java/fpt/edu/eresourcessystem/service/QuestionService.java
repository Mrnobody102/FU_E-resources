package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.dto.QuestionDto;
import fpt.edu.eresourcessystem.model.Document;
import fpt.edu.eresourcessystem.model.Question;
import fpt.edu.eresourcessystem.model.Student;

import javax.print.Doc;
import java.util.List;

public interface QuestionService {
    List<Question> findByDocId(Document document);
    List<Question> findByDocIdAndStudentId(Document document, Student student);
    List<Question> findByStudent(Student student);
    Question findById(String quesId);
    Question addQuestion(Question question);

    Question updateQuestion(Question question);
    boolean deleteQuestion(Question question);
}
