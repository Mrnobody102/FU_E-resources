package fpt.edu.eresourcessystem.repository;

import fpt.edu.eresourcessystem.model.Document;
import fpt.edu.eresourcessystem.model.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
@Repository("feedbackRepository")
public interface FeedbackRepository extends MongoRepository<Feedback, String> {
    @Override
    @Query("{ 'deleteFlg' : 'PRESERVED' }")
    List<Feedback> findAll();


    @Query("{'createdDate': {$gte: ?0, $lte: ?1}}")
    Page<Feedback> findAllByCreatedDateBetween(Date start, Date end, Pageable pageable);

    Page<Feedback> findAllByCreatedDateGreaterThanEqual(Date start, Pageable pageable);

    Page<Feedback> findAllByCreatedDateLessThanEqual(Date end, Pageable pageable);
}
