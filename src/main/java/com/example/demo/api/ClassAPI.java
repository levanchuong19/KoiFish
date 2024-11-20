package com.example.demo.api;

import com.example.demo.entity.ClassEntity;
import com.example.demo.model.request.ClassEntityRequest;
import com.example.demo.service.ClassService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/class")
@SecurityRequirement(name = "api")
public class ClassAPI {

    @Autowired
    private ClassService classService;

    @PostMapping()
    public ResponseEntity createNewClass(@RequestBody ClassEntityRequest classEntityRequest) {
        ClassEntity classEntity = classService.createNewClass(classEntityRequest);
        return ResponseEntity.ok().body(classEntity);

    }
}
