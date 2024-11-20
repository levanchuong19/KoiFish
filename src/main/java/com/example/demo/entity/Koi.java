package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.codec.StringDecoder;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Koi {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    String image;
    String name;
    String description;
    float price;

    @OneToMany(mappedBy = "koi")
    @JsonIgnore
    List<OrderDetails> orderDetails;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    @JsonIgnore
    Account owner;
}
