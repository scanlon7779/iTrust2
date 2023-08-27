package edu.ncsu.csc.iTrust2.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.iTrust2.models.Payment;
import edu.ncsu.csc.iTrust2.repositories.PaymentRepository;

/**
 * Payment service
 *
 * @author colinscanlon
 *
 */
@Component
@Transactional
public class PaymentService extends Service<Payment, Long> {

    /**
     * payment repo
     */
    @Autowired
    private PaymentRepository repository;

    @Override
    protected JpaRepository<Payment, Long> getRepository () {

        return repository;
    }

}
