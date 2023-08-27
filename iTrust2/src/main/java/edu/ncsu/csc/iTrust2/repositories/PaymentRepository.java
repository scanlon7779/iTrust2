package edu.ncsu.csc.iTrust2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.iTrust2.models.Payment;

/**
 * Payment repo
 *
 * @author colinscanlon
 *
 */
public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
