package edu.ncsu.csc.iTrust2.services;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.iTrust2.forms.VaccineVisitForm;
import edu.ncsu.csc.iTrust2.models.CPTCode;
import edu.ncsu.csc.iTrust2.models.Patient;
import edu.ncsu.csc.iTrust2.models.User;
import edu.ncsu.csc.iTrust2.models.VaccineAppointmentRequest;
import edu.ncsu.csc.iTrust2.models.VaccineVisit;
import edu.ncsu.csc.iTrust2.repositories.VaccineVisitRepository;

/**
 * Service class for interacting with VaccineVisit model, performing CRUD tasks
 * with database and building a persistence object from a Form.
 *
 * @author accline2
 * @author mjcheim
 */
@Component
@Transactional
public class VaccineVisitService extends Service<VaccineVisit, Long> {

    /** Repository for CRUD tasks */
    @Autowired
    private VaccineVisitRepository           repository;

    /** UserService for CRUD operations on User */
    @Autowired
    private UserService<User>                userService;

    /**
     * VaccineAppointmentRequest Service
     */
    @Autowired
    private VaccineAppointmentRequestService vaxService;

    /** Repository to check Vaccine Types */
    @Autowired
    private VaccineTypeService               vaccineService;

    /** service for cpt codes */
    @Autowired
    private CPTCodeService                   cptCodeService;

    @Override
    protected JpaRepository<VaccineVisit, Long> getRepository () {
        return repository;
    }

    /**
     * Find all appointment requests for a given Patient
     *
     * @param patient
     *            Patient for lookups
     * @return Matching requests
     */
    public List<VaccineVisit> findByPatient ( final User patient ) {
        return repository.findByPatient( patient );
    }

    /**
     * Find all appointment requests for a given HCP
     *
     * @param hcp
     *            HCP for lookups
     * @return Matching requests
     */
    public List<VaccineVisit> findByVaccinator ( final User hcp ) {
        return repository.findByVaccinator( hcp );
    }

    /**
     * Find all appointment requests for a given HCP and patient
     *
     * @param hcp
     *            HCP for lookups
     * @param patient
     *            Patient for lookups
     * @return Matching requests
     */
    public List<VaccineVisit> findByVaccinatorAndPatient ( final User hcp, final User patient ) {
        return repository.findByVaccinatorAndPatient( hcp, patient );
    }

    /**
     * Builds a VaccineVisit from a VaccineVisitForm
     *
     * @param varf
     *            form to build
     * @return built VaccineVisit
     */
    public VaccineVisit build ( final VaccineVisitForm varf ) {
        final VaccineVisit request = new VaccineVisit();

        request.setPatient( (Patient) userService.findByName( varf.getPatient() ) );

        if ( ( varf.getVaccinator() != null && !varf.getVaccinator().isEmpty() ) ) {
            request.setVaccinator( userService.findByName( varf.getVaccinator() ) );
        }
        else {
            throw new IllegalArgumentException( "Invalid Vaccinator." );
        }

        final ZonedDateTime requestDate = ZonedDateTime.parse( varf.getDateTime() );
        request.setDate( requestDate );

        request.setDose( Integer.parseInt( varf.getDose() ) );

        if ( !varf.getRequestId().equals( "" ) ) {
            final VaccineAppointmentRequest corRequest = vaxService.findById( Long.parseLong( varf.getRequestId() ) );
            request.setCorrespondingRequest( corRequest );
        }
        else {
            request.setCorrespondingRequest( null );
        }

        request.setVaccineType( vaccineService.findByName( varf.getVaccine() ) );

        if ( request.getDose() < request.getVaccineType().getNumDoses() ) {
            if ( !varf.getFollowUpDate().equals( "" ) ) {
                request.setFollowUpDate( ZonedDateTime.parse( varf.getFollowUpDate() ) );
            }
        }

        if ( varf.getCPTCodes() != null ) {
            final List<CPTCode> codes = new ArrayList<CPTCode>();

            request.setCPTCodes(
                    varf.getCPTCodes().stream().map( cptCodeService::build ).collect( Collectors.toList() ) );
        }

        return request;
    }

}
