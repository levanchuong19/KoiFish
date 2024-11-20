package com.example.demo.repository;

import com.example.demo.entity.Koi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface KoiRepository extends JpaRepository<Koi, UUID> {
    Koi findKoiById(UUID id);
//    @Query("SELECT p.name, SUM(od.quantity) AS totalSold " +
//            "FROM OrderDetails od JOIN od.koi p " +
//            "GROUP BY p.id " +
//            "ORDER BY totalSold DESC")
//    List<Object[]> findTop5BestSellingProducts();

    //top 5 san pham ban chay nhat (id , name , quantity )
    @Query("SELECT tblKoi.id,tblKoi.name,SUM(tblOd.quantity) as totalSold FROM Koi tblKoi JOIN tblKoi.orderDetails tblOd GROUP BY tblKoi.id ORDER BY totalSold desc")
    List<Object[]> findTop5BestSellingProducts();

}
