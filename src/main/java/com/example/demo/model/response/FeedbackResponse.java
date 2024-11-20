package com.example.demo.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FeedbackResponse {
    private Long id;
    private String email;
    private String content;
    private int rating;

}
