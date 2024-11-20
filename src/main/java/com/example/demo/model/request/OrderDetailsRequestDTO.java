package com.example.demo.model.request;

import lombok.Data;

import java.util.UUID;
@Data
public class OrderDetailsRequestDTO {
    UUID koiId;
    int quantity;

}
