package com.telco.phonenumberservice.repository.phonenumber;

import com.telco.phonenumberservice.repository.customer.Customer;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
public class PhoneNumber {

    @Id
    @GeneratedValue(generator =  "UUID")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "number", nullable = false, unique = true)
    private String number;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
}
