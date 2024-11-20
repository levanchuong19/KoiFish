package com.example.demo.model.request;

import com.example.demo.entity.Feedback;
import lombok.Data;

@Data
public class FeedbackRequestDTO {
    private String content;
    private int rating;
    private long shopId;
}
