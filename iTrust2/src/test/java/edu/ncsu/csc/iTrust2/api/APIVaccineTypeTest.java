package edu.ncsu.csc.iTrust2.api;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;

import javax.transaction.Transactional;

import org.hamcrest.Matchers;
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

import edu.ncsu.csc.iTrust2.common.TestUtils;
import edu.ncsu.csc.iTrust2.forms.VaccineTypeForm;
import edu.ncsu.csc.iTrust2.models.VaccineType;
import edu.ncsu.csc.iTrust2.services.VaccineTypeService;

/**
 * Class for testing vaccine API.
 *
 * @author Subhashni Kadhiresan
 * @author Meg Penaganti
 *
 */
@RunWith ( SpringRunner.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APIVaccineTypeTest {

    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private VaccineTypeService    service;

    /**
     * Sets up test
     */
    @Before
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
        service.deleteAll();
    }

    /**
     * Tests basic vaccine API functionality.
     *
     * @throws UnsupportedEncodingException
     * @throws Exception
     */
    @Test
    @Transactional
    @WithMockUser ( username = "admin", roles = { "USER", "ADMIN" } )
    public void testVaccineTypeAPI () throws UnsupportedEncodingException, Exception {
        // Create vaccines for testing
        final VaccineTypeForm form1 = new VaccineTypeForm();
        form1.setName( "Pfizer" );
        form1.setNumDoses( 2 );
        form1.setMinAge( 12 );
        form1.setMaxAge( 100 );
        form1.setIsAvailable( true );
        form1.setInventoryAmount( 100 );
        form1.setDaysBetweenDoses( 21 );

        final VaccineTypeForm form2 = new VaccineTypeForm();
        form2.setName( "Moderna" );
        form2.setNumDoses( 2 );
        form2.setMinAge( 16 );
        form2.setMaxAge( 100 );
        form2.setIsAvailable( true );
        form2.setInventoryAmount( 100 );
        form2.setDaysBetweenDoses( 28 );

        // Add vaccine 1 to system
        final String content1 = mvc
                .perform( post( "/api/v1/vaccines" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( form1 ) ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        // Parse response as Vaccine object
        final VaccineType vaccine1 = TestUtils.gson().fromJson( content1, VaccineType.class );
        assertEquals( form1.getDaysBetweenDoses(), vaccine1.getDaysBetweenDoses() );
        assertEquals( form1.getName(), vaccine1.getName() );
        assertEquals( form1.getNumDoses(), vaccine1.getNumDoses() );
        assertEquals( form1.getMinAge(), vaccine1.getMinAge() );
        assertEquals( form1.getMaxAge(), vaccine1.getMaxAge() );
        assertEquals( form1.getInventoryAmount(), vaccine1.getInventoryAmount() );
        // assertEquals( form1.getIsAvailable(), vaccine1.getIsAvailable() );

        // Attempt to add same drug twice
        mvc.perform( post( "/api/v1/vaccines" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( form1 ) ) ).andExpect( status().isConflict() );

        // Add vaccine2 to system
        final String content2 = mvc
                .perform( post( "/api/v1/vaccines" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( form2 ) ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        final VaccineType vaccine2 = TestUtils.gson().fromJson( content2, VaccineType.class );
        assertEquals( form2.getDaysBetweenDoses(), vaccine2.getDaysBetweenDoses() );
        assertEquals( form2.getName(), vaccine2.getName() );
        assertEquals( form2.getNumDoses(), vaccine2.getNumDoses() );
        assertEquals( form2.getMinAge(), vaccine2.getMinAge() );
        assertEquals( form2.getMaxAge(), vaccine2.getMaxAge() );
        assertEquals( form2.getInventoryAmount(), vaccine2.getInventoryAmount() );
        // assertEquals( form2.getIsAvailable(), vaccine2.getIsAvailable() );
        // Verify drugs have been added
        mvc.perform( get( "/api/v1/vaccines" ) ).andExpect( status().isOk() )
                .andExpect( content().string( Matchers.containsString( form1.getName() ) ) )
                .andExpect( content().string( Matchers.containsString( form2.getName() ) ) );

        // Edit first drug's description
        vaccine1.setIsAvailable( false );
        vaccine1.setName( "PfizerEdit" );

        final String editContent = mvc
                .perform( put( "/api/v1/vaccines" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( vaccine1 ) ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        final VaccineType editedVaccine = TestUtils.gson().fromJson( editContent, VaccineType.class );
        assertEquals( editedVaccine.getDaysBetweenDoses(), vaccine1.getDaysBetweenDoses() );
        assertEquals( editedVaccine.getName(), vaccine1.getName() );
        assertEquals( editedVaccine.getNumDoses(), vaccine1.getNumDoses() );
        assertEquals( editedVaccine.getMinAge(), vaccine1.getMinAge() );
        assertEquals( editedVaccine.getMaxAge(), vaccine1.getMaxAge() );
        assertEquals( editedVaccine.getInventoryAmount(), vaccine1.getInventoryAmount() );
        assertEquals( editedVaccine.getIsAvailable(), vaccine1.getIsAvailable() );

        // Attempt invalid edit
        vaccine2.setName( "PfizerEdit" );
        mvc.perform( put( "/api/v1/vaccines" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( vaccine2 ) ) ).andExpect( status().isConflict() );

        // Follow up with valid edit, change name and availability
        vaccine2.setName( "ModernaEdit" );
        vaccine2.setIsAvailable( false );
        mvc.perform( put( "/api/v1/vaccines" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( vaccine2 ) ) ).andExpect( status().isOk() );
        // Edit availability again
        vaccine2.setIsAvailable( true );
        mvc.perform( put( "/api/v1/vaccines" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( vaccine2 ) ) ).andExpect( status().isOk() );

        // Check that we can get one vaccine by id
        mvc.perform( get( "/api/v1/vaccines/" + vaccine1.getId() ) ).andExpect( status().isOk() )
                .andExpect( content().string( Matchers.containsString( form1.getName() ) ) );
        mvc.perform( get( "/api/v1/vaccines/" + vaccine2.getId() ) ).andExpect( status().isOk() )
                .andExpect( content().string( Matchers.containsString( form2.getName() ) ) );
    }

}
