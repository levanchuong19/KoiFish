package com.example.demo.model.request;

import lombok.Data;

import java.util.UUID;

@Data
public class FeedbackRequestOrderDTO {
    private String content;
    private int rating;
    private UUID orderID;
}
