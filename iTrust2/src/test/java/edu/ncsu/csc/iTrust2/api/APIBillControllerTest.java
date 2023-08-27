package edu.ncsu.csc.iTrust2.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

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

import edu.ncsu.csc.iTrust2.forms.UserForm;
import edu.ncsu.csc.iTrust2.models.Bill;
import edu.ncsu.csc.iTrust2.models.CPTCode;
import edu.ncsu.csc.iTrust2.models.Patient;
import edu.ncsu.csc.iTrust2.models.Personnel;
import edu.ncsu.csc.iTrust2.models.enums.Role;
import edu.ncsu.csc.iTrust2.services.BillService;
import edu.ncsu.csc.iTrust2.services.CPTCodeService;
import edu.ncsu.csc.iTrust2.services.PatientService;
import edu.ncsu.csc.iTrust2.services.PersonnelService;

@RunWith ( SpringRunner.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APIBillControllerTest {

    private MockMvc                 mvc;

    @Autowired
    private WebApplicationContext   context;

    @Autowired
    private BillService             billService;

    @Autowired
    private PatientService<Patient> patientService;

    @Autowired
    private PersonnelService        personnelService;

    @Autowired
    private CPTCodeService          codeService;

    /**
     * Sets up test
     */
    @Before
    @WithMockUser ( username = "admin", roles = { "USER", "ADMIN" } )
    public void setup () throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
    }

    @SuppressWarnings ( { "unchecked", "deprecation" } )
    @Test
    @Transactional
    @WithMockUser ( username = "billing staff member", roles = { "USER", "BSM" } )
    public void testRetrievingBills () throws Exception {

        patientService.save( new Patient( new UserForm( "patient", "123456", Role.ROLE_PATIENT, 1 ) ) );
        personnelService.save( new Personnel( new UserForm( "hcp", "654322", Role.ROLE_HCP, 1 ) ) );
        final Patient pat1 = patientService.findAll().get( 0 );
        final Personnel per2 = personnelService.findAll().get( 0 );

        final CPTCode cost1 = new CPTCode();
        cost1.setCode( "00363" );
        cost1.setDescription( "Bandaid" );
        cost1.setId( 1L );
        cost1.setCost( 10 );

        final CPTCode cost2 = new CPTCode();
        cost2.setCode( "00364" );
        cost2.setDescription( "neosporin" );
        cost2.setId( 2L );
        cost2.setCost( 15 );

        final CPTCode cost3 = new CPTCode();
        cost3.setCode( "00365" );
        cost3.setDescription( "Application" );
        cost3.setId( 3L );
        cost3.setCost( 10 );

        codeService.save( cost1 );
        codeService.save( cost2 );
        codeService.save( cost3 );

        final List<CPTCode> codes = codeService.findAll();

        // make a bill, store it in the database
        final Bill billClyde = new Bill( pat1, per2, codes ); // missing "clyde,
                                                              // billingStaff,
        // codes"
        // for now
        billService.save( billClyde );

        // now retrieve all bills
        final String content = mvc.perform( get( "/api/v1/bills/" ).contentType( MediaType.APPLICATION_JSON ) )
                .andReturn().getResponse().getContentAsString();

        // now make another patient

        // give them a bill

        // store said bill in the database

        // retrieve all bills, make sure bills from both patients are included

        // retrieve bill from one patient
    }

}
