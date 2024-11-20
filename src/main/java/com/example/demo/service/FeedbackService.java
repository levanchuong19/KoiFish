package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.Feedback;
import com.example.demo.entity.Order;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.model.request.FeedbackRequestDTO;
import com.example.demo.model.request.FeedbackRequestOrderDTO;
import com.example.demo.model.response.FeedbackResponse;
import com.example.demo.model.response.OrderResponse;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.FeedbackRepository;
import com.example.demo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    @Autowired
    FeedbackRepository feedbackRepository;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    OrderRepository orderRepository;

    public Feedback createNewFeedback(FeedbackRequestDTO feedbackRequestDTO) {
        Account shop = accountRepository.findById(feedbackRequestDTO.getShopId()).orElseThrow(()-> new EntityNotFoundException("Not found shop"));
        Feedback feedback = new Feedback();
        feedback.setContent(feedbackRequestDTO.getContent());
        feedback.setRating(feedbackRequestDTO.getRating());
        feedback.setCustomer(authenticationService.getCurrentAccount());
        feedback.setShop(shop);
        return feedbackRepository.save(feedback);
    }
    public List<FeedbackResponse> getFeedback() {
        List<FeedbackResponse> feedbacks = feedbackRepository.feedbackList(authenticationService.getCurrentAccount().getId());
        return feedbacks;
    }

    public Order feedbackOrder(FeedbackRequestOrderDTO feedbackRequestOrderDTO){
        Order order = orderRepository.findById(feedbackRequestOrderDTO.getOrderID()).orElseThrow
                ( () -> new EntityNotFoundException("Order Not Exist"));
        Feedback feedback = new Feedback();
        feedback.setOrder(order);
        feedback.setRating(feedbackRequestOrderDTO.getRating());
        feedback.setContent(feedbackRequestOrderDTO.getContent());
         feedbackRepository.save(feedback);
        order.setFeedback(feedback);
        orderRepository.save(order);
        return order;
    }
}
