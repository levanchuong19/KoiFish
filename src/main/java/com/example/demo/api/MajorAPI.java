package com.example.demo.api;

import com.example.demo.entity.Major;
import com.example.demo.model.request.MajorRequest;
import com.example.demo.service.MajorService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/major")
@SecurityRequirement(name = "api")
public class MajorAPI {

    @Autowired
    MajorService majorService;

    @PostMapping("")
    public ResponseEntity<Major> createMajor(@Valid @RequestBody MajorRequest majorRequest) {
        Major newMajor = majorService.createMajor(majorRequest);
        return ResponseEntity.ok(newMajor);
    }


}
