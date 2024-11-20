package com.example.demo.exception;

public class DuplicateEntity extends RuntimeException {// tạo định nghĩa hệ thống lỗi
   public DuplicateEntity(String msg){
        super(msg);
    }

}
