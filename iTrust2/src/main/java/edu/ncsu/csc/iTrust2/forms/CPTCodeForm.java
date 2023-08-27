package edu.ncsu.csc.iTrust2.forms;

import java.util.Objects;

import edu.ncsu.csc.iTrust2.models.CPTCode;

/**
 * Intermediate form for adding or editing CPTCodes. Used to create and
 * serialize CPTCodes.
 *
 * @author Jonathan Buck
 *
 */
public class CPTCodeForm {
    /** The CPT code of the treatment */
    private String  code;
    /** The description of the treatment */
    private String  description;
    /** ID of the CPTCode */
    private Long    id;
    /** the cost of the treatment */
    private Integer cost;
    /** true if cpt code is archived */
    private boolean archive;

    /**
     * Empty constructor
     *
     */
    public CPTCodeForm () {

    }

    /**
     * Construct a form off an existing CPT code
     */
    public CPTCodeForm ( final CPTCode code ) {
        setCode( code.getCode() );
        setDescription( code.getDescription() );
        setId( code.getId() );
        setCost( code.getCost() );
        setArchive( code.getArchive() );
    }

    /**
     * Sets the String representation of the code
     *
     * @param code
     *            The new code
     */
    public void setCode ( final String code ) {
        this.code = code;
    }

    /**
     * Returns the String representation of the code
     *
     * @return The code
     */
    public String getCode () {
        return code;
    }

    /**
     * Sets the description of this code
     *
     * @param d
     *            The new description
     */
    public void setDescription ( final String d ) {
        description = d;
    }

    /**
     * Returns the description of the code
     *
     * @return The description
     */
    public String getDescription () {
        return description;
    }

    /**
     * Sets the cost of this code
     *
     * @param d
     *            The new description
     */
    public void setCost ( final Integer c ) {
        cost = c;
    }

    /**
     * Returns the cost of the code
     *
     * @return The description
     */
    public Integer getCost () {
        return cost;
    }

    /**
     * setter for is archive
     *
     * @param b
     *            true if archived
     */
    public void setArchive ( final boolean b ) {
        archive = b;
    }

    /**
     * getter for is archived
     */
    public Boolean getArchive () {
        return archive;
    }

    /**
     * Sets the ID of the Code
     *
     * @param l
     *            The new ID of the Code. For Hibernate.
     */
    public void setId ( final Long l ) {
        id = l;
    }

    /**
     * Returns the ID of the Diagnosis
     *
     * @return The diagnosis ID
     */
    public Long getId () {
        return id;
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
        final CPTCodeForm other = (CPTCodeForm) obj;
        return Objects.equals( code, other.code ) && Objects.equals( cost, other.cost )
                && Objects.equals( description, other.description ) && Objects.equals( id, other.id );
    }

}
