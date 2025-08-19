package com.telco.phonenumberservice.repository.phonenumber;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, UUID> {
    List<PhoneNumber> findByCustomer_CustomerId(String customerId);

    Optional<PhoneNumber> findByNumber(String number);
}
