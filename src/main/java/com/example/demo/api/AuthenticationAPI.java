package com.example.demo.api;

import com.example.demo.entity.Account;
import com.example.demo.model.AccountResponse;
import com.example.demo.model.LoginRequest;
import com.example.demo.model.RegisterRequest;
import com.example.demo.model.request.ForgotPasswordRequest;
import com.example.demo.model.request.ResetPasswordRequest;
import com.example.demo.service.AuthenticationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
@CrossOrigin("*")
@SecurityRequirement(name="api")// để sử dụng token tren swagger
public class AuthenticationAPI {
    @Autowired
    AuthenticationService authenticationService;
    @PostMapping("register")
    public ResponseEntity register (@Valid @RequestBody RegisterRequest registerRequest) {
        //DI : Dependency Injection
        AccountResponse newAccount = authenticationService.register(registerRequest);
        return  ResponseEntity.ok(newAccount);
    }

    @GetMapping("account")
    public ResponseEntity getAllAccounts() {
        List<Account> accounts = authenticationService.getAllAccounts();
        return  ResponseEntity.ok(accounts);
    }

    @PostMapping("login")
    public ResponseEntity login(@Valid @RequestBody LoginRequest loginRequest) {
        AccountResponse accountResponse = authenticationService.login(loginRequest);
        return  ResponseEntity.ok(accountResponse);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        authenticationService.forgotPassword(forgotPasswordRequest.getEmail());
        return ResponseEntity.ok("Check your email to confirm reset password");
    }
    @PostMapping("/reset-password")
    public ResponseEntity resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        authenticationService.resetPassword(resetPasswordRequest);
        return ResponseEntity.ok("Password reset successfully");
    }


}
