package com.telco.phonenumberservice.repository.customer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;


@Entity
@Data
public class Customer {
    @Id
    @Column(name = "customer_id")
    private String customerId;

    private String name;
}
