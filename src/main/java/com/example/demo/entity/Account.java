package com.example.demo.entity;

import com.example.demo.entity.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity// This tells Hibernate to make a table out of this class
public class Account implements UserDetails {
    // help spring security identify which table is storing Account(username , password)

    @Id// đánh dấu là primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

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

    @Enumerated(EnumType.STRING)
    Role role;

    float balance = 0;

    @OneToMany(mappedBy = "customer")
    Set<Feedback> customerFeedbacks;//1 customer tao nhieu feedback

    @OneToMany(mappedBy = "shop")
    Set<Feedback> shopFeedbacks;//1 shop nhan nhieu feedbacks



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { // đinh nghĩa quyền hạn account này làm đc
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if(this.role != null){
            authorities.add(new SimpleGrantedAuthority(this.role.toString()));
        }
        return authorities;
    }

    @Override
    public String getUsername() {
        return this.phone; // cho user phuong thuc dang nhap bang username nao`
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @OneToMany(mappedBy = "account")
    @JsonIgnore
    List<Student> studentList;

    @OneToMany(mappedBy = "customer")
    List<Order> orderList;

    @OneToMany(mappedBy = "from")
    Set<Transaction> transactionsFrom;

    @OneToMany(mappedBy = "to")
    Set<Transaction> transactionsTo;

    @OneToMany(mappedBy = "owner")
    Set<Koi> kois;
}
