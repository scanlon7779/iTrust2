package edu.ncsu.csc.iTrust2.controllers.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.iTrust2.models.Bill;
import edu.ncsu.csc.iTrust2.models.Patient;
import edu.ncsu.csc.iTrust2.models.Payment;
import edu.ncsu.csc.iTrust2.models.enums.TransactionType;
import edu.ncsu.csc.iTrust2.services.BillService;
import edu.ncsu.csc.iTrust2.services.PatientService;
import edu.ncsu.csc.iTrust2.utils.LoggerUtil;

/**
 * Class that provided the REST endpoints for dealing with Bills, deletion,
 * updating, and viewing.
 *
 * @author Jonathan Buck
 *
 */
@RestController
@SuppressWarnings ( { "unchecked", "rawtypes" } )
public class APIBillController extends APIController {
    /** LoggerUtil */
    @Autowired
    private LoggerUtil     loggerUtil;

    /** Bill service */
    @Autowired
    private BillService    billService;

    /** Patient service */
    @Autowired
    private PatientService patientService;

    /**
     * Returns a particular bill
     *
     * @param username
     *            the name of the patient
     * @return the result of the API call
     */
    @GetMapping ( BASE_PATH + "/bills/{id}" )
    public ResponseEntity getBillsbyId ( @PathVariable ( "id" ) final Long id ) {

        // logging right away

        // the bill in question
        final Bill bill = billService.findById( id );
        if ( bill == null ) {
            return new ResponseEntity( errorResponse( "Bill doesn't exist" ), HttpStatus.NOT_FOUND );
        }

        // otherwise
        loggerUtil.log( TransactionType.VIEW_BILL, LoggerUtil.currentUser(), "bill viewed" );
        return new ResponseEntity( bill, HttpStatus.OK );
    }

    /**
     * Returns the bills associated with
     *
     * @param username
     *            the name of the patient
     * @return the result of the API call
     */
    @GetMapping ( BASE_PATH + "/bills/patient" )
    public List<Bill> getBillsbyPatient () {

        // logging right away
        loggerUtil.log( TransactionType.LIST_BILLS_BY, LoggerUtil.currentUser(), "Patient bills viewed" );

        // the patient in question
        final Patient patient = (Patient) patientService.findByName( LoggerUtil.currentUser() );
        if ( patient == null ) {
            // return an empty array list
            return new ArrayList<Bill>();
        }

        // otherwise
        final List<Bill> patientBills = new ArrayList<Bill>();
        final List<Bill> allBills = billService.findAll(); // all the bills

        for ( int i = 0; i < allBills.size(); i++ ) {
            if ( patient.equals( allBills.get( i ).getPatient() ) ) {
                patientBills.add( allBills.get( i ) );
            }
        }

        // coallesced, so we can return
        return patientBills;

    }

    /**
     * Returns the entire repository of bills: paid, unpaid and delinquent alike
     *
     * @return the result of the API call
     */
    @GetMapping ( BASE_PATH + "/bills/" )
    @PreAuthorize ( "hasRole('ROLE_BSM')" )
    public List<Bill> getBills () {
        loggerUtil.log( TransactionType.LIST_ALL_BILLS, LoggerUtil.currentUser(), "List all bills" );
        return billService.findAll();
    }

    /**
     * Returns a particular bill
     *
     * @param username
     *            the name of the patient
     * @return the result of the API call
     * 
     * @PutMapping ( BASE_PATH + "/bills/{id}" )
     * @PreAuthorize ( "hasRole('ROLE_BSM')" ) public ResponseEntity editBill
     *               ( @PathVariable ( "id" ) final Long id, @RequestBody final
     *               Bill bill ) {
     * 
     *               // the bill in question final Bill billToChange =
     *               billService.findById( id );
     * 
     *               if ( billToChange == null ) { return new ResponseEntity(
     *               errorResponse( "Bill doesn't exist" ), HttpStatus.NOT_FOUND
     *               ); }
     * 
     *               billService.delete( billToChange ); billService.save( bill
     *               ); // update the database
     * 
     *               // otherwise loggerUtil.log( TransactionType.EDIT_BILLS,
     *               LoggerUtil.currentUser(), "bill edited" ); return new
     *               ResponseEntity( bill, HttpStatus.OK ); }
     */

    @PutMapping ( BASE_PATH + "/bills/pay/{id}" )
    @PreAuthorize ( "hasRole('ROLE_BSM')" )
    public ResponseEntity payBill ( @PathVariable ( "id" ) final Long id, @RequestBody final Payment payment ) {
        final Bill bill = billService.findById( id );
        if ( bill == null ) {
            return new ResponseEntity( errorResponse( "Bill doesn't exist" ), HttpStatus.NOT_FOUND );
        }
        bill.pay( payment );
        billService.save( bill );
        loggerUtil.log( TransactionType.EDIT_BILLS, LoggerUtil.currentUser(), "payment made" );
        return new ResponseEntity( bill, HttpStatus.OK );
    }

}
