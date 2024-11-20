package com.example.demo.model.response;

import com.example.demo.entity.Account;
import lombok.Data;

@Data
public class EmailDetail {
    Account account;
    String subject;
    String link;
}
