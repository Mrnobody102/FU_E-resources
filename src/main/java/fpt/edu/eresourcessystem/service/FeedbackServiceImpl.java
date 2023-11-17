package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.model.Document;
import fpt.edu.eresourcessystem.model.Feedback;
import fpt.edu.eresourcessystem.repository.DocumentRepository;
import fpt.edu.eresourcessystem.repository.FeedbackRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("feedbackService")
public class FeedbackServiceImpl implements FeedbackService {

    private FeedbackRepository feedbackRepository;
    @Override
    public List<Feedback> findAll() {
        List<Feedback> feedbacks = feedbackRepository.findAll();
        return feedbacks;
    }

}
