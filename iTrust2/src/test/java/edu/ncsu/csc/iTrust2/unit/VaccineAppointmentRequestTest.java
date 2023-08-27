package edu.ncsu.csc.iTrust2.unit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.ncsu.csc.iTrust2.forms.UserForm;
import edu.ncsu.csc.iTrust2.models.Personnel;
import edu.ncsu.csc.iTrust2.models.User;
import edu.ncsu.csc.iTrust2.models.VaccineAppointmentRequest;
import edu.ncsu.csc.iTrust2.models.VaccineType;
import edu.ncsu.csc.iTrust2.models.enums.Role;

/**
 * Tests the vaccine appointment request model
 *
 */
public class VaccineAppointmentRequestTest {
    /**
     * Tests setting up a correct vaccine appointment request
     */
    @Test
    public void testVaccineAppointmentRequest () {
        final VaccineAppointmentRequest app = new VaccineAppointmentRequest();
        final User user1 = new Personnel( new UserForm( "abc", "123", Role.ROLE_VACCINATOR, 1 ) );
        app.setHcp( user1 );
        final VaccineType vacc = new VaccineType();
        app.setVaccine( vacc );
        assertEquals( app.getHcp(), user1 );
        assertEquals( app.getVaccine(), vacc );
    }
}
