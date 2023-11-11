package fpt.edu.eresourcessystem.repository;

import fpt.edu.eresourcessystem.model.Answer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("answerRepository")
public interface AnswerRepository extends MongoRepository<Answer, String> {
    Optional<Answer> findById(String id);
    List<Answer> findByDocumentId(String documentId);
    List<Answer> findByDocumentIdAndQuestionId(String documentId, String questionId);
}
