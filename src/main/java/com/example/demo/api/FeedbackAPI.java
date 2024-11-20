package com.example.demo.api;

import com.example.demo.entity.Feedback;
import com.example.demo.entity.Order;
import com.example.demo.model.request.FeedbackRequestDTO;
import com.example.demo.model.request.FeedbackRequestOrderDTO;
import com.example.demo.model.response.FeedbackResponse;
import com.example.demo.service.FeedbackService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@SecurityRequirement(name = "api")
@CrossOrigin("*")
public class FeedbackAPI {

    @Autowired
    FeedbackService feedbackService;

    @PostMapping("/shop")
    public ResponseEntity createFeedback(@RequestBody FeedbackRequestDTO feedbackRequestDTO) {
        Feedback feedback = feedbackService.createNewFeedback(feedbackRequestDTO);
        return ResponseEntity.ok(feedback);
    }
    @PostMapping("/order")
    public ResponseEntity createFeedbackOrder(@RequestBody FeedbackRequestOrderDTO feedbackRequestDTO) {
        Order order = feedbackService.feedbackOrder(feedbackRequestDTO);
        return ResponseEntity.ok(order);
    }

    @GetMapping
    public ResponseEntity getFeedback() {
        List<FeedbackResponse> feedbacks = feedbackService.getFeedback();
        return ResponseEntity.ok(feedbacks);
    }


}
