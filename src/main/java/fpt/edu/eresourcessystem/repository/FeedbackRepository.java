package fpt.edu.eresourcessystem.repository;

import fpt.edu.eresourcessystem.model.Document;
import fpt.edu.eresourcessystem.model.Feedback;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository("feedbackRepository")
public interface FeedbackRepository extends MongoRepository<Feedback, String> {
    @Override
    @Query("{ 'deleteFlg' : 'PRESERVED' }")
    List<Feedback> findAll();
}
