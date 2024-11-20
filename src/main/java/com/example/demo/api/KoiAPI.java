package com.example.demo.api;


import com.example.demo.entity.Koi;
import com.example.demo.service.KoiService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/koi")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class KoiAPI {

    @Autowired
    private KoiService koiService;

    @GetMapping
    public ResponseEntity getAll(){
        List<Koi> kois = koiService.getAll();
        return ResponseEntity.ok(kois);
    }

    @PostMapping
    public ResponseEntity create(@RequestBody Koi koi){
        Koi newKoi = koiService.create(koi);
        return ResponseEntity.ok(newKoi);
    }
}

//1.API lấy danh sách lên -> phân trang ở back-end
//2.Tạo và lưu đơn hàng
//3.Lịch sử mua hàng

