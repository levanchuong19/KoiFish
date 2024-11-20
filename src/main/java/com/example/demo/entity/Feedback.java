package com.example.demo.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String content;

    private int rating;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    Account customer;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    @JsonIgnore
    Account shop;

    @JsonIgnore
    @OneToOne(mappedBy = "feedback", cascade = CascadeType.ALL)
    Order order;

}
