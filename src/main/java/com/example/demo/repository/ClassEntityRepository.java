package com.example.demo.repository;

import com.example.demo.entity.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassEntityRepository extends JpaRepository<ClassEntity, Long> {
    ClassEntity findClassEntityById(Long id);
}
