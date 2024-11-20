package com.example.demo.api;

import com.example.demo.entity.Account;
import com.example.demo.entity.enums.Role;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.KoiRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.service.AuthenticationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/class")
@SecurityRequirement(name = "api")
public class DashboardAPI {

    @Autowired
    private KoiRepository productRepository;

    @Autowired
    private AccountRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    AuthenticationService authenticationService;

    // API để lấy số lượng sản phẩm, số lượng user theo role, và top 5 sản phẩm bán chạy
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        // Đếm tổng số sản phẩm trong hệ thống
        long totalProducts = productRepository.count();
        stats.put("totalProducts", totalProducts);

        // Đếm số lượng người dùng theo role "Customer"
        long customerCount = userRepository.countByRole(Role.CUSTOMER);
        stats.put("customerCount", customerCount);

        // Đếm số lượng người dùng theo role "Owner"
        long ownerCount = userRepository.countByRole(Role.OWNER);
        stats.put("ownerCount", ownerCount);

        // Lấy top 5 sản phẩm bán chạy nhất
        List<Object[]> topProducts = productRepository.findTop5BestSellingProducts();
        List<Map<String, Object>> topProductList = new ArrayList<>();

        for (Object[] productData : topProducts) {
            Map<String, Object> productInfo = new HashMap<>();
            productInfo.put("productName", productData[0]);
            productInfo.put("totalSold", productData[1]);
            topProductList.add(productInfo);
        }
        stats.put("topProducts", topProductList);

        return ResponseEntity.ok(stats);
    }



//    @GetMapping("/revenue/monthly")
//    public ResponseEntity<Map<String, Object>> getMonthlyRevenueFromTransactions() {
//        Map<String, Object> revenueData = new HashMap<>();
//
//        // Lấy thông tin user đang đăng nhập
//        Account currentUser = authenticationService.getCurrentAccount();
//
//        // Nếu không tìm thấy người dùng hiện tại, trả về lỗi
//        if (currentUser == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
//        }
//
//        // Lấy doanh thu theo từng tháng từ Transaction của user hiện tại
//        List<Object[]> monthlyRevenue = transactionRepository.calculateMonthlyRevenueForUser(currentUser.getId());
//
//        List<Map<String, Object>> monthlyRevenueList = new ArrayList<>();
//
//        for (Object[] result : monthlyRevenue) {
//            Map<String, Object> monthData = new HashMap<>();
//            monthData.put("year", result[0]);
//            monthData.put("month", result[1]);
//            monthData.put("totalRevenue", result[2]);
//            monthlyRevenueList.add(monthData);
//        }
//
//        revenueData.put("monthlyRevenue", monthlyRevenueList);
//
//        return ResponseEntity.ok(revenueData);
//    }
}
