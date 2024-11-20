package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Major {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long major_id;

    @Column(unique = true)
    String major_code;

    String major_name;

    @OneToMany(mappedBy = "major")//
    Set<Student> students;


}
