package com.example.demo.api;

import com.example.demo.entity.Order;
import com.example.demo.model.request.OrderRequestDTO;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.AuthenticationService;
import com.example.demo.service.OrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/order")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class OrderAPI {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity create(@RequestBody OrderRequestDTO orderRequestDTO) {
        String vnPayUrl = null;
        try {
            vnPayUrl = orderService.createUrl(orderRequestDTO);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return ResponseEntity.ok(vnPayUrl);
    }

    @GetMapping
    public ResponseEntity getAll() {
        List<Order> orders = orderRepository.findOrderByCustomer(authenticationService.getCurrentAccount());
        return ResponseEntity.ok(orders);
    }

    @PostMapping("/transaction")
    public ResponseEntity createTransaction(@RequestParam UUID orderID) {
        orderService.createTransaction(orderID);
        return ResponseEntity.ok("success");
    }
}
