package edu.ncsu.csc.iTrust2.api;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.ZonedDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.iTrust2.common.TestUtils;
import edu.ncsu.csc.iTrust2.controllers.api.APIVaccinationCertificateController.Certificate;
import edu.ncsu.csc.iTrust2.forms.UserForm;
import edu.ncsu.csc.iTrust2.models.Patient;
import edu.ncsu.csc.iTrust2.models.Personnel;
import edu.ncsu.csc.iTrust2.models.User;
import edu.ncsu.csc.iTrust2.models.VaccineAppointmentRequest;
import edu.ncsu.csc.iTrust2.models.VaccineType;
import edu.ncsu.csc.iTrust2.models.VaccineVisit;
import edu.ncsu.csc.iTrust2.models.enums.AppointmentType;
import edu.ncsu.csc.iTrust2.models.enums.Role;
import edu.ncsu.csc.iTrust2.models.enums.Status;
import edu.ncsu.csc.iTrust2.services.UserService;
import edu.ncsu.csc.iTrust2.services.VaccineAppointmentRequestService;
import edu.ncsu.csc.iTrust2.services.VaccineTypeService;
import edu.ncsu.csc.iTrust2.services.VaccineVisitService;

/**
 * Testing the VaccinationCertificateAPI class
 *
 * @author Subhashni Kadhiresan (skadhir)
 *
 */
@RunWith ( SpringRunner.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APIVaccinationCertificateTest {

    private MockMvc                          mvc;

    @Autowired
    private WebApplicationContext            context;

    /** User service */
    @Autowired
    private UserService<User>                userService;

    @Autowired
    private VaccineTypeService               vaccineService;

    @Autowired
    private VaccineVisitService              visitService;

    @Autowired
    private VaccineAppointmentRequestService reqService;

    /**
     * Sets up tests
     */
    @Before
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
        userService.deleteAll();
        vaccineService.deleteAll();
        visitService.deleteAll();

        final User patient = new Patient( new UserForm( "patient", "123456", Role.ROLE_PATIENT, 1 ) );

        final User hcp = new Personnel( new UserForm( "hcp", "123456", Role.ROLE_HCP, 1 ) );

        final User vaccinator = new Personnel( new UserForm( "vaccinator", "123456", Role.ROLE_VACCINATOR, 1 ) );

        final VaccineType vaccine = new VaccineType();
        vaccine.setName( "Moderna" );
        vaccine.setNumDoses( 2 );
        vaccine.setIsAvailable( true );

        userService.saveAll( List.of( patient, hcp, vaccinator ) );
        vaccineService.save( vaccine );
        final VaccineAppointmentRequest app = new VaccineAppointmentRequest();

        app.setDate( ZonedDateTime.parse( "2021-11-19T04:50:00.000-05:00" ) );
        app.setType( AppointmentType.GENERAL_CHECKUP );
        app.setStatus( Status.APPROVED );
        app.setHcp( userService.findByName( "hcp" ) );
        app.setPatient( userService.findByName( "patient" ) );
        app.setComments( "Test appointment please ignore" );
        app.setVaccine( vaccineService.findByName( "Moderna" ) );

        reqService.save( app );

        final VaccineVisit visit = new VaccineVisit();
        visit.setDose( 2 );
        visit.setPatient( (Patient) patient );
        visit.setVaccinator( vaccinator );
        visit.setVaccineType( vaccine );
        visit.setCorrespondingRequest( app );
        visit.setDate( app.getDate() );

        visitService.save( visit );

    }

    /**
     * Tests VaccinationCertificateAPI
     *
     * @throws Exception
     */
    @Test
    @WithMockUser ( username = "patient", roles = { "PATIENT" } )
    @Transactional
    public void testVaccinationCertificationAPIOneVaccine () throws Exception {

        final ResultActions content1 = mvc.perform( get( "/api/v1/vaccinationcertificate" ) )
                .andExpect( status().isOk() ).andExpect( content().contentType( MediaType.APPLICATION_JSON ) );

        final String content = content1.andReturn().getResponse().getContentAsString();
        final Certificate certElement = TestUtils.gson().fromJson( content, Certificate.class );
        assertEquals( "2021-11-19T04:50-05:00", certElement.getCelist().get( 0 ).getDateTime() );
        // Null because Patient doesn't officially have first and last name
        assertEquals( "1", certElement.getCelist().get( 0 ).getNumDoses() );
        assertEquals( "vaccinator", certElement.getCelist().get( 0 ).getVaccinator() );
        assertEquals( "Moderna", certElement.getCelist().get( 0 ).getVaccineType() );
        assertEquals( 1, certElement.getCelist().size() );

    }

    /**
     * Tests VaccinationCertificateAPI
     *
     * @throws Exception
     */
    @Test
    @WithMockUser ( username = "patient", roles = { "PATIENT" } )
    @Transactional
    public void testVaccinationCertificationAPITwoVaccines () throws Exception {

        final User patient = new Patient( new UserForm( "patient", "123456", Role.ROLE_PATIENT, 1 ) );

        final User vaccinator = new Personnel( new UserForm( "vaccinator", "123456", Role.ROLE_VACCINATOR, 1 ) );

        final VaccineType vaccine = new VaccineType();
        vaccine.setName( "Moderna" );
        vaccine.setNumDoses( 2 );
        vaccine.setIsAvailable( true );

        final VaccineAppointmentRequest app = new VaccineAppointmentRequest();

        app.setDate( ZonedDateTime.parse( "2021-12-19T04:50:00.000-05:00" ) );
        app.setType( AppointmentType.GENERAL_CHECKUP );
        app.setStatus( Status.APPROVED );
        app.setHcp( userService.findByName( "hcp" ) );
        app.setPatient( userService.findByName( "patient" ) );
        app.setComments( "Test appointment please ignore" );
        app.setVaccine( vaccineService.findByName( "Moderna" ) );

        reqService.save( app );

        final VaccineVisit visit = new VaccineVisit();
        visit.setPatient( (Patient) patient );
        visit.setVaccinator( vaccinator );
        visit.setVaccineType( vaccineService.findByName( "Moderna" ) );
        visit.setCorrespondingRequest( app );
        visit.setDate( app.getDate() );
        visit.setDose( 2 );

        visitService.save( visit );

        final ResultActions content1 = mvc.perform( get( "/api/v1/vaccinationcertificate" ) )
                .andExpect( status().isOk() ).andExpect( content().contentType( MediaType.APPLICATION_JSON ) );

        final String content = content1.andReturn().getResponse().getContentAsString();
        final Certificate certElement = TestUtils.gson().fromJson( content, Certificate.class );
        assertEquals( "2021-11-19T04:50-05:00", certElement.getCelist().get( 0 ).getDateTime() );

        assertEquals( "1", certElement.getCelist().get( 0 ).getNumDoses() );
        assertEquals( "vaccinator", certElement.getCelist().get( 0 ).getVaccinator() );
        assertEquals( "Moderna", certElement.getCelist().get( 0 ).getVaccineType() );
        assertEquals( 2, certElement.getCelist().size() );
        assertEquals( "2021-12-19T04:50-05:00", certElement.getCelist().get( 1 ).getDateTime() );

        assertEquals( "2", certElement.getCelist().get( 1 ).getNumDoses() );
        assertEquals( "vaccinator", certElement.getCelist().get( 1 ).getVaccinator() );
        assertEquals( "Moderna", certElement.getCelist().get( 1 ).getVaccineType() );

    }

}
