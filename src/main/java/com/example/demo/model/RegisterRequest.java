package com.example.demo.model;

import com.example.demo.entity.enums.Role;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Code can not be blank !")
    @Pattern(regexp = "KH\\d{6}",message = "Invalid code format")
    @Column(unique = true)
    String code;
    //KHxxxxxx
    @Email(message = "Invalid email")
    @Column(unique = true)
    String email;
    @Pattern(regexp = "(84|0[3|5|7|8|9])+(\\d{8})\\b" , message = "Invalid phone number")
    @Column(unique = true)
    String phone;
    @Size(min = 6 , message = "Password must be exceed 6 characters ")
    String password;
    Role role;


}
