package fpt.edu.eresourcessystem.repository;

import fpt.edu.eresourcessystem.model.Document;
import fpt.edu.eresourcessystem.model.Feedback;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface FeedbackRepository extends MongoRepository<Feedback, String> {
    @Override
    @Query("{ 'deleteFlg' : 'PRESERVED' }")
    List<Feedback> findAll();
}
