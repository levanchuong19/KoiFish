package com.example.demo.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class StudentRequest {
    @NotBlank(message = "name is mandatory")
    String name;
    String code;
    float score;
    Long major_id;
    Set<Long> class_ids;

}
