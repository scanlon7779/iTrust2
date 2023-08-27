package edu.ncsu.csc.iTrust2.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Payment object class
 *
 * @author colinscanlon
 *
 */
@Entity
public class Payment extends DomainObject {

    /**
     * The patient of this Bill
     */
    @NotNull
    @ManyToOne
    @JoinColumn ( name = "patient_id", columnDefinition = "varchar(100)" )
    private User    patient;

    /**
     * The id of this Payment
     */
    @Id
    @GeneratedValue ( strategy = GenerationType.AUTO )
    private Long    id;

    /**
     * The amount of the payment
     */
    @Min ( 0 )
    private Integer amount;

    /**
     * The amount of the payment
     */
    private String  type;

    /**
     * Hibertnate constructor
     */
    public Payment () {

    }

    /**
     * Const for payment
     *
     * @param patient
     *            patient
     * @param amount
     *            amount
     * @param type
     *            type
     */
    public Payment ( final User patient, final Integer amount, final String type ) {

        setPatient( patient );
        setAmount( amount );
        setType( type );
    }

    /**
     * @return the patient
     */
    public User getPatient () {
        return patient;
    }

    /**
     * @param patient
     *            the patient to set
     */
    public void setPatient ( final User patient ) {
        this.patient = patient;
    }

    /**
     * @return the id
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * @return the amount
     */
    public Integer getAmount () {
        return amount;
    }

    /**
     * @param amount
     *            the amount to set
     */
    public void setAmount ( final Integer amount ) {
        this.amount = amount;
    }

    /**
     * @return the type
     */
    public String getType () {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType ( final String type ) {
        this.type = type;
    }

}
