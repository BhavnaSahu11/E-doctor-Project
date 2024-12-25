package Outpatient.example.Intership_Backend.Service;

import Outpatient.example.Intership_Backend.Entity.Feedback;
import Outpatient.example.Intership_Backend.Repository.FeedbackRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;

    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public Feedback saveFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    // Method to get all feedbacks
    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();  // Fetch all feedback records
    }
}
