package com.example.demo.model.response;

import com.example.demo.entity.Student;
import lombok.Data;

import java.util.List;

@Data
public class StudentResponse {
    private List<Student> content;
    private int pageNumber;
    private int totalElements;
    private int totalPages;

}
