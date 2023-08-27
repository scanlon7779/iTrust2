package edu.ncsu.csc.iTrust2.api;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import edu.ncsu.csc.iTrust2.common.TestUtils;
import edu.ncsu.csc.iTrust2.forms.CPTCodeForm;
import edu.ncsu.csc.iTrust2.models.User;
import edu.ncsu.csc.iTrust2.services.CPTCodeService;
import edu.ncsu.csc.iTrust2.services.UserService;

@RunWith ( SpringRunner.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APICPTCodeTest {
    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private CPTCodeService        service;

    @Autowired
    private UserService<User>     userService;

    /**
     * Sets up test
     */
    @Before
    @WithMockUser ( username = "admin", roles = { "USER", "ADMIN" } )
    public void setup () throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
        service.deleteAll();
        userService.deleteAll();
    }

    @Test
    @Transactional
    @WithMockUser ( username = "billing staff member", roles = { "USER", "BSM" } )
    public void testCodeAPI () throws Exception {
        final CPTCodeForm form = new CPTCodeForm();
        form.setCode( "12345" );
        form.setDescription( "Test Code" );
        form.setCost( 1000 );
        form.setArchive( false );

        String content = mvc.perform( post( "/api/v1/cptcode/add" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( form ) ) ).andReturn().getResponse().getContentAsString();
        CPTCodeForm code = TestUtils.gson().fromJson( content, CPTCodeForm.class );
        form.setId( code.getId() ); // fill in the id of the code we just
                                    // created
        assertEquals( form.getCode(), code.getCode() );

        content = mvc.perform( get( "/api/v1/cptcode/" + form.getCode() ).contentType( MediaType.APPLICATION_JSON ) )
                .andReturn().getResponse().getContentAsString();
        code = TestUtils.gson().fromJson( content, CPTCodeForm.class );
        assertEquals( form, code );

        // edit it
        form.setCost( 10000 );
        content = mvc
                .perform( put( "/api/v1/cptcode/" + form.getCode() ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( form ) ) )
                .andDo( print() ).andReturn().getResponse().getContentAsString();
        code = TestUtils.gson().fromJson( content, CPTCodeForm.class );
        assertEquals( form, code );
        content = mvc.perform( get( "/api/v1/cptcode/" + form.getCode() ).contentType( MediaType.APPLICATION_JSON ) )
                .andReturn().getResponse().getContentAsString();
        code = TestUtils.gson().fromJson( content, CPTCodeForm.class );
        assertEquals( form, code );

        // then delete it and check that its gone.
        mvc.perform( delete( "/api/v1/cptcode/" + form.getCode() ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

        assertNull( service.findByCode( form.getCode() ) );
        mvc.perform( get( "/api/v1/cptcode/" + form.getCode() ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isNotFound() );
    }

    @Test
    @Transactional
    @WithMockUser ( username = "billing staff member", roles = { "USER", "BSM" } )
    public void testCodeAPIGetNonArchived () throws Exception {
        CPTCodeForm form = new CPTCodeForm();
        form.setCode( "12345" );
        form.setDescription( "Test Code" );
        form.setCost( 1000 );
        form.setArchive( false );
        String saveContent = mvc.perform( post( "/api/v1/cptcode/add" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( form ) ) ).andReturn().getResponse().getContentAsString();
        assertNotNull( saveContent );

        String getContent = mvc.perform( get( "/api/v1/cptcode" ) ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertNotNull( getContent );

        form = new CPTCodeForm();
        form.setCode( "13137" );
        form.setDescription( "Test Code" );
        form.setCost( 1000 );
        form.setArchive( true );

        saveContent = mvc.perform( post( "/api/v1/cptcode/add" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( form ) ) ).andReturn().getResponse().getContentAsString();
        assertNotNull( saveContent );

        getContent = mvc.perform( get( "/api/v1/cptcode" ) ).andExpect( status().isOk() ).andReturn().getResponse()
                .getContentAsString();
        assertThat( getContent, containsString( "12345" ) );
        assertThat( getContent, not( containsString( "13137" ) ) );
    }

    @Test
    @Transactional
    @WithMockUser ( username = "billing staff member", roles = { "USER", "BSM" } )
    public void testCodeAPIGetArchived () throws Exception {
        CPTCodeForm form = new CPTCodeForm();
        form.setCode( "12345" );
        form.setDescription( "Test Code" );
        form.setCost( 1000 );
        form.setArchive( false );
        String saveContent = mvc.perform( post( "/api/v1/cptcode/add" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( form ) ) ).andReturn().getResponse().getContentAsString();
        assertNotNull( saveContent );

        String getContent = mvc.perform( get( "/api/v1/cptcode" ) ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertNotNull( getContent );

        form = new CPTCodeForm();
        form.setCode( "13137" );
        form.setDescription( "Test Code" );
        form.setCost( 1000 );
        form.setArchive( true );

        saveContent = mvc.perform( post( "/api/v1/cptcode/add" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( form ) ) ).andReturn().getResponse().getContentAsString();
        assertNotNull( saveContent );

        getContent = mvc.perform( get( "/api/v1/cptcode/history" ) ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertThat( getContent, not( containsString( "12345" ) ) );
        assertThat( getContent, containsString( "13137" ) );
    }

    @Test
    @Transactional
    @WithMockUser ( username = "billing staff member", roles = { "USER", "BSM" } )
    public void testCodeAPIArchiving () throws Exception {
        CPTCodeForm form = new CPTCodeForm();
        form.setCode( "12345" );
        form.setDescription( "Test Code" );
        form.setCost( 1000 );
        form.setArchive( false );
        String saveContent = mvc.perform( post( "/api/v1/cptcode/add" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( form ) ) ).andReturn().getResponse().getContentAsString();
        assertNotNull( saveContent );

        String getContent = mvc.perform( get( "/api/v1/cptcode" ) ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertNotNull( getContent );

        form = new CPTCodeForm();
        form.setCode( "13137" );
        form.setDescription( "Test Code" );
        form.setCost( 1000 );
        form.setArchive( false );

        saveContent = mvc.perform( post( "/api/v1/cptcode/add" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( form ) ) ).andReturn().getResponse().getContentAsString();
        assertNotNull( saveContent );

        getContent = mvc.perform( get( "/api/v1/cptcode" ) ).andExpect( status().isOk() ).andReturn().getResponse()
                .getContentAsString();
        assertThat( getContent, containsString( "12345" ) );
        assertThat( getContent, containsString( "13137" ) );

        form.setCost( 200000 );

        final String content = mvc.perform( put( "/api/v1/cptcode/" + form.getCode() )
                .contentType( MediaType.APPLICATION_JSON ).content( TestUtils.asJsonString( form ) ) ).andReturn()
                .getResponse().getContentAsString();
        final CPTCodeForm code = TestUtils.gson().fromJson( content, CPTCodeForm.class );
        assertEquals( form.getCost(), code.getCost() );
        assertEquals( form.getCode(), code.getCode() );

        getContent = mvc.perform( get( "/api/v1/cptcode/history" ) ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertThat( getContent, containsString( "1000" ) );
        assertThat( getContent, containsString( "13137" ) );

        mvc.perform( delete( "/api/v1/cptcode/" + form.getCode() ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

        getContent = mvc.perform( get( "/api/v1/cptcode" ) ).andExpect( status().isOk() ).andReturn().getResponse()
                .getContentAsString();
        assertThat( getContent, containsString( "12345" ) );
        assertThat( getContent, not( containsString( "13137" ) ) );

        getContent = mvc.perform( get( "/api/v1/cptcode/history" ) ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertThat( getContent, containsString( "1000" ) );
        assertThat( getContent, containsString( "13137" ) );
        assertThat( getContent, containsString( "REMOVED" ) );
    }

    @Test
    @Transactional
    @WithMockUser ( username = "billing staff member", roles = { "USER", "BSM" } )
    public void testFailures () throws Exception {
        final CPTCodeForm form = new CPTCodeForm();
        form.setCode( "12345" );
        form.setDescription( "Test Code" );
        form.setCost( 1000 );
        form.setArchive( false );

        String content = mvc.perform( post( "/api/v1/cptcode/add" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( form ) ) ).andReturn().getResponse().getContentAsString();
        CPTCodeForm code = TestUtils.gson().fromJson( content, CPTCodeForm.class );
        form.setId( code.getId() ); // fill in the id of the code we just
                                    // created
        assertEquals( form, code );

        content = mvc.perform( get( "/api/v1/cptcode/" + form.getCode() ).contentType( MediaType.APPLICATION_JSON ) )
                .andReturn().getResponse().getContentAsString();
        code = TestUtils.gson().fromJson( content, CPTCodeForm.class );
        assertEquals( form, code );

        form.setDescription( "Test Code2" );
        form.setCost( 2000 );

        content = mvc
                .perform( post( "/api/v1/cptcode/add" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( form ) ) )
                .andExpect( status().isConflict() ).andReturn().getResponse().getContentAsString();

        form.setCode( "78901" );

        content = mvc
                .perform( put( "/api/v1/cptcode/edit" + form.getCode() ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( form ) ) )
                .andExpect( status().isNotFound() ).andReturn().getResponse().getContentAsString();

        content = mvc
                .perform( put( "/api/v1/cptcode/edit/12332" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( form ) ) )
                .andExpect( status().isNotFound() ).andReturn().getResponse().getContentAsString();
    }

}
