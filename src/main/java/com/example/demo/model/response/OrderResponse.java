package com.example.demo.model.response;

import com.example.demo.entity.Feedback;
import com.example.demo.entity.Order;
import lombok.Data;

@Data
public class OrderResponse {
    Order order;
    Feedback feedback;
}
