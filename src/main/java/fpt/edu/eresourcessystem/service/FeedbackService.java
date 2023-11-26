package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.model.Feedback;

import java.util.List;
import java.util.Optional;

public interface FeedbackService {

    List<Feedback> findAll();

    public Feedback saveFeedback(Feedback feedback) ;

     Optional<Feedback> getFeedbackById(String id) ;

     Feedback updateFeedback(String id, Feedback feedbackDetails) ;
     void deleteFeedback(String id);
}
