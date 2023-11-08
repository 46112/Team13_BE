package com.theocean.fundering.domain.payment.repository;


import com.theocean.fundering.domain.payment.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
