package edu.ncsu.csc.iTrust2.models;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.google.gson.annotations.JsonAdapter;

import edu.ncsu.csc.iTrust2.adapters.ZonedDateTimeAdapter;
import edu.ncsu.csc.iTrust2.adapters.ZonedDateTimeAttributeConverter;

/**
 * representation of a Bill in the iTrust2 System
 *
 * @author colinscanlon
 * @author jmbuck4
 *
 */
@Entity
public class Bill extends DomainObject {

    /**
     * The patient of this Bill
     */
    @NotNull
    @ManyToOne
    @JoinColumn ( name = "patient_id", columnDefinition = "varchar(100)" )
    private User          patient;

    /**
     * The hcp of this Bill
     */
    @NotNull
    @ManyToOne
    @JoinColumn ( name = "hcp_id", columnDefinition = "varchar(100)" )
    private User          hcp;

    /**
     * The date of this Bill
     */
    @NotNull
    @Basic
    // Allows the field to show up nicely in the database
    @Convert ( converter = ZonedDateTimeAttributeConverter.class )
    @JsonAdapter ( ZonedDateTimeAdapter.class )
    private ZonedDateTime date;

    /**
     * The id of this Bill
     */
    @Id
    @GeneratedValue ( strategy = GenerationType.AUTO )
    private Long          id;

    /**
     * List of all CPTCodes for a Bill
     */
    @ManyToMany ( cascade = CascadeType.MERGE )
    @JoinTable ( name = "BILL_CODES", joinColumns = { @JoinColumn ( name = "bill_id" ) },
            inverseJoinColumns = { @JoinColumn ( name = "cptcode_id" ) } )
    @JsonManagedReference
    private List<CPTCode> cptCodes;

    /**
     * The remaining cost of the bill
     */
    private Integer       cost;

    /**
     * The status of the bill
     */
    private String        status;

    /** The list of Payments */
    @OneToMany ( cascade = CascadeType.ALL )
    private List<Payment> payments;

    /**
     * Empty constructor for Hibernate
     */
    public Bill () {

    }

    /**
     * Bill constructor
     */
    public Bill ( final User patient, final User hcp, final List<CPTCode> codes ) {
        setPatient( patient );
        setHcp( hcp );
        setCptCodes( codes );
        int cost = 0;
        for ( int i = 0; i < codes.size(); i++ ) {
            cost += codes.get( i ).getCost();
        }
        setCost( cost );
        setStatus( "Unpaid" );
        setDate( ZonedDateTime.now() );
        payments = new ArrayList<Payment>();
    }

    /**
     * method to pay bill
     *
     * @param pay
     *            amount being paid
     * @return true if payment occurs
     */
    public Boolean pay ( final Payment pay ) {
        if ( !status.equals( "Fully Paid" ) && pay.getAmount() <= cost ) {
            payments.add( pay );
            cost -= pay.getAmount();
            if ( cost == 0 ) {
                setStatus( "Fully Paid" );
            }
            else {
                setStatus( "Partially Paid" );
            }
            return true;
        }
        return false;
    }

    /**
     * Gets the id of the Bill
     *
     * @return The id of the Bill
     *
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Sets the ID of the Bill
     *
     * @param id
     *            The new ID of the Code. For Hibernate.
     */
    public void setId ( final Long id ) {
        this.id = id;
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
     * @return the hcp
     */
    public User getHcp () {
        return hcp;
    }

    /**
     * @param hcp
     *            the hcp to set
     */
    public void setHcp ( final User hcp ) {
        this.hcp = hcp;
    }

    /**
     * @return the date
     */
    public ZonedDateTime getDate () {
        return date;
    }

    /**
     * @param date
     *            the date to set
     */
    public void setDate ( final ZonedDateTime date ) {
        this.date = date;
    }

    /**
     * @return the cptCodes
     */
    public List<CPTCode> getCptCodes () {
        return cptCodes;
    }

    /**
     * @param cptCodes
     *            the cptCodes to set
     */
    public void setCptCodes ( final List<CPTCode> cptCodes ) {
        this.cptCodes = cptCodes;
    }

    /**
     * @return the cost
     */
    public Integer getCost () {
        return cost;
    }

    /**
     * @param cost
     *            the cost to set
     */
    public void setCost ( final Integer cost ) {
        this.cost = cost;
    }

    /**
     * @return the status
     */
    public String getStatus () {
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus ( final String status ) {
        this.status = status;
    }

    /**
     * @return the payments
     */
    public List<Payment> getPayments () {
        return payments;
    }

    /**
     * @param payments
     *            the payments to set
     */
    public void setPayments ( final List<Payment> payments ) {
        this.payments = payments;
    }

}
