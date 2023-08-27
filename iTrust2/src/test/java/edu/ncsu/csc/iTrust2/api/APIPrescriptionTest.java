package edu.ncsu.csc.iTrust2.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.reflect.TypeToken;

import edu.ncsu.csc.iTrust2.common.TestUtils;
import edu.ncsu.csc.iTrust2.forms.DrugForm;
import edu.ncsu.csc.iTrust2.forms.PrescriptionForm;
import edu.ncsu.csc.iTrust2.forms.UserForm;
import edu.ncsu.csc.iTrust2.models.Patient;
import edu.ncsu.csc.iTrust2.models.Personnel;
import edu.ncsu.csc.iTrust2.models.Prescription;
import edu.ncsu.csc.iTrust2.models.User;
import edu.ncsu.csc.iTrust2.models.enums.Role;
import edu.ncsu.csc.iTrust2.services.UserService;

/**
 * Class for testing prescription API.
 *
 * @author Connor
 * @author Kai Presler-Marshall
 */

@RunWith ( SpringRunner.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APIPrescriptionTest {
    private MockMvc               mvc;

    private DrugForm              drugForm;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserService<User>     uService;

    /**
     * Performs setup operations for the tests.
     *
     * @throws Exception
     */
    @Before
    public void setup () throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
        final UserForm pTestUser = new UserForm( "hcp", "123456", Role.ROLE_HCP, 1 );
        final UserForm pTestUser2 = new UserForm( "patient", "123456", Role.ROLE_PATIENT, 1 );

        final Personnel p = new Personnel( pTestUser );
        final Patient p2 = new Patient( pTestUser2 );

        uService.save( p );

        uService.save( p2 );

        // Create drug for testing
        drugForm = new DrugForm();
        drugForm.setCode( "0000-0000-20" );
        drugForm.setName( "TEST" );
        drugForm.setDescription( "DESC" );
        mvc.perform( post( "/api/v1/drugs" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( drugForm ) ) );
    }

    /**
     * Tests basic prescription APIs.
     *
     * @throws Exception
     */
    @Test
    @WithMockUser ( username = "hcp", roles = { "USER", "HCP", "ADMIN" } )
    @Transactional
    public void testPrescriptionAPI () throws Exception {

        // Create two prescription forms for testing
        final PrescriptionForm form1 = new PrescriptionForm();
        form1.setDrug( drugForm.getCode() );
        form1.setDosage( 100 );
        form1.setRenewals( 12 );
        form1.setPatient( "patient" );
        form1.setStartDate( "2009-10-10" ); // 10/10/2009
        form1.setEndDate( "2010-10-10" ); // 10/10/2010

        final PrescriptionForm form2 = new PrescriptionForm();
        form2.setDrug( drugForm.getCode() );
        form2.setDosage( 200 );
        form2.setRenewals( 3 );
        form2.setPatient( "patient" );
        form2.setStartDate( "2020-10-10" ); // 10/10/2020
        form2.setEndDate( "2020-11-10" ); // 11/10/2020

        // Add first prescription to system
        final String content1 = mvc
                .perform( post( "/api/v1/prescriptions" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( form1 ) ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        // Parse response as Prescription
        final Prescription p1 = TestUtils.gson().fromJson( content1, Prescription.class );
        final PrescriptionForm p1Form = new PrescriptionForm( p1 );
        assertEquals( form1.getDrug(), p1Form.getDrug() );
        assertEquals( form1.getDosage(), p1Form.getDosage() );
        assertEquals( form1.getRenewals(), p1Form.getRenewals() );
        assertEquals( form1.getPatient(), p1Form.getPatient() );
        assertEquals( form1.getStartDate(), p1Form.getStartDate() );
        assertEquals( form1.getEndDate(), p1Form.getEndDate() );

        // Add second prescription to system
        final String content2 = mvc
                .perform( post( "/api/v1/prescriptions" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( form2 ) ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        final Prescription p2 = TestUtils.gson().fromJson( content2, Prescription.class );
        final PrescriptionForm p2Form = new PrescriptionForm( p1 );
        assertEquals( form1.getDrug(), p2Form.getDrug() );
        assertEquals( form1.getDosage(), p2Form.getDosage() );
        assertEquals( form1.getRenewals(), p2Form.getRenewals() );
        assertEquals( form1.getPatient(), p2Form.getPatient() );
        assertEquals( form1.getStartDate(), p2Form.getStartDate() );
        assertEquals( form1.getEndDate(), p2Form.getEndDate() );

        // Verify prescriptions have been added
        final String allPrescriptionContent = mvc.perform( get( "/api/v1/prescriptions" ) ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        final List<Prescription> allPrescriptions = TestUtils.gson().fromJson( allPrescriptionContent,
                new TypeToken<List<Prescription>>() {
                }.getType() );
        assertTrue( allPrescriptions.size() >= 2 );

        // Edit first prescription
        p1.setDosage( 500 );
        final String editContent = mvc
                .perform( put( "/api/v1/prescriptions" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( new PrescriptionForm( p1 ) ) ) )
                .andReturn().getResponse().getContentAsString();
        final Prescription edited = TestUtils.gson().fromJson( editContent, Prescription.class );
        assertEquals( p1.getId(), edited.getId() );
        assertEquals( p1.getDosage(), edited.getDosage() );

        // Get single prescription
        final String getContent = mvc
                .perform( put( "/api/v1/prescriptions" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( new PrescriptionForm( p1 ) ) ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        final Prescription fetched = TestUtils.gson().fromJson( getContent, Prescription.class );
        assertEquals( p1.getId(), fetched.getId() );

        // Make sure we can delete one of our objects (the other will be trashed
        // in the transaction rollback)
        mvc.perform( delete( "/api/v1/prescriptions/" + p1.getId() ) ).andExpect( status().isOk() )
                .andExpect( content().string( p1.getId().toString() ) );
    }

}
