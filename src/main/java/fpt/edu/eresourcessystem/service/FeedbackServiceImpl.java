package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.model.Document;
import fpt.edu.eresourcessystem.model.Feedback;
import fpt.edu.eresourcessystem.repository.DocumentRepository;
import fpt.edu.eresourcessystem.repository.FeedbackRepository;
import org.springframework.data.elasticsearch.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("feedbackService")
public class FeedbackServiceImpl implements FeedbackService {

    private FeedbackRepository feedbackRepository;
    @Override
    public List<Feedback> findAll() {
        List<Feedback> feedbacks = feedbackRepository.findAll();
        return feedbacks;
    }

    public Feedback saveFeedback(Feedback feedback) {
        // You might want to add additional business logic here
        return feedbackRepository.save(feedback);
    }

    // Get a single feedback entry by ID
    public Optional<Feedback> getFeedbackById(String id) {
        return feedbackRepository.findById(id);
    }

    // Update feedback entry
    public Feedback updateFeedback(String id, Feedback feedbackDetails) {
        return feedbackRepository.findById(id)
                .map(feedback -> {
                    // Update the feedback properties here
                    // For example: feedback.setContent(feedbackDetails.getContent());
                    return feedbackRepository.save(feedback);
                }).orElseThrow(() -> new ResourceNotFoundException("Feedback not found with id " + id));
    }

    // Delete a feedback entry
    public void deleteFeedback(String id) {
        feedbackRepository.deleteById(id);
    }

}
