package fpt.edu.eresourcessystem.repository;

import fpt.edu.eresourcessystem.model.Question;
import fpt.edu.eresourcessystem.model.Topic;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("questionRepository")
public interface QuestionRepository extends MongoRepository<Question, String> {
    Optional<Question> findById(String id);
    List<Question> findByDocumentId(String documentId);
    List<Question> findByDocumentIdAndStudent(String documentId,String studentId);
}
