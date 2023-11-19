package fpt.edu.eresourcessystem.repository;

import fpt.edu.eresourcessystem.model.Answer;
import fpt.edu.eresourcessystem.model.Document;
import fpt.edu.eresourcessystem.model.Question;
import fpt.edu.eresourcessystem.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("answerRepository")
public interface AnswerRepository extends MongoRepository<Answer, String> {
    Optional<Answer> findById(String id);
    List<Answer> findByDocumentId(Document document);
    List<Answer> findByDocumentIdAndQuestionId(Document document, Question question);

    List<Answer> findByQuestion(Question question);
    List<Answer> findByStudentAndQuestion(Student student, Question question);
}
