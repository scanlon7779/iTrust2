package edu.ncsu.csc.iTrust2.models;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;

import edu.ncsu.csc.iTrust2.forms.CPTCodeForm;

/**
 * Public class for the CPTCodes
 *
 * @author colinscanlon
 *
 */
@Entity
public class CPTCode extends DomainObject {

    /**
     * ID of this CPTCode
     */
    @Id
    @GeneratedValue ( strategy = GenerationType.AUTO )
    private Long               id;

    /**
     * The code of the CPTCode
     */
    private String             code;

    /**
     * The cost of the CPTCode
     */
    private Integer            cost;

    /**
     * Description of the diagnosis
     */
    private String             description;

    /**
     * Flag for if the code is archived or not
     */
    private boolean            archive;

    /**
     * list of visits associated with cpt code
     */
    @ManyToMany ( mappedBy = "cptCodes" )
    @JsonBackReference
    private List<OfficeVisit>  officeVisits;

    /**
     * list of visits associated with cpt code
     */
    @ManyToMany ( mappedBy = "cptCodes" )
    @JsonBackReference
    private List<VaccineVisit> vaccineVisits;

    /**
     * list of bills associated with cpt codes
     */
    @ManyToMany ( mappedBy = "cptCodes" )
    @JsonBackReference
    private List<Bill>         bills;

    /**
     * Empty constructor for Hibernate
     */
    public CPTCode () {

    }

    /**
     * Construct from a form
     *
     * @param form
     *            The form that validates and sanitizes input
     */
    public CPTCode ( final CPTCodeForm form ) {
        setCode( form.getCode() );
        setDescription( form.getDescription() );
        setId( form.getId() );
        setCost( form.getCost() );
        setArchive( form.getArchive() );

        // validate
        if ( description.length() > 250 ) {
            throw new IllegalArgumentException( "Description too long (250 characters max): " + description );
        }
        if ( !code.matches( "^[0-9]{5}$" ) ) {
            throw new IllegalArgumentException( "Code not 5 digits: " + code );
        }
    }

    /**
     * this function provides functionality for creating archived cpt codes
     *
     * @param code
     *            the cpt code object
     * @param delete
     *            true if code is being deleted false if it is just being edited
     */
    public CPTCode ( final CPTCode code, final Boolean delete ) {
        if ( delete ) {
            setCode( code.getCode() + " REMOVED: "
                    + Instant.ofEpochSecond( Instant.now().getEpochSecond() ).toString() );
        }
        else {
            setCode( code.getCode() + " " + Instant.ofEpochSecond( Instant.now().getEpochSecond() ).toString() );
        }
        setArchive( true );
        setDescription( code.getDescription() );
        setCost( code.getCost() );
    }

    /**
     * Gets the id of the CPTCode
     *
     * @return The id of the CPTCode
     *
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Sets the ID of the Code
     *
     * @param id
     *            The new ID of the Code. For Hibernate.
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Gets the code of the CPTCode
     *
     * @return the code
     */
    public String getCode () {
        return code;
    }

    /**
     * Sets the code
     *
     * @param string
     *            the code to set
     */
    public void setCode ( final String string ) {
        this.code = string;
    }

    /**
     * Gets the CPTCode cost
     *
     * @return the cost
     */
    public Integer getCost () {
        return cost;
    }

    /**
     * Sets the CPTCode cost
     *
     * @param c
     *            the cost to set
     */
    public void setCost ( final Integer c ) {
        this.cost = c;
    }

    /**
     * Gets the CPTCode description
     *
     * @return the description
     */
    public String getDescription () {
        return description;
    }

    /**
     * Sets the CPTCode description
     *
     * @param description
     *            the description to set
     */
    public void setDescription ( final String description ) {
        this.description = description;
    }

    /**
     * Returns if the CPTCode was archived or not
     *
     * @return the archive
     */
    public boolean getArchive () {
        return archive;
    }

    /**
     * getter for office visits
     *
     * @return officevists
     */
    public List<OfficeVisit> getOfficeVisits () {
        return officeVisits;
    }

    /**
     * setter for office visits
     *
     * @param officeVisits
     *            office visits
     */
    public void setOfficeVisits ( final List<OfficeVisit> officeVisits ) {
        this.officeVisits = officeVisits;
    }

    /**
     * Sets if the CPTCode is an archive or not
     *
     * @param archive
     *            the archive to set
     */
    public void setArchive ( final boolean archive ) {
        this.archive = archive;
    }

    /**
     * getter for vaccine visit
     *
     * @return the vaccineVisits
     */
    public List<VaccineVisit> getVaccineVisits () {
        return vaccineVisits;
    }

    /**
     * setter for vaccine visit
     *
     * @param vaccineVisits
     *            the vaccineVisits to set
     */
    public void setVaccineVisits ( final List<VaccineVisit> vaccineVisits ) {
        this.vaccineVisits = vaccineVisits;
    }

    /**
     * getter for bills
     *
     * @return the bills
     */
    public List<Bill> getBills () {
        return bills;
    }

    /**
     * setter for bills
     *
     * @param bills
     *            the bills to set
     */
    public void setBills ( final List<Bill> bills ) {
        this.bills = bills;
    }

    @Override
    public int hashCode () {
        return Objects.hash( code, cost, description, id );
    }

    @Override
    public boolean equals ( final Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final CPTCode other = (CPTCode) obj;
        return Objects.equals( code, other.code ) && Objects.equals( cost, other.cost )
                && Objects.equals( description, other.description ) && Objects.equals( id, other.id );
    }

    @Override
    public String toString () {
        return "CPTCode [id=" + id + ", code=" + code + ", cost=" + cost + ", description=" + description + "]";
    }

}
