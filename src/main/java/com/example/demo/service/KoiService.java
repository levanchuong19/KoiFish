package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.Koi;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.KoiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KoiService {

    @Autowired
    private KoiRepository koiRepository;
    @Autowired
    private AccountRepository accountRepository;

    public Koi create(Koi koi) {
        koi.setOwner(getCurrentAccount());
        return koiRepository.save(koi);
    }

    public List<Koi> getAll() {
        return koiRepository.findAll();
    }
    public Account getCurrentAccount(){
        Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //phai get thong tin user tu database

        return accountRepository.findAccountById(account.getId());
    }

}
