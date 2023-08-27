package edu.ncsu.csc.iTrust2.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.ncsu.csc.iTrust2.TestConfig;
import edu.ncsu.csc.iTrust2.forms.CPTCodeForm;
import edu.ncsu.csc.iTrust2.models.CPTCode;

/**
 * Class to test that CPTCode and CPTCodeForms are created from each other
 * properly.
 *
 * @author Colin Scanlon
 *
 */
@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class CPTCodeTest {

    @Test
    @Transactional
    public void testCodes () {
        final CPTCodeForm form = new CPTCodeForm();
        form.setId( 1L );
        form.setCode( "00363" );
        form.setDescription( "Testing" );
        form.setCost( 1 );
        final CPTCode base = new CPTCode();
        base.setCode( "00363" );
        base.setDescription( "Testing" );
        base.setId( 1L );
        base.setCost( 1 );

        final CPTCode code = new CPTCode( form );
        assertEquals( code, base );

        final CPTCodeForm f2 = new CPTCodeForm( code );
        assertEquals( form, f2 );

        assertEquals( "00363", code.getCode() );
        assertTrue( code.getId().equals( 1L ) );
        assertEquals( "Testing", code.getDescription() );
        assertEquals( 1, (int) code.getCost() );

    }

    @Test
    @Transactional
    public void testCreateCodeFromForm () {
        final CPTCodeForm cost1 = new CPTCodeForm();
        cost1.setCode( "99202" );
        cost1.setDescription( "Bandaid" );
        cost1.setId( 1L );
        cost1.setCost( 10 );
        cost1.setArchive( false );

        final CPTCode code = new CPTCode( cost1 );
        assertEquals( "99202", code.getCode() );
        assertEquals( "Bandaid", code.getDescription() );
        assertEquals( (Integer) 10, code.getCost() );
        assertFalse( code.getArchive() );
    }

    @Test
    @Transactional
    public void testInvalidCodes () {
        final CPTCodeForm form = new CPTCodeForm();
        form.setCode( "111110" );
        form.setDescription( "Invalid" );
        form.setCost( 1 );

        @SuppressWarnings ( "unused" ) // we want to do an assignment so that
                                       // the compiler doesn't optimize it away
        CPTCode code;
        try {
            code = new CPTCode( form );
            fail();
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "Code not 5 digits: 111110", e.getMessage() );
        }
        form.setCode( "11111" );
        // valid
        code = new CPTCode( form );

    }

}
