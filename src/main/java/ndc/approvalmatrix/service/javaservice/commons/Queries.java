package ndc.approvalmatrix.service.javaservice.commons;

public class Queries {

    /// CreateApprovalMatrixDao

    public static final String CAM_QUERY1="INSERT INTO ndc_approvalmatrixmaster (CONTRACTID, ACCOUNTNO,USERID )"+
                    " VALUES(?, ?, ?)";

    public static final String CAM_QUERY2="SELECT A.CUSTOMER_ID AS APPROVERID FROM customergroup A " +
            " INNER JOIN membergroup B  ON A.GROUP_ID = B.ID " +
            " WHERE A.CONTRACTID =? AND A.CORECUSTOMERID =? AND B.NAME =?";

    public static final String CAM_QUERY3="INSERT INTO ndc_approvalmatrixdetail (APPROVALMASTERID, APPROVERID, SEQUENCENO, GROUPNO,APPROVALORDER, ROLE,WORKFLOWID) " +
            " VALUES(?, ?, ?, ?, ?, ?, ?)";

    public static final String CAM_QUERY4="INSERT INTO ndc_sequencerule" +
            "(APPROVALMASTERID, SEQUENCENO, GROUPNO ,RULE, RULEVALUE,WORKFLOWID)" +
            " VALUES(?, ?, ?, ?, ?, ?)";

    public static final String CAM_QUERY5="INSERT INTO dbxdb.ndc_workflowdetail " +
            " approvalmasterid, workflowid, issequential, featureactionid, minamount, maxamount) " +
            " VALUES(?, ?, ?, ?, ?, ?) ";


}
