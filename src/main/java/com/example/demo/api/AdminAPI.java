package com.example.demo.api;

import com.example.demo.service.AdminService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/")
@CrossOrigin("*")
@SecurityRequirement(name="api")
public class AdminAPI {

    @Autowired
    AdminService adminService;

    @GetMapping("/admin/stats")
    public ResponseEntity getDashboardStats(){
        Map<String,Object> stats = adminService.getDashboardStat();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/admin/revenue")
    public ResponseEntity getRevenue(){
        Map<String,Object> revenue = adminService.getMonthlyRevenue();
        return ResponseEntity.ok(revenue);
    }
}
