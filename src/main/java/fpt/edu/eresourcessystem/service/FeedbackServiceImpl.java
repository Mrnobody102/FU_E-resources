package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.model.Feedback;
import fpt.edu.eresourcessystem.repository.FeedbackRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
//import org.springframework.data.elasticsearch.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service("feedbackService")
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;

    public FeedbackServiceImpl(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

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


    // Delete a feedback entry
    public void deleteFeedback(String id) {
        feedbackRepository.deleteById(id);
    }


    @Override
    public Page<Feedback> findAll(PageRequest pageRequest) {
        return feedbackRepository.findAll(pageRequest);
    }

    @Override
    public Page<Feedback> findAllByDateRange(Date minDate, Date maxDate, Pageable pageable) {
        if (minDate != null && maxDate != null) {
            return feedbackRepository.findAllByCreatedDateBetween(minDate, maxDate, pageable);
        } else if (minDate != null) {
            return feedbackRepository.findAllByCreatedDateGreaterThanEqual(minDate, pageable);
        } else if (maxDate != null) {
            return feedbackRepository.findAllByCreatedDateLessThanEqual(maxDate, pageable);
        } else {
            return feedbackRepository.findAll(pageable);
        }
    }

    @Transactional
    @Override
    public boolean softDelete(String feedbackId) {
        return feedbackRepository.findById(feedbackId).map(feedback -> {
            feedback.setDeleteFlg(CommonEnum.DeleteFlg.DELETED); // Assuming DELETED is a constant in DeleteFlg enum representing the deleted state
            feedbackRepository.save(feedback);
            return true;
        }).orElse(false); // Return false if feedback not found
    }


    public void updateFeedbackStatus(String feedbackId, String status) {
        Optional<Feedback> feedbackOpt = feedbackRepository.findById((feedbackId));
        if (feedbackOpt.isPresent()) {
            Feedback feedback = feedbackOpt.get();
            feedback.setStatus(status);
            feedbackRepository.save(feedback);
        } else {
            // Handle the case where feedback is not found
//            throw new EntityNotFoundException("Feedback not found with ID: " + feedbackId);
        }
    }

}
