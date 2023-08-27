package edu.ncsu.csc.iTrust2.unit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.ncsu.csc.iTrust2.forms.VaccineTypeForm;
import edu.ncsu.csc.iTrust2.models.VaccineType;

/**
 * Tests the vaccine type model
 *
 */
public class VaccineTypeTest {

    /**
     * Tests creating multiple vaccine types
     */
    @Test
    public void testVaccineType () {
        final VaccineType vt = new VaccineType();
        vt.setName( "Moderna" );
        vt.setId( (long) 123 );
        vt.setMinAge( 5 );
        vt.setNumDoses( 2 );
        vt.setDaysBetweenDoses( 10 );
        // vt.setAvailable( true );
        vt.setInventoryAmount( 1000 );

        assertEquals( vt.getName(), "Moderna" );
        assertEquals( (long) vt.getId(), (long) 123 );
        assertEquals( vt.getMinAge(), 5 );
        assertEquals( vt.getNumDoses(), 2 );
        assertEquals( vt.getDaysBetweenDoses(), 10 );
        // assertEquals( vt.isAvailable(), true );
        assertEquals( vt.getInventoryAmount(), 1000 );

        final VaccineTypeForm form = new VaccineTypeForm( vt );
        assertEquals( vt.getName(), form.getName() );
        assertEquals( (long) vt.getId(), (long) form.getId() );
        assertEquals( vt.getMinAge(), form.getMinAge() );
        assertEquals( vt.getNumDoses(), form.getNumDoses() );
        assertEquals( vt.getDaysBetweenDoses(), form.getDaysBetweenDoses() );
        // assertEquals( vt.isAvailable(), form.isAvailable() );
        assertEquals( vt.getInventoryAmount(), form.getInventoryAmount() );

        final VaccineType vt2 = new VaccineType( form );
        assertEquals( vt.getName(), vt2.getName() );
        assertEquals( (long) vt.getId(), (long) vt2.getId() );
        assertEquals( vt.getMinAge(), vt2.getMinAge() );
        assertEquals( vt.getNumDoses(), vt2.getNumDoses() );
        assertEquals( vt.getDaysBetweenDoses(), vt2.getDaysBetweenDoses() );
        // assertEquals( vt.isAvailable(), vt2.isAvailable() );
        assertEquals( vt.getInventoryAmount(), vt2.getInventoryAmount() );

        // add saving to database

    }
}
