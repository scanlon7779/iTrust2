package edu.ncsu.csc.iTrust2.forms;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import edu.ncsu.csc.iTrust2.models.Bill;
import edu.ncsu.csc.iTrust2.models.CPTCode;

/**
 * The Bill Form class
 *
 * @author colinscanlon
 *
 */
public class BillForm implements Serializable {
    /**
     * Serial Version of the Form. For the Serializable
     */
    private static final long serialVersionUID = 1L;

    /**
     * Name of the Patient involved in the OfficeVisit
     */
    @NotEmpty
    private String            patient;

    /**
     * Name of the HCP involved in the OfficeVisit
     */
    @NotEmpty
    private String            hcp;

    /**
     * Date at which the OfficeVisit occurred
     */
    @NotEmpty
    private String            date;

    /**
     * ID of the OfficeVisit
     */
    private String            id;

    /**
     * Type of the OfficeVisit.
     */
    @NotEmpty
    private String            status;

    /**
     * List of all CPTCodes for an office visit
     */
    private List<CPTCode>     cptCodes;

    /**
     * The remaining cost of the bill
     */
    private String            cost;

    /**
     * Empty hibernate constructor
     */
    public BillForm () {

    }

    /**
     * Constructor for building a form from a bill
     *
     * @param bill
     *            the bill
     */
    public BillForm ( final Bill bill ) {
        setPatient( bill.getPatient().getUsername() );
        setHcp( bill.getHcp().getUsername() );
        setDate( bill.getDate().toString() );
        setId( bill.getId().toString() );
        setCptCodes( bill.getCptCodes() );
        setCost( bill.getCost().toString() );

    }

    /**
     * Gets the patient
     *
     * @return the patient
     */
    public String getPatient () {
        return patient;
    }

    /**
     * sets the patient
     *
     * @param patient
     *            the patient to set
     */
    public void setPatient ( final String patient ) {
        this.patient = patient;
    }

    /**
     * gets the hcp
     *
     * @return the hcp
     */
    public String getHcp () {
        return hcp;
    }

    /**
     * sets the hcp
     *
     * @param hcp
     *            the hcp to set
     */
    public void setHcp ( final String hcp ) {
        this.hcp = hcp;
    }

    /**
     * gets the date
     *
     * @return the date
     */
    public String getDate () {
        return date;
    }

    /**
     * Sets the date
     *
     * @param date
     *            the date to set
     */
    public void setDate ( final String date ) {
        this.date = date;
    }

    /**
     * Gets the id
     *
     * @return the id
     */
    public String getId () {
        return id;
    }

    /**
     * sets the ID
     *
     * @param id
     *            the id to set
     */
    public void setId ( final String id ) {
        this.id = id;
    }

    /**
     * gets the status
     *
     * @return the status
     */
    public String getStatus () {
        return status;
    }

    /**
     * sets the status
     *
     * @param status
     *            the status to set
     */
    public void setStatus ( final String status ) {
        this.status = status;
    }

    /**
     * gets the cpt codes
     *
     * @return the cptCodes
     */
    public List<CPTCode> getCptCodes () {
        return cptCodes;
    }

    /**
     * sets the cptcodes
     *
     * @param cptCodes
     *            the cptCodes to set
     */
    public void setCptCodes ( final List<CPTCode> cptCodes ) {
        this.cptCodes = cptCodes;
    }

    /**
     * gets the cost
     *
     * @return the cost
     */
    public String getCost () {
        return cost;
    }

    /**
     * sets the cost
     *
     * @param cost
     *            the cost to set
     */
    public void setCost ( final String cost ) {
        this.cost = cost;
    }

}
