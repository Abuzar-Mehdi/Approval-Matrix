package ndc.approvalmatrix.service.javaservice.commons;

public class ApprovalConstants {

    public static final String NOT_ASSIGNED ="NOT ASSIGNED";
    public static final String PENDING ="PENDING";
    public static final String APPROVED ="APPROVED";
    public static final String REJECTED ="REJECTED";
    public static final String IN_PROGRESS ="IN PROGRESS";
    public static final String CANCEL ="CANCEL";


    public static final String REQUEST_CREATED_SUCCESSFULLY ="REQUEST CREATED SUCCESSFULLY";

    public static final String REQUEST_APPROVED_SUCCESSFULLY="REQUEST APPROVED SUCCESSFULLY";

    public static final String REQUEST_REJECTED_SUCCESSFULLY="REQUEST REJECTED SUCCESSFULLY";

    public static final String REQUEST_ASSIGN_TO_NEXT_APPROVERS ="REQUEST ASSIGN TO NEXT APPROVERS";

    public static final String ONLY_SAME_USER_CAN_CANCEL ="ONLY SAME USER CAN CANCEL";

    public static final String REQUEST_PENDING_FROM_OTHER_APPROVERS ="REQUEST PENDING FROM OTHER APPROVERS";
    public static final String NO_REQUEST_FOUND ="NO REQUEST FOUND AGAINST REFERENCE NO";

    public static final String  REQUEST_CANCELED="REQUEST CANCELED SUCCESSFULLY";

    public static final String REQUEST_ALREADY_APPROVED ="REQUEST ALREADY APPROVED";


    public static final String REQUEST_ALREADY_APPROVED_BY_USER ="REQUEST ALREADY APPROVED BY USER";

    public static final String REQUEST_ALREADY_REJECTED ="REQUEST ALREADY REJECTED";

    public static final String  REQUEST_ALREADY_CANCELED="REQUEST ALREADY CANCELED";

    public static final String  REQUEST_NOT_ASSIGNED="REQUEST NOT ASSIGNED TO USER";

    public static final String ALL ="All Approvals";
    public static final String ANY_ONE ="Any one approval";
    public static final String ANY_TWO ="Any two approval";
    public static final String ANY_THREE ="Any three approval";
    public static final String ANY_FOUR ="Any four approval";



    public static final Integer NOT_ASSIGNED_VALUE =1;
    public static final Integer PENDING_VALUE =2;
    public static final Integer APPROVED_VALUE =3;
    public static final Integer REJECTED_VALUE =4;
    public static final Integer IN_PROGRESS_VALUE =5;
    public static final Integer CANCEL_VALUE =6;


    public static final Integer ALL_VALUE = -1;
    public static final Integer ANY_ONE_VALUE =1;
    public static final Integer ANY_TWO_VALUE =2;
    public static final Integer ANY_THREE_VALUE =3;
    public static final Integer ANY_FOUR_VALUE =4;

    public static final String FETCH_APPROVAL ="ndc_fetchalluserpendingapprovals";
    public static final String FETCH_REQUEST ="ndc_fetchalluserpendingrequest";

    public static final String GET_CONTRACT_WORKFLOW="ndc_getcontractworkflow";

    public static final String GET_GROUP_STATUS="ndc_getgroupapprovalstatus";

    public static final String GET_NON_SEQ_STATUS="ndc_getsequenceapprovalstatus";

}
