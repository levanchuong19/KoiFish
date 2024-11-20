package com.example.demo.exception;

public class EntityNotFoundException extends RuntimeException {// tạo định nghĩa hệ thống lỗi
   public EntityNotFoundException(String msg){
        super(msg);
    }

}
