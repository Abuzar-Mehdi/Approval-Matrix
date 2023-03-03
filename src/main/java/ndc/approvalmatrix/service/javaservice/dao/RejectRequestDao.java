package ndc.approvalmatrix.service.javaservice.dao;

import ndc.approvalmatrix.service.javaservice.commons.ApprovalConstants;
import ndc.approvalmatrix.service.javaservice.commons.Queries;
import ndc.approvalmatrix.service.javaservice.dto.RequestDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class RejectRequestDao {

    private Connection connection;

    public RejectRequestDao() {   }
    public RejectRequestDao(Connection connection) {
        super();
        this.connection = connection;
    }
    public RequestDto rejectRequest(RequestDto requestDto){

        try {

//            String sqlRequest="SELECT NR.ID,NR.CONTRACTID ,NR2.SEQUENCENO,NR2.ID AS REQID,NR2.RULEVALUE,NR.STATUS ,NR2.SEQSTATUS,NR2.STATUS as APPROVERSTATUS,NR2.GROUPSTATUS,NR.ISSEQUENTIAL FROM ndc_request NR  " +
//                    "INNER JOIN ndc_requestworkflow NR2 ON NR.ID =NR2.REQUESTID " +
//                    "WHERE NR.CONTRACTID =? AND  NR.ACCOUNTNO=?  AND NR2.APPROVERID =? AND NR.REFERENCENO =? " ;


            PreparedStatement statementS = connection.prepareStatement(Queries.RR_QUERIES.RR_QUERY1);

            statementS.setString(1,requestDto.getContractId());
            statementS.setString(2, requestDto.getAccountNo());
            statementS.setString(3,requestDto.getApproverId());
            statementS.setString(4,requestDto.getReferenceNo());

            ResultSet resultSet = statementS.executeQuery();

            if(resultSet.next()){

                requestDto.setRequestId(resultSet.getLong("ID"));
                requestDto.setSequenceNo( resultSet.getInt("SEQUENCENO"));

                if(resultSet.getString("STATUS").equalsIgnoreCase(ApprovalConstants.IN_PROGRESS) && resultSet.getString("SEQSTATUS").equalsIgnoreCase(ApprovalConstants.PENDING) &&  resultSet.getString("GROUPSTATUS").equalsIgnoreCase(ApprovalConstants.PENDING)  ) {

                    if(resultSet.getString("APPROVERSTATUS").equalsIgnoreCase(ApprovalConstants.PENDING)) {

                       // String sqlWorkFlow = "UPDATE ndc_requestworkflow SET  STATUS=?,GROUPSTATUS=?,SEQSTATUS=?, REMARKS=? WHERE ID=? ";

                        PreparedStatement statement = connection.prepareStatement(Queries.RR_QUERIES.RR_QUERY2);

                        statement.setString(1, ApprovalConstants.REJECTED);
                        statement.setString(2, ApprovalConstants.REJECTED);
                        statement.setString(3, ApprovalConstants.REJECTED);
                        statement.setString(4, requestDto.getRemarks());
                        statement.setLong(5, resultSet.getLong("REQID"));

                        statement.executeUpdate();

                        /// update request to reject  ///

                       // String sqlAssignToNextApprover2 = "UPDATE ndc_request SET  STATUS=? , REMARKS=? , MODIFYDATE=? , MODIFYBY=?  WHERE ID=?";
                        PreparedStatement statement3 = connection.prepareStatement(Queries.RR_QUERIES.RR_QUERY3);
                        statement3.setString(1, ApprovalConstants.REJECTED);
                        statement3.setString(2, requestDto.getRemarks());
                        statement3.setString(3, LocalDateTime.now().toString());
                        statement3.setString(4, requestDto.getApproverId());
                        statement3.setLong(5, requestDto.getRequestId());

                        statement3.executeUpdate();
                        requestDto.setStatus(ApprovalConstants.REJECTED);
                        requestDto.setResponse(ApprovalConstants.REQUEST_REJECTED_SUCCESSFULLY);

                    }else{

                        requestDto.setResponse(ApprovalConstants.REQUEST_ALREADY_APPROVED_BY_USER);
                    }

                }else {

                    if(resultSet.getString("STATUS").equalsIgnoreCase(ApprovalConstants.APPROVED)){

                        requestDto.setResponse(ApprovalConstants.REQUEST_ALREADY_APPROVED);

                    }else if (resultSet.getString("STATUS").equalsIgnoreCase(ApprovalConstants.REJECTED)){

                        requestDto.setResponse(ApprovalConstants.REQUEST_ALREADY_REJECTED);
                    }
                    else if (resultSet.getString("STATUS").equalsIgnoreCase(ApprovalConstants.CANCEL)){

                        requestDto.setResponse(ApprovalConstants.REQUEST_ALREADY_CANCELED);
                    }

                }

            }else {

                requestDto.setResponse(ApprovalConstants.NO_REQUEST_FOUND);

            }

        } catch (SQLException e) {
            try {
                connection.rollback();
                connection.close();
                requestDto.setResponse(e.getStackTrace().toString());
                e.printStackTrace();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }

        return requestDto;

    }
}
