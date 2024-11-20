package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.ClassEntity;
import com.example.demo.entity.Major;
import com.example.demo.entity.Student;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.model.request.StudentRequest;
import com.example.demo.model.response.StudentResponse;
import com.example.demo.repository.ClassEntityRepository;
import com.example.demo.repository.MajorRepository;
import com.example.demo.repository.StudentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class StudentService {
    @Autowired
    StudentRepository studentRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    MajorRepository majorRepository;

    @Autowired
    ClassEntityRepository classEntityRepository;

    public Student create(StudentRequest studentRequest) {
        Student newStudent = modelMapper.map(studentRequest, Student.class);
        Account account = authenticationService.getCurrentAccount();
        newStudent.setAccount(account);
        Major major = majorRepository.findMajorByMajor_id(studentRequest.getMajor_id());
        if(major == null) {
            throw new EntityNotFoundException("Major not found");
        }
        newStudent.setMajor(major);
        Set<ClassEntity> classEntitySet = new HashSet<>();
        for(Long id_class : studentRequest.getClass_ids()) {
            ClassEntity classEntity = classEntityRepository.findById(id_class).orElseThrow(() -> new EntityNotFoundException("Class not found"));
            classEntitySet.add(classEntity);
        }
        newStudent.setClassEntities(classEntitySet);
        try{
            studentRepository.save(newStudent);
            return newStudent;
        }catch (Exception e){
            e.printStackTrace();
            throw new EntityNotFoundException("error when saving student to db");
        }
    }
    public StudentResponse getAllStudents(int page , int size) {
        Page studentPage = studentRepository.findAll(PageRequest.of(page, size));
        StudentResponse studentResponse = new StudentResponse();
        studentResponse.setPageNumber(studentPage.getNumber());
        studentResponse.setContent(studentPage.getContent());
        studentResponse.setTotalElements(studentPage.getNumberOfElements());
        studentResponse.setTotalPages(studentPage.getTotalPages());
       return studentResponse;
    }

    public Student update(long studentID, Student student) {
        Student oldStudent = getStudentById(studentID);

        oldStudent.setStudentCode(student.getStudentCode());
        oldStudent.setName(student.getName());
        oldStudent.setScore(student.getScore());
        return  studentRepository.save(oldStudent);
    }

    public Student delete(long studentID) {
        Student oldStudent = studentRepository.findByStudentID(studentID);
        if (oldStudent == null) {
            throw new EntityNotFoundException("Student not found");
        }
        oldStudent.setDeleted(true);
        return studentRepository.save(oldStudent);
    }

    public Student getStudentById(long studentId){
        Student oldStudent = studentRepository.findByStudentID(studentId);
        if (oldStudent == null) {
            throw new EntityNotFoundException("Student not found");
        }
        return oldStudent;
    }
}
