package com.example.demo.service;

import com.example.demo.entity.Major;
import com.example.demo.model.request.MajorRequest;
import com.example.demo.repository.MajorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MajorService {

    @Autowired
    private MajorRepository majorRepository;

    public Major createMajor(MajorRequest majorRequest) {
        Major major = new Major();
        major.setMajor_code(majorRequest.getCode());
        major.setMajor_name(majorRequest.getName());
        try{
            return majorRepository.save(major);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }
}
