package edu.ncsu.csc.iTrust2.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.iTrust2.models.CPTCode;

/**
 * Interface for CPTCode repository
 *
 * @author colinscanlon
 *
 */
public interface CPTCodeRepository extends JpaRepository<CPTCode, Long> {

    /**
     * Finds an ICDCode by the provided code
     *
     * @param code
     *            Code to search by
     * @return Matching code, if any
     */
    public CPTCode findByCode ( String code );

    /**
     * Find all archived CPT codes
     *
     * @return matching codes
     */
    public List<CPTCode> findByArchiveIsTrue ();

    /**
     * Find all active CPT codes
     *
     * @return matching codes
     */
    public List<CPTCode> findByArchiveIsFalse ();

}
