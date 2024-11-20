package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "class")
@Data
public class ClassEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_id")
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(name = "student_class" , joinColumns = @JoinColumn(name = "class_id") ,
                                        inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    @JsonIgnore
    Set<Student> students;



}
