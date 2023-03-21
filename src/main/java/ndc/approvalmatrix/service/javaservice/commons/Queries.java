package ndc.approvalmatrix.service.javaservice.commons;

public class Queries {

    ////////////CREATE APPROVAL MATRIX/////////////////

    public static class CAM_QUERY {

        public static final String CAM_QUERY1 = "SELECT a.id,MAX(b.workflowid) as wid  FROM ndc_am_matrix a inner join ndc_am_matrix_detail b on a.id =b.matrixid  where a.contractid=? and a.accountno=? and softdelete=0";
        public static final String CAM_QUERY2 = "INSERT INTO ndc_am_matrix (CONTRACTID, ACCOUNTNO,USERID )" +
                " VALUES(?, ?, ?)";

        public static final String CAM_QUERY3 = "INSERT INTO ndc_am_matrix_detail (matrixid, workflowid, sequenceno, groupno, role, ischecker, rulevalue, rule, approvalorder)" +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

        public static String CAM_QUERY4 = "INSERT INTO ndc_am_workflow  (matrixid, workflowid, issequential, featureactionid, minamount, maxamount) " +
                " VALUES(?, ?, ?, ?, ?, ?) ";

        public static String CAM_QUERY5 = "SELECT accountId  FROM contractaccounts WHERE contractId = ?";

    }
    ////////////CREATE NEW  REQUEST/////////////////

    public static class CNR_QUERIES {


        public static final String CNR_QUERY1 = "INSERT INTO ndc_am_request (matrixid, requesterid,  status,  modifyby, remarks, referenceno, featureactionid,contractId,issequential,accountno,workflowid) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        public static final String CNR_QUERY2 = "SELECT A.CUSTOMER_ID AS APPROVERID FROM customergroup A " +
                "INNER JOIN membergroup B  ON A.GROUP_ID = B.ID " +
                "WHERE A.CONTRACTID =? AND A.CORECUSTOMERID =? AND B.NAME =?";

        public static final String CNR_QUERY3 = "SELECT count(B.NAME) as count  FROM customergroup A " +
                "INNER JOIN membergroup B  ON A.GROUP_ID = B.ID " +
                "WHERE A.CONTRACTID =? AND A.CORECUSTOMERID =? AND B.NAME =? ";

        public static final String CNR_QUERY4 = "INSERT INTO ndc_am_instances" +
                "(requestid, approverid, sequenceno, approverorder, approvedate, status, remarks, rulevalue, seqstatus,groupno,groupstatus)" +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }


    public static class AR_QUERIES {

        public static final String AR_QUERY1 = "SELECT NR.ID,NR.CONTRACTID ,NR2.SEQUENCENO,NR2.GROUPNO,NR2.ID AS REQID,NR2.RULEVALUE,NR.ISSEQUENTIAL,NR.STATUS ,NR2.SEQSTATUS,NR2.STATUS as APPROVERSTATUS ,NR2.GROUPSTATUS FROM ndc_am_request NR  " +
                "INNER JOIN ndc_am_instances NR2 ON NR.ID =NR2.REQUESTID " +
                "WHERE NR.CONTRACTID =? AND  NR.ACCOUNTNO=? AND NR2.APPROVERID =? AND NR.REFERENCENO =?";
        public static final String AR_QUERY2 = "UPDATE ndc_am_instances SET  STATUS=?, REMARKS=? WHERE ID=? ";
        public static final String AR_QUERY3 = "UPDATE ndc_am_instances SET  GROUPSTATUS=? WHERE REQUESTID=? AND SEQUENCENO=? AND  GROUPNO=?  ";
        public static final String AR_QUERY4 = "UPDATE ndc_am_instances SET  SEQSTATUS=? WHERE REQUESTID=? AND SEQUENCENO=? ";
        public static final String AR_QUERY5 = "UPDATE ndc_am_request SET  STATUS=? , REMARKS=? , MODIFYDATE=? , MODIFYBY=?   WHERE ID=?";
        public static final String AR_QUERY6 = "UPDATE ndc_am_instances SET  STATUS=? , GROUPSTATUS=? ,SEQSTATUS=?  WHERE REQUESTID=? AND SEQUENCENO=? AND GROUPNO=? ";
        public static final String AR_QUERY7 = "UPDATE ndc_am_instances SET  STATUS=?, GROUPSTATUS=?  WHERE REQUESTID=? AND SEQUENCENO=? AND STATUS=? AND GROUPSTATUS=? ";


    }

    public static class RR_QUERIES {

        public static final String RR_QUERY1 = "SELECT NR.ID,NR.CONTRACTID ,NR2.SEQUENCENO,NR2.ID AS REQID,NR2.RULEVALUE,NR.STATUS ,NR2.SEQSTATUS,NR2.STATUS as APPROVERSTATUS,NR2.GROUPSTATUS,NR.ISSEQUENTIAL FROM ndc_am_request NR  " +
                "INNER JOIN ndc_am_instances NR2 ON NR.ID =NR2.REQUESTID " +
                "WHERE NR.CONTRACTID =? AND  NR.ACCOUNTNO=?  AND NR2.APPROVERID =? AND NR.REFERENCENO =?";
        public static final String RR_QUERY2 = "UPDATE ndc_am_instances SET  STATUS=?,GROUPSTATUS=?,SEQSTATUS=?, REMARKS=? WHERE ID=? ";
        public static final String RR_QUERY3 = "UPDATE ndc_am_request SET  STATUS=? , REMARKS=? , MODIFYDATE=? , MODIFYBY=?  WHERE ID=?";

    }


    public static class CR_QUERIES {

        public static final String CR_QUERY1 = "SELECT * FROM ndc_am_request WHERE  CONTRACTID=? AND  REFERENCENO=? AND  SOFTDELETE=0 ";
        public static final String CR_QUERY2 = "UPDATE ndc_am_request SET  STATUS=? , REMARKS=? , MODIFYDATE=? , MODIFYBY=?  WHERE  REQUESTERID=? AND CONTRACTID=? AND REFERENCENO=?";

    }


    public static class GW_QUERIES {

        public static final String GW_QUERY1 = "SELECT id FROM ndc_am_matrix where contractid=? and accountno=? and softdelete=0 ";
        public static final String GW_QUERY2 = "SELECT workflowid,sequenceno,groupno,role,ischecker,rulevalue FROM ndc_am_matrix_detail where matrixid=? AND workflowid=?";

        public static final String GW_QUERY3 = "SELECT workflowid,issequential,featureactionid,minamount,maxamount FROM ndc_am_workflow where matrixid=? AND workflowid=? AND featureactionid=?";

    }

    public static class FAC_QUERIES{

        public static final String FAC_QUERY1="INSERT INTO ndc_featureactionconfig" +
                "(featureactionid, name, contractid, createby, createdate, modifyby, modifydate,isenabled,accountno)" +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        public static final String FAC_QUERY2="UPDATE ndc_featureactionconfig " +
                " SET featureactionid=?, name=?,  modifyby=?, modifydate=?, isenabled=? " +
                " WHERE contractid=? and  accountno=? and featureactionid=?";

    }


}
