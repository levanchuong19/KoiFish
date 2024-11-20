package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.model.AccountResponse;
import com.example.demo.model.LoginRequest;
import com.example.demo.model.RegisterRequest;
import com.example.demo.model.request.ResetPasswordRequest;
import com.example.demo.model.response.EmailDetail;
import com.example.demo.repository.AccountRepository;
import jakarta.validation.ConstraintViolationException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    AccountRepository accountRepository; // ~ dao
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    TokenService tokenService;

    @Autowired
    EmailService emailService;


    public AccountResponse register(RegisterRequest registerRequest) {
        Account newAccount = modelMapper.map(registerRequest, Account.class);
        try {
            newAccount.setPassword(passwordEncoder.encode(registerRequest.getPassword()));//encode password before save to db
            accountRepository.save(newAccount);// JPA có sẵn :  INSERT INTO account(...) VALUES (....)
            EmailDetail emailDetail = new EmailDetail();
            emailDetail.setAccount(newAccount);
            emailDetail.setSubject("Hello world");
            emailDetail.setLink("https://www.google.com/");
            emailService.sendEmail(emailDetail);
            return modelMapper.map(newAccount,AccountResponse.class);

        } catch (Exception e) {
            if (e.getMessage().contains(newAccount.getCode())) {
                throw new DuplicateEntity("Duplicated code");
            } else if (e.getMessage().contains(newAccount.getEmail())) {
                throw new DuplicateEntity("Duplicated  email ");
            } else {
                throw new DuplicateEntity("Duplicated  phone ");
            }
        }


    }

    public AccountResponse login(LoginRequest loginRequest) {
        try{
            Authentication authentication =
                    authenticationManager.  authenticate(new UsernamePasswordAuthenticationToken( // xac thuc
                    // username , password (
                    // tu dong ma hoa password user va check tren database )
                    loginRequest.getUsername() , loginRequest.getPassword() // go to loadUserByUsername(String phone)
                            // to check username in db first -> so sanh password db with request password

            ));
            //==> account exists
            Account account = (Account) authentication.getPrincipal(); // tra ve account tu database
            AccountResponse accountResponse = modelMapper.map(account, AccountResponse.class);
            accountResponse.setToken(tokenService.generateToken(account));
            return accountResponse; // response thong tin account
        } catch (Exception e) {
            e.printStackTrace();
            throw new EntityNotFoundException("Username or password is incorrect");
        }

    }

    public List<Account> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts;
    }

    public void forgotPassword(String email) {
        Account account = accountRepository.findAccountByEmail(email);
        if(account == null) {
            throw new EntityNotFoundException("Account not found");
        }
        String token = tokenService.generateToken(account);
        EmailDetail emailDetail = new EmailDetail();
        emailDetail.setAccount(account);//set receiver
        emailDetail.setSubject("Reset password");
        emailDetail.setLink("https://www.google.com/?token="+token);
        emailService.sendEmail(emailDetail);

    }

    public Account resetPassword(ResetPasswordRequest resetPasswordRequest) {
        Account account = getCurrentAccount();
        account.setPassword(passwordEncoder.encode(resetPasswordRequest.getPassword()));
        try{
            accountRepository.save(account);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

        return account;
    }

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
       Account account = accountRepository.findAccountByPhone(phone);
       if (account == null) {
           throw new EntityNotFoundException("User not found");
       }
       return account;
    }



    public Account getCurrentAccount(){
        Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //phai get thong tin user tu database

        return accountRepository.findAccountById(account.getId());
    }
}
