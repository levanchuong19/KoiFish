package com.example.demo.repository;

import com.example.demo.entity.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MajorRepository extends JpaRepository<Major, Long> {
    @Query("SELECT U FROM Major U WHERE U.major_id = ?1")
    Major findMajorByMajor_id(@Param("major_id") Long id);
}
