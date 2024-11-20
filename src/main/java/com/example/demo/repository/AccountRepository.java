package com.example.demo.repository;

import com.example.demo.entity.Account;
import com.example.demo.entity.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends JpaRepository<Account, Long> { // cung cấp entity và kiểu dữ liệu của PK
    // vì jpa đã giúp xử lí phần logic -> cho class này là interface nên ko cần code gì cả
    Account findAccountByPhone(String phone);

    Account findAccountById(Long id);

    Account findAccountByEmail(String email);

    Account findAccountByRole(Role role);

    @Query("SELECT COUNT(u) FROM Account u WHERE u.role = :role")
    long countByRole(@Param("role") Role role);
}
