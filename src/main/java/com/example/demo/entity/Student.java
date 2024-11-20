package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//lombok hỗ trợ các annotation tạo getter setter , constructor
//khi có thông tin mới nó cũng tự cập nhật cho mình ( thêm biến age ... )

@Entity
@Table(name = "tblStudent")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)// ẩn đi , chỉ xuất hiện khi READ ( C "R" U D )
    Long studentID;
    @NotBlank(message = "Name can not be blank !"/*Customized message (optional)*/)
    String name;
    @NotBlank(message = "ID can not be blank !")
    @Pattern(regexp = "SE\\d{6}" , message = "Student Code format is not valid")
    String studentCode;// pattern SExxxxxx
    @Min(value = 0 , message = "Score must be at least 0")
    @Max(value = 10 , message = "Score cannot be exceeded 10")
    float score;
    @JsonIgnore // ko bắt request nhap them thong tin nay
    boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "account_id")
    Account account;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "major_id")
    Major major;

    @ManyToMany(mappedBy = "students")

    Set<ClassEntity> classEntities;
}
