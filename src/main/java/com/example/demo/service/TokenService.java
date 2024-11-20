package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.repository.AccountRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class TokenService {

    @Autowired
    private AccountRepository accountRepository;

    public final String SECRET_KEY = "4bb6d1dfbafb64a681139d1586b6f1160d18159afd57c8c79136d7490630407cnt"; //tạo secretkey chỉ lưu ở back-end để bảo mật
    private SecretKey getSigninKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    //Tạo ra token
    public String generateToken(Account account) { // 1account~ 1token khac nhau
        String token = Jwts.builder()
                .subject(account.getId()+ "")//save unique(id ,..) information from user to token
                .issuedAt(new Date(System.currentTimeMillis()))//10:30
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))//1 day ( 1000 : 1s ) * 60 = 1min
                                                                                        //1 day ( 1000 : 1s ) * 60 * 60 = 1h
                                                                                        // 1 day ( 1000 : 1s ) * 60 *  60  * 24 = 1day
                .signWith(getSigninKey())
                .compact();
        return token;
    }


    //Verify cái token
    public Account getAccountByToken(String token) {
        //nhờ JWT decode token để lấy thông tin
        Claims claims = Jwts.parser()
                .verifyWith(getSigninKey())
                .build().parseSignedClaims(token)
                .getPayload();// lấy nôị dung token ( id string cua user )
        String idString = claims.getSubject();
        Long id = Long.parseLong(idString);// convert to Long
        Account account = accountRepository.findAccountById(id);
        // mô hình dùng token để lưu thông tin user -> stateless
        // mô hình dùng Session ....................-> stateful
        return account;
    }
}
