package Outpatient.example.Intership_Backend.Controller;

import Outpatient.example.Intership_Backend.Entity.Feedback;
import Outpatient.example.Intership_Backend.Service.FeedbackService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {
    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping("/add")
    public ResponseEntity<Feedback> addFeedback(@RequestBody Feedback feedback) {
        Feedback savedFeedback = feedbackService.saveFeedback(feedback);
        return ResponseEntity.ok(savedFeedback);
    }
    @GetMapping("/get-all-feedbacks")
    public List<Feedback> getAllFeedbacks() {
        return feedbackService.getAllFeedbacks();
    }

}
