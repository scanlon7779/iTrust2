package edu.ncsu.csc.iTrust2.models.enums;

/**
 * A TransactionType represents an event that took place in the system and that
 * is to be logged. This is used to provide a code that can easily be saved in
 * the database and a longer description of it that can be displayed to the
 * user. Also stores whether the event is patient-visible.
 *
 * As new functionality is added to iTrust2, add in new TransactionType codes
 * representing the event.
 *
 * @author Kai Presler-Marshall
 * @author Jack MacDonald
 * @author Anthony Cline
 *
 */
public enum TransactionType {

    /**
     * When an HTTP request got intercepted, generic transaction type.
     *
     * <pre>
     *  IT'S OVER 9000!!!!
     * </pre>
     */
    HTTP_REQUEST ( 9001, "HTTP Request was intercepted on the system", false ),

    /**
     * New User created
     */
    CREATE_USER ( 100, "New user created", true ),
    /**
     * User was viewed
     */
    VIEW_USER ( 101, "Single user viewed", false ),
    /**
     * Multiple users viewed
     */
    VIEW_USERS ( 102, "List of users viewed", false ),
    /**
     * User deleted
     */
    DELETE_USER ( 103, "User deleted", false ),
    /**
     * User changed/updated
     */
    UPDATE_USER ( 104, "User updated", false ),

    /**
     * Sample users for login generated
     */
    USERS_GENERATED ( 105, "Sample users generated", false ),

    /**
     * Failed login
     */
    LOGIN_FAILURE ( 201, "Failed login", true ),
    /**
     * Successful login
     */
    LOGIN_SUCCESS ( 202, "Successful login", true ),
    /**
     * User logged out
     */
    LOGOUT ( 203, "Logged Out", true ),
    /**
     * User locked out of system (temporary)
     */
    USER_LOCKOUT ( 204, "User Locked Out", true ),
    /**
     * IP locked out of system (temporary)
     */
    IP_LOCKOUT ( 205, "IP Locked Out", true ),
    /**
     * User banned
     */
    USER_BANNED ( 206, "User Banned", true ),
    /**
     * IP Banned
     */
    IP_BANNED ( 207, "IP Banned", true ),

    /**
     * User viewed their demographics
     */
    VIEW_DEMOGRAPHICS ( 400, "Demographics viewed by user", true ),
    /**
     * User updated their demographics
     */
    EDIT_DEMOGRAPHICS ( 410, "Demographics edited by user", true ),

    /**
     * User creates their demographics
     */
    CREATE_DEMOGRAPHICS ( 411, "Demographics created by user", true ),

    /**
     * Hospital created
     */
    CREATE_HOSPITAL ( 500, "New hospital created", false ),
    /**
     * Hospital viewed by user
     */
    VIEW_HOSPITAL ( 501, "Hospital viewed", false ),
    /**
     * Hospital modified by user
     */
    EDIT_HOSPITAL ( 502, "Hospital edited", false ),
    /**
     * Hospital deleted
     */
    DELETE_HOSPITAL ( 503, "Hospital deleted", false ),

    /**
     * Upcoming appointment viewed by Patient or HCP
     */
    VIEW_SCHEDULED_APPOINTMENT ( 611, "Upcoming general checkup viewed", true ),
    /**
     * AppointmentRequest submitted by patient
     */
    APPOINTMENT_REQUEST_SUBMITTED ( 640, "General checkup requested by patient", true ),
    /**
     * AppointmentRequest viewed
     */
    APPOINTMENT_REQUEST_VIEWED ( 641, "Appointment request(s) viewed", true ),
    /**
     * AppointmentRequest canceled/deleted by patient
     */
    APPOINTMENT_REQUEST_DELETED ( 642, "General checkup request deleted by patient", true ),
    /**
     * AppointmentRequest approved by HCP
     */
    APPOINTMENT_REQUEST_APPROVED ( 650, "General checkup request approved by HCP", true ),
    /**
     * AppointmentRequest denied by HCP
     */
    APPOINTMENT_REQUEST_DENIED ( 651, "General checkup request denied by HCP", true ),
    /**
     * AppointmentRequest otherwise updated
     */
    APPOINTMENT_REQUEST_UPDATED ( 652, "General checkup request was updated", true ),

    /**
     * OfficeVisit was viewed by a patient or staff member
     */
    OFFICE_VISIT_VIEWED ( 700, "Office visit was viewed", true ),

    /**
     * Office visit was created by a staff member
     */
    OFFICE_VISIT_CREATED ( 701, "Office visit created for patient", true ),

    /**
     * Office visit was updated by a staff member
     */
    OFFICE_VISIT_UPDATED ( 702, "Office visit was updated", true ),

    /**
     * Office visit was deleted by a staff member
     */
    OFFICE_VISIT_DELETED ( 703, "Office visit was deleted", true ),

    /**
     * Create basic health metrics
     */
    GENERAL_CHECKUP_CREATE ( 800, "Create office visit for patient", true ),
    /**
     * HCP views basic health metrics
     */
    GENERAL_CHECKUP_HCP_VIEW ( 801, "View office visit by HCP", true ),
    /**
     * HCP edits basic health metrics
     */
    GENERAL_CHECKUP_EDIT ( 802, "HCP edits basic health metrics", true ),
    /**
     * Patient views basic health metrics for an office visit
     */
    GENERAL_CHECKUP_PATIENT_VIEW ( 810, "View office visit by Patient", true ),
    /**
     * Patient deleted all office visits
     */
    DELETE_ALL_OFFICE_VISITS ( 899, "Patient deleted all office visits", false ),
    /**
     * Patient view all office visits
     */
    VIEW_ALL_OFFICE_VISITS ( 898, "Patient viewed all office visits", false ),

    /**
     * Office visit is deleted
     */
    GENERAL_CHECKUP_DELETE ( 811, "Office visit deleted", true ),

    /**
     * Admin adds an ICD-10 code
     */
    ICD_CREATE ( 1001, "Admin adds ICD-10 code", false ),
    /**
     * Admin deletes an ICD10 code
     */
    ICD_DELETE ( 1002, "Admin deletes ICD-10 code", false ),
    /**
     * Admin edits ICD-10 code
     */
    ICD_EDIT ( 1003, "Admin edits ICD-10 code", false ),
    /**
     * Admin views ICD-10 code
     */
    ICD_VIEW ( 1004, "Administrator views ICD-10 codes", false ),
    /**
     * Admin views all ICD-10 code
     */
    ICD_VIEW_ALL ( 1005, "Administrator views all ICD-10 codes", false ),
    /**
     * User gets diagnosis by id
     */
    DIAGNOSIS_VIEW_BY_ID ( 1006, "Diagnoses retrieved by id", true ),
    /**
     * User gets diagnoses for an office visit
     */
    DIAGNOSIS_VIEW_BY_OFFICE_VISIT ( 1007, "Diagnoses retrieved by office visit", true ),
    /**
     * Patient views diagnoses
     */
    DIAGNOSIS_PATIENT_VIEW_ALL ( 1008, "Patient views diagnoses", true ),
    /**
     * HCP creates diagnosis
     */
    DIAGNOSIS_CREATE ( 1009, "HCP creates a diagnosis within and office visit", true ),
    /**
     * HCP edits diagnosis
     */
    DIAGNOSIS_EDIT ( 1010, "HCP edits diagnosis", true ),
    /**
     * HCP deletes diagnosis
     */
    DIAGNOSIS_DELETE ( 1011, "HCP deletes diagnosis", true ),

    /**
     * Admin created a new drug
     */
    DRUG_CREATE ( 900, "Admin created a new drug", true ),
    /**
     * Admin edited an existing drug
     */
    DRUG_EDIT ( 901, "Admin edited an existing drug", true ),
    /**
     * Admin deleted an existing drug
     */
    DRUG_DELETE ( 902, "Admin deleted an existing drug", true ),
    /**
     * Admin views all drugs in the system
     */
    DRUG_VIEW ( 903, "Admin views all drugs in the system", true ),

    /**
     * HCP created a new prescription
     */
    PRESCRIPTION_CREATE ( 910, "HCP created a new prescription", true ),
    /**
     * HCP edited an existing prescription
     */
    PRESCRIPTION_EDIT ( 911, "HCP edited an existing prescription", true ),
    /**
     * HCP deleted an existing prescription
     */
    PRESCRIPTION_DELETE ( 912, "HCP deleted an existing prescription", true ),
    /**
     * User viewed an existing prescription
     */
    PRESCRIPTION_VIEW ( 913, "User viewed an existing prescription", true ),
    /**
     * Patient viewed their list of prescriptions
     */
    PATIENT_PRESCRIPTION_VIEW ( 914, "Patient viewed their list of prescriptions", true ),
    /**
     * Attempt to update password fails
     */
    PASSWORD_UPDATE_FAILURE ( 1100, "Failed password update", true ),

    /**
     * Attempt to update password is successful
     */
    PASSWORD_UPDATE_SUCCESS ( 1101, "Successful password update", true ),
    /**
     * Reset request email sent successfully
     */
    PASSWORD_RESET_EMAIL_SENT ( 1102, "Reset request email sent", true ),

    /**
     * User views their log entries
     */
    VIEW_USER_LOG ( 1201, "Log events viewed", true ),
    /**
     * An email is sent to the user on password change
     */
    CREATE_PW_CHANGE_EMAIL ( 1301, "PW Change Email notification sent", true ),
    /**
     * An email is sent to the user on appointment request change
     */
    CREATE_APPOINTMENT_REQUEST_EMAIL ( 1302, "AppointmentRequest Email notification sent", true ),
    /**
     * An email is sent to the user on lockout
     */
    CREATE_LOCKOUT_EMAIL ( 1303, "Account Lockout Email notification sent", true ),

    /**
     * Patient views a survey
     */
    PATIENT_VIEW_SURVEY ( 1500, "Patient views a survey", true ),

    /**
     * Admin views a survey
     */
    ADMIN_VIEW_SURVEY ( 1501, "Admin views a survey", false ),

    /**
     * Patient completes a satisfaction survey
     */
    PATIENT_COMPLETED_SATISFACION_SURVEY ( 1502, "Patient has completed a satisfaction survey", true ),

    /**
     * Patient views a completed a satisfaction survey
     */
    PATIENT_VIEW_COMPLETED_SATISFACION_SURVEY ( 1503, "Patient views a completed a satisfaction survey", true ),

    /**
     * Patient views average ratings
     */
    PATIENT_VIEW_STATS ( 1504, "Patient views average ratings", true ),

    /**
     * Admin views average ratings
     */
    ADMIN_VIEW_STATS ( 1506, "Admin views average ratings", false ),

    /**
     * Admin created a new vaccine
     */
    VACCINETYPE_CREATE ( 1601, "Admin created a new vaccine", false ),

    /**
     * Admin deleted a new vaccine
     */
    VACCINETYPE_DELETE ( 1602, "Admin deleted an existing vaccine", false ),
    /**
     * Admin edited an existing vaccine
     */
    VACCINETYPE_EDIT ( 1603, "Admin edited an existing vaccine", false ),

    /**
     * Admin views all vaccines in the system
     */
    VACCINETYPE_VIEW ( 1604, "User viewed an existing vaccine", false ),

    /**
     * An upcoming Vaccination Certificate was created.
     */
    VACCINATION_CERTIFICATE_CREATE ( 1801, "Vaccination certificate was created", true ),

    /**
     * An upcoming Vaccination Certificate was recieved.
     */
    VACCINATION_CERTIFICATE_GET ( 1802, "Vaccination certificate was downloaded", true ),

    /**
     * A CPT Code was viewed
     */
    CPT_CODE_VIEWED ( 2101, "CPT Code was viewed", false ),

    /**
     * A CPT Code was added
     */
    CPT_CODE_ADDED ( 2102, "CPT Code was added", false ),

    /**
     * A CPT Code was edited
     */
    CPT_CODE_EDITED ( 2103, "CPT Code was edited", false ),

    /**
     * A CPT Code was deleted
     */
    CPT_CODE_DELETED ( 2104, "CPT Code was deleted", false ),

    /**
     * List of active CPT codes viewed
     */
    LIST_ACTIVE_CPT_CODES ( 2105, "Active CPT codes viewed", false ),

    /**
     * List of archived CPT codes viewed
     */
    LIST_ARCHIVED_CPT_CODES ( 2106, "Archived CPT codes viewed", false ),

    /**
     * List of all bills in the system
     */
    LIST_ALL_BILLS ( 2107, "All bills viewed", false ),

    /**
     * List of all bills in the system by a particular patient
     */
    LIST_BILLS_BY ( 2108, "Patient bills viewed", true ),

    /**
     * A bill was viewed is all
     */
    VIEW_BILL ( 2109, "Bill was viewed", true ),

    /**
     * A bill was updated
     */
    EDIT_BILLS ( 2110, "Bill was udpated", false );

    /**
     * Creates a TransactionType for logging events
     *
     * @param code
     *            Code of the event
     * @param description
     *            Description of the event that occurred
     * @param patientViewable
     *            Whether this logged event can be viewed by the patient
     *            involved
     */
    private TransactionType ( final int code, final String description, final boolean patientViewable ) {
        this.code = code;
        this.description = description;
        this.patientView = patientViewable;
    }

    /**
     * Code of the TransactionType, from the iTrust2 wiki.
     */
    private int     code;
    /**
     * Description of the event
     */
    private String  description;
    /**
     * Whether the patient can view the event
     */
    private boolean patientView;

    /**
     * Retrieves the code of this TransactionType
     *
     * @return Code of the event
     */
    public int getCode () {
        return code;
    }

    /**
     * Description of this TransactionType event
     *
     * @return Description of the event
     */
    public String getDescription () {
        return description;
    }

    /**
     * Retrieves if the Patient can view this event
     *
     * @return Patient viewable or not
     */
    public boolean isPatientViewable () {
        return patientView;
    }

}
