package fpt.edu.eresourcessystem.repository;

import fpt.edu.eresourcessystem.model.Document;
import fpt.edu.eresourcessystem.model.Lecturer;
import fpt.edu.eresourcessystem.model.Question;
import fpt.edu.eresourcessystem.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("questionRepository")
public interface QuestionRepository extends MongoRepository<Question, String> {
    List<Question> findAll();
    Optional<Question> findById(String id);
    List<Question> findByDocumentId(Document document);
    List<Question> findByDocumentIdAndStudent(Document document, Student student);
    List<Question> findByStudent(Student student);

}
