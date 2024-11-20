package com.example.demo.entity;

import com.example.demo.entity.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;


    Date date;

    float total;

    @Enumerated(EnumType.STRING)
    OrderStatus status = OrderStatus.PENDING;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    Account customer;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)

            //khi lưu  order -> tự lưu order details
    List<OrderDetails> orderDetails;

    @OneToOne(mappedBy = "order")
    @JsonIgnore
    Payment payment;


    @OneToOne
    @JoinColumn(name = "feedback_id")
    Feedback feedback;


}
