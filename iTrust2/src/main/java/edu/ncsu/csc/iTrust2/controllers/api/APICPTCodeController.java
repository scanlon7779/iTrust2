package edu.ncsu.csc.iTrust2.controllers.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.iTrust2.forms.CPTCodeForm;
import edu.ncsu.csc.iTrust2.models.CPTCode;
import edu.ncsu.csc.iTrust2.models.enums.TransactionType;
import edu.ncsu.csc.iTrust2.services.CPTCodeService;
import edu.ncsu.csc.iTrust2.utils.LoggerUtil;

/**
 * Class that provided the REST endpoints for dealing with CPT Code creation,
 * deletion, updating, and viewing.
 *
 * @author Jonathan Buck
 *
 */
@RestController
@SuppressWarnings ( { "unchecked", "rawtypes" } )
public class APICPTCodeController extends APIController {
    /** LoggerUtil */
    @Autowired
    private LoggerUtil     loggerUtil;

    /** CPTCode service */
    @Autowired
    private CPTCodeService service;

    /**
     * Adds a new CPT Code to the active CPT code list
     *
     * @param form
     *            the data for the new CPT code
     * @return the result of the API call
     */
    @PostMapping ( BASE_PATH + "/cptcode/add" )
    @PreAuthorize ( "hasRole('ROLE_BSM')" )
    public ResponseEntity createCode ( @RequestBody final CPTCodeForm form ) {
        try {
            if ( null != service.findByCode( form.getCode() ) ) {
                return new ResponseEntity(
                        errorResponse( "CPT Code with the code " + form.getCode() + " already exists" ),
                        HttpStatus.CONFLICT );
            }
            final CPTCode code = new CPTCode( form );
            service.save( code );
            loggerUtil.log( TransactionType.CPT_CODE_ADDED, LoggerUtil.currentUser(),
                    LoggerUtil.currentUser() + "added a CPT code" );
            return new ResponseEntity( code, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity(
                    errorResponse( "Could not create CPT Code " + form.getCode() + " because of " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }

    }

    /**
     * Updates a CPT Code in the active CPT code list with a given ID and adds
     * the old version to the CPT code archive
     *
     * @param id
     *            the ID of the CPT code to edit
     * @param form
     *            the data for the new CPT code
     * @return the result of the API call
     */
    @PutMapping ( BASE_PATH + "/cptcode/{cptcode}" )
    @PreAuthorize ( "hasRole('ROLE_BSM')" )
    public ResponseEntity editCode ( @PathVariable ( "cptcode" ) final String cptcode,
            @RequestBody final CPTCodeForm form ) {
        try {
            if ( null == service.findByCode( cptcode ) ) {
                return new ResponseEntity( "No code with id " + cptcode, HttpStatus.NOT_FOUND );
            }
            if ( !form.getCode().equals( cptcode ) ) {
                return new ResponseEntity(
                        errorResponse(
                                "Could not update CPT Code " + cptcode + " because of mismatched form cpt code" ),
                        HttpStatus.BAD_REQUEST );
            }
            final CPTCode archive = new CPTCode( service.findByCode( cptcode ), false );
            service.save( archive );
            CPTCode code = service.findByCode( cptcode );
            form.setId( code.getId() );
            code = new CPTCode( form );
            service.save( code );
            loggerUtil.log( TransactionType.CPT_CODE_EDITED, LoggerUtil.currentUser(),
                    LoggerUtil.currentUser() + "edited a CPT code" );
            return new ResponseEntity( code, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity(
                    errorResponse( "Could not update CPT Code " + cptcode + " because of " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Deletes a CPT Code from the active CPT code list with a given ID and adds
     * it to the CPT code archive
     *
     * @param id
     *            the ID of the CPT code to edit
     * @return the result of the API call
     */
    @DeleteMapping ( BASE_PATH + "/cptcode/{cptcode}" )
    @PreAuthorize ( "hasRole('ROLE_BSM')" )
    public ResponseEntity deleteCode ( @PathVariable ( "cptcode" ) final String cptcode ) {
        try {
            final CPTCode archive = new CPTCode( service.findByCode( cptcode ), true );
            service.save( archive );
            final CPTCode code = service.findByCode( cptcode );
            service.delete( code );
            loggerUtil.log( TransactionType.CPT_CODE_DELETED, LoggerUtil.currentUser(),
                    LoggerUtil.currentUser() + "deleted a CPT code" );

            return new ResponseEntity( HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity(
                    errorResponse( "Could not delete ICD Code " + cptcode + " because of " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Returns a single CPT code by ID
     *
     * @param id
     *            the ID of the CPT code to return
     * @return the result of the API call
     */
    @GetMapping ( BASE_PATH + "/cptcode/{cptcode}" )
    @PreAuthorize ( "hasRole('ROLE_BSM')" )
    public ResponseEntity getCodeByCode ( @PathVariable ( "cptcode" ) final String cptcode ) {
        try {
            final CPTCode code = service.findByCode( cptcode );
            if ( code == null ) {
                return new ResponseEntity( errorResponse( "No code numbered: " + cptcode ), HttpStatus.NOT_FOUND );
            }
            loggerUtil.log( TransactionType.CPT_CODE_VIEWED, LoggerUtil.currentUser(),
                    "Viewed CPT code with id" + cptcode );
            return new ResponseEntity( code, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity(
                    errorResponse( "Could not retrieve CPT Code " + cptcode + " because of " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Returns the entire list of active CPT codes
     *
     * @return the result of the API call
     */
    @GetMapping ( BASE_PATH + "/cptcode" )
    public List<CPTCode> getCodes () {
        loggerUtil.log( TransactionType.LIST_ACTIVE_CPT_CODES, LoggerUtil.currentUser(), "List all active CPT codes" );
        return service.findAllActive();

    }

    /**
     * Returns the entire list of archived CPT codes
     *
     * @return the result of the API call
     */
    @GetMapping ( BASE_PATH + "/cptcode/history" )
    @PreAuthorize ( "hasRole('ROLE_BSM')" )
    public List<CPTCode> getCodeHistory () {
        loggerUtil.log( TransactionType.LIST_ARCHIVED_CPT_CODES, LoggerUtil.currentUser(),
                "List all history CPT codes" );
        return service.findAllArchive();

    }

}
