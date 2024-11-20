package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.enums.Role;
import com.example.demo.exception.AuthenException;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.KoiRepository;
import com.example.demo.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AdminService {
    //đếm sản phẩm trong system
    // số lượng customerr
    //số lượng owner
    //top 5 sản phầm bán chạy nhất

    @Autowired
    KoiRepository koiRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    AuthenticationService authenticationService;

    public Map<String, Object> getDashboardStat() {
        Map<String, Object> stats = new HashMap<>();

        long totalProducts = koiRepository.count();
        long customersCount = accountRepository.countByRole(Role.CUSTOMER);
        long ownersCount = accountRepository.countByRole(Role.OWNER);
        List<Object[]> topProducts = koiRepository.findTop5BestSellingProducts();

        List<Map<String, Object>> topProductList = new ArrayList<>();
        for (Object[] productsData : topProducts) {
            Map<String, Object> productInfo = new HashMap<>();
            productInfo.put("productId", productsData[0]);
            productInfo.put("productName", productsData[1]);
            productInfo.put("totalSold", productsData[2]);
            topProductList.add(productInfo);
        }
        stats.put("Total products", totalProducts);
        stats.put("Customer count", customersCount);
        stats.put("Owners count", ownersCount);
        stats.put("Top products", topProductList);
        return stats;

    }

    public Map<String, Object> getMonthlyRevenue() {
        Map<String, Object> revenueData = new HashMap<>();
        Account account = authenticationService.getCurrentAccount();
        if (account == null) {
            throw new AuthenException("You are not logged in");
        }
        List<Object[]> monthlyRevenue =
                transactionRepository.calculateMonthlyRevenue(authenticationService.getCurrentAccount().getId());
        List<Map<String, Object>> revenueList = new ArrayList<>();
        for (Object[] result : monthlyRevenue) {
            Map<String, Object> revenue = new HashMap<>();
            revenue.put("Year", result[0]);
            revenue.put("Month", result[1]);
            revenue.put("Total revenue", result[2]);
            revenueList.add(revenue);
        }
        revenueData.put("Monthly revenue", revenueList);
        return revenueData;
    }

}
