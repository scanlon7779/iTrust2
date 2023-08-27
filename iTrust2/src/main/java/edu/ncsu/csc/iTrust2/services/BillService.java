package edu.ncsu.csc.iTrust2.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.iTrust2.models.Bill;
import edu.ncsu.csc.iTrust2.models.User;
import edu.ncsu.csc.iTrust2.repositories.BillRepository;

/**
 * Service class for bills
 *
 * @author colinscanlon
 *
 */
@Component
@Transactional
public class BillService extends Service<Bill, Long> {

    /** Repository for CRUD operations */
    @Autowired
    private BillRepository repository;

    @Override
    protected JpaRepository<Bill, Long> getRepository () {
        return repository;
    }

    /**
     * Gets a list of bills for a patient
     *
     * @param patient
     *            The patient to get bills for
     * @return The list of bills
     */
    public List<Bill> findByPatient ( final User patient ) {

        return repository.findByPatient( patient );

    }

}
