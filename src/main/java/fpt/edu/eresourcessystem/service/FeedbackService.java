package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.model.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface FeedbackService {

    List<Feedback> findAll();

    public Feedback saveFeedback(Feedback feedback) ;

     Optional<Feedback> getFeedbackById(String id) ;

     Feedback updateFeedback(String id, Feedback feedbackDetails) ;
     void deleteFeedback(String id);

    Page<Feedback> findAll(PageRequest pageRequest);

    Page<Feedback> findAllByDateRange(Date minDate, Date maxDate, Pageable pageable);
}
