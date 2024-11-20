package com.example.demo.api;

import com.example.demo.entity.Student;
import com.example.demo.model.request.StudentRequest;
import com.example.demo.model.response.StudentResponse;
import com.example.demo.service.StudentService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.persistence.ManyToOne;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/student")
//@CrossOrigin("*")//cơ chế CORS : chặn quyền truy cập giữa các app khác nhau . Đánh dấu (*) là cho phép truy cập hết lẫn nhau
// @CrossOrigin({đường dẫn đến cho app nhất định truy cập đc}) thường gắn đường dẫn của front-end chỉ cho phép 1 thằng front end đó truy cập vào đc
@SecurityRequirement(name="api")// để sử dụng token tren swagger
public class StudentAPI {

    @Autowired
    StudentService studentService;

    @PostMapping
    @PreAuthorize("hasAuthority('LECTURER')")
    //@Valid before @Requestbody automatically init Validation Handler and call if it matches Exception
    public ResponseEntity<Student> createStudent(@Valid @RequestBody StudentRequest student){
        Student newStudent = studentService.create(student);

        return ResponseEntity.ok(newStudent);//.ok :trả về success
    }

    @GetMapping
    public ResponseEntity<StudentResponse> getStudentList(@RequestParam int page , @RequestParam(defaultValue = "5") int size){
        StudentResponse studentResponse = studentService.getAllStudents(page , size);
        return ResponseEntity.ok(studentResponse);
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<Student> updateStudent(@PathVariable long studentId ,@Valid @RequestBody Student student){
        Student updatedStudent = studentService.update(studentId,student);
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<Student> deleteStudent(@PathVariable long studentId){
        Student deletedStudent = studentService.delete(studentId);
        return ResponseEntity.ok(deletedStudent);
    }




}
