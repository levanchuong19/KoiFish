package com.example.demo.exception;

public class AuthenException extends RuntimeException {// tạo định nghĩa hệ thống lỗi
   public AuthenException(String msg){
        super(msg);
    }

}
