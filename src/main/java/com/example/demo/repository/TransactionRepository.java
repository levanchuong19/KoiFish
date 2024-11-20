package com.example.demo.repository;

import com.example.demo.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // Truy vấn để lấy doanh thu từ Transaction theo từng tháng
//    @Query("SELECT YEAR(t.createAt) as year, MONTH(t.createAt) as month, SUM(t.payment.total) as totalRevenue " +
//            "FROM Transaction t " +
//            "WHERE t.status = 'SUCCESS' AND t.to.id = :userId " +  // Lọc theo trạng thái hoàn thành và user hiện tại
//            "GROUP BY YEAR(t.createAt), MONTH(t.createAt) " +
//            "ORDER BY YEAR(t.createAt), MONTH(t.createAt)")
//    List<Object[]> calculateMonthlyRevenueForUser(@Param("userId") Long userId);


      @Query("SELECT YEAR(t.createAt) as year ,MONTH(t.createAt) , SUM(t.amount) as month " +
              "FROM Transaction t" +
              " WHERE t.status='SUCCESS' AND t.to.id =:userId " +
              "GROUP BY YEAR(t.createAt) , MONTH(t.createAt) " +
              "ORDER BY YEAR(t.createAt) , MONTH(t.createAt)")
      List<Object[]> calculateMonthlyRevenue(@Param("userId") Long userID);

}
