package com.example.demo.entity;

import com.example.demo.entity.enums.TransactionEnum;
import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;
import java.util.Objects;


@Entity
@Data
@Table(name = "Transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    Date createAt;
    @Enumerated(EnumType.STRING)
    TransactionEnum status;
    String description;
    float amount;

    @ManyToOne
    @JoinColumn(name = "from_id")
    Account from;

    @ManyToOne
    @JoinColumn(name = "to_id")
    Account to;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    Payment payment;


    @Override
    public String toString() {
        return "Transaction{id=" + id + ", createAt=" + createAt + ", status=" + status + ", description='" + description + "'}";
        // Omitting 'payment' to avoid recursion
    }


    @Override
    public int hashCode() {
        // Use fields that uniquely identify the Transaction
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Transaction)) return false;
        Transaction other = (Transaction) obj;
        return Objects.equals(createAt, other.createAt) && // Compare other relevant fields
                Objects.equals(from, other.from) &&
                Objects.equals(to, other.to) &&
                Objects.equals(status, other.status) &&
                Objects.equals(description, other.description);
    }

}
