package com.example.demo.model.request;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDTO {
    List<OrderDetailsRequestDTO> details;
}
