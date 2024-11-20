package com.example.demo.repository;

import com.example.demo.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByStudentID(long studentID);

    @Query("SELECT U FROM Student U WHERE  U.isDeleted = false ")
    List<Student> getAvailableStudent();

    //findStudentsByIsDeletedFalse();
    List<Student>findStudentsByIsDeletedTrue();

    Page<Student> findAll(Pageable pageable  );
}
