package ndc.approvalmatrix.service.javaservice.commons;

public class StoredProcedure {

    public static final String FETCH_APPROVAL ="ndc_fetchalluserpendingapprovals";
    public static final String FETCH_REQUEST ="ndc_fetchalluserpendingrequest";

    public static final String GET_CONTRACT_WORKFLOW="ndc_getcontractworkflow";

    public static final String GET_GROUP_STATUS="ndc_getgroupapprovalstatus";

    public static final String GET_NON_SEQ_STATUS="ndc_getsequenceapprovalstatus";

    public static final String GET_FEATURE_ACTION_APPROVER_LIMIT ="ndc_getapproverfeatureactionlimiagainstcontractaccount";

    public static final String REMOVE_TO_HISTORY ="ndc_transfermatrixtohistory";
}
