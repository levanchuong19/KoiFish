package com.example.demo.service;

import com.example.demo.entity.ClassEntity;
import com.example.demo.model.request.ClassEntityRequest;
import com.example.demo.repository.ClassEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class ClassService {
    @Autowired
    private ClassEntityRepository classEntityRepository;

    public ClassEntity createNewClass(ClassEntityRequest classEntityRequest) {
        ClassEntity classEntity = new ClassEntity();
        classEntity.setName(classEntityRequest.getName());

        try {
            classEntityRepository.save(classEntity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return classEntity;
    }

}
