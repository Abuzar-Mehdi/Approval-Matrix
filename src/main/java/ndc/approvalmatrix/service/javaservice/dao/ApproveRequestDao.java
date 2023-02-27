package ndc.approvalmatrix.service.javaservice.dao;

import ndc.approvalmatrix.service.javaservice.commons.ApprovalConstants;
import ndc.approvalmatrix.service.javaservice.dto.RequestDto;

import java.sql.*;
import java.time.LocalDateTime;

public class ApproveRequestDao {

    private Connection connection;

    public ApproveRequestDao() {

    }

    public ApproveRequestDao(Connection connection) {
        super();
        this.connection = connection;
    }


    public RequestDto approveRequest(RequestDto requestDto){


        try {


            String sqlRequest="SELECT NR.ID,NR.CONTRACTID ,NR2.SEQUENCENO,NR2.GROUPNO,NR2.ID AS REQID,NR2.RULEVALUE,NR.ISSEQUENTIAL,NR.STATUS ,NR2.SEQSTATUS,NR2.STATUS as APPROVERSTATUS ,NR2.GROUPSTATUS FROM ndc_am_request NR  " +
                    "INNER JOIN ndc_am_instances NR2 ON NR.ID =NR2.REQUESTID " +
                    "WHERE NR.CONTRACTID =? AND  NR.ACCOUNTNO=? AND NR2.APPROVERID =? AND NR.REFERENCENO =? " ;
                   // "AND NR.STATUS =? AND NR2.SEQSTATUS =? AND NR2.STATUS=? ";

            PreparedStatement statementS = connection.prepareStatement(sqlRequest);

            statementS.setString(1,requestDto.getContractId());
            statementS.setString(2, requestDto.getAccountNo());
            statementS.setString(3,requestDto.getApproverId());
            statementS.setString(4,requestDto.getReferenceNo());


            ResultSet resultSet = statementS.executeQuery();

            if(resultSet.next()) {

                requestDto.setRequestId(resultSet.getLong("ID"));
                requestDto.setRuleValue(resultSet.getInt("RULEVALUE"));
                requestDto.setSequenceNo(resultSet.getInt("SEQUENCENO"));
                requestDto.setGroupNo(resultSet.getInt("GROUPNO"));

                if (resultSet.getString("STATUS").equalsIgnoreCase(ApprovalConstants.IN_PROGRESS) && resultSet.getString("SEQSTATUS").equalsIgnoreCase(ApprovalConstants.PENDING) && resultSet.getString("APPROVERSTATUS").equalsIgnoreCase(ApprovalConstants.PENDING) && resultSet.getString("GROUPSTATUS").equalsIgnoreCase(ApprovalConstants.PENDING) ) {


                    /// UPDATING REQUEST WORK FLOW STATUS ///

                    //String sqlWorkFlow = "UPDATE ndc_requestworkflow SET  status=?, remarks=? WHERE requestid=? and approverid=? and sequenceno=?";
                    String sqlWorkFlow = "UPDATE ndc_am_instances SET  STATUS=?, REMARKS=? WHERE ID=? ";

                    PreparedStatement statementU = connection.prepareStatement(sqlWorkFlow);

                    statementU.setString(1, ApprovalConstants.APPROVED);
                    statementU.setString(2, requestDto.getRemarks());
                    statementU.setLong(3, resultSet.getLong("REQID"));


                    if (statementU.executeUpdate() == 0) {
                        throw new RuntimeException("Record not updated");
                    }



                    CallableStatement callableStatement = connection.prepareCall("{CALL "+ ApprovalConstants.GET_GROUP_STATUS +"(?,?,?,?,?,?)}");
                    callableStatement.setLong(1, requestDto.getRequestId());
                    callableStatement.setString(2, ApprovalConstants.APPROVED);
                    callableStatement.setInt(3, requestDto.getSequenceNo());
                    callableStatement.setInt(4, requestDto.getGroupNo());
                    callableStatement.setInt(5, requestDto.getRuleValue());
                    callableStatement.setInt(6, 0);
                    //callableStatement.setInt(6, resultSet.getInt("ISSEQUENTIAL"));
                    callableStatement.execute();

                    ResultSet resultSet2 =callableStatement.getResultSet();


                    if (resultSet2.next()) {

                        if (resultSet.getInt("ISSEQUENTIAL") == 1) {

                            /* SEQUENTIAL */

                            // Updating group Status //

                            String sqlAssignToNextApprove = "UPDATE ndc_am_instances SET  GROUPSTATUS=? WHERE REQUESTID=? AND SEQUENCENO=? AND  GROUPNO=?  ";
                            PreparedStatement statement = connection.prepareStatement(sqlAssignToNextApprove);
                            statement.setString(1, ApprovalConstants.APPROVED);
                            statement.setLong(2, requestDto.getRequestId());
                            statement.setLong(3, requestDto.getSequenceNo());
                            statement.setInt(4, requestDto.getGroupNo());

                            statement.executeUpdate();

                            CallableStatement callableStatement1 = connection.prepareCall("{CALL "+ ApprovalConstants.GET_NON_SEQ_STATUS +"(?,?,?)}");

                            callableStatement1.setLong(1, requestDto.getRequestId());
                            callableStatement1.setString(2, ApprovalConstants.APPROVED);
                            callableStatement1.setInt(3, requestDto.getSequenceNo());

                            callableStatement1.execute();

                            ResultSet resultSet3 = callableStatement1.getResultSet();

                            if (resultSet3.next()) {


                                String sqlAssignToNextApprove1 = "UPDATE ndc_am_instances SET  SEQSTATUS=? WHERE REQUESTID=? AND SEQUENCENO=? ";
                                PreparedStatement statement2 = connection.prepareStatement(sqlAssignToNextApprove1);
                                statement2.setString(1, ApprovalConstants.APPROVED);
                                statement2.setLong(2, requestDto.getRequestId());
                                statement2.setInt(3, requestDto.getSequenceNo());

                                statement2.executeUpdate();

                                String sqlAssignToNextApprover2 = "UPDATE ndc_am_request SET  STATUS=? , REMARKS=? , MODIFYDATE=? , MODIFYBY=?   WHERE ID=?";
                                PreparedStatement statement3 = connection.prepareStatement(sqlAssignToNextApprover2);
                                statement3.setString(1, ApprovalConstants.APPROVED);
                                statement3.setString(2, requestDto.getRemarks());
                                statement3.setString(3, LocalDateTime.now().toString());
                                statement3.setString(4, requestDto.getApproverId());
                                statement3.setLong(5, requestDto.getRequestId());

                                statement3.executeUpdate();

                                requestDto.setStatus(ApprovalConstants.APPROVED);
                                requestDto.setResponse(ApprovalConstants.REQUEST_APPROVED_SUCCESSFULLY);

                            }
                            else {

                                int nextGroup = requestDto.getGroupNo();
                                String sqlAssignToNextApprove1 = "UPDATE ndc_am_instances SET  STATUS=? , GROUPSTATUS=? ,SEQSTATUS=?  WHERE REQUESTID=? AND SEQUENCENO=? AND GROUPNO=? ";
                                PreparedStatement statement2 = connection.prepareStatement(sqlAssignToNextApprove1);
                                statement2.setString(1, ApprovalConstants.PENDING);
                                statement2.setString(2, ApprovalConstants.PENDING);
                                statement2.setString(3, ApprovalConstants.PENDING);
                                statement2.setLong(4, requestDto.getRequestId());
                                statement2.setInt(5, requestDto.getSequenceNo());
                                statement2.setInt(6,++nextGroup );

                            if (statement2.executeUpdate() >= 1) {

                                String sqlAssignToNextApprover2 = "UPDATE ndc_am_request SET  STATUS=? , REMARKS=? , MODIFYDATE=? , MODIFYBY=?  WHERE ID=?";
                                PreparedStatement statement3 = connection.prepareStatement(sqlAssignToNextApprover2);
                                statement3.setString(1, ApprovalConstants.IN_PROGRESS);
                                statement3.setString(2, requestDto.getRemarks());
                                statement3.setString(3, LocalDateTime.now().toString());
                                statement3.setString(4, requestDto.getApproverId());
                                statement3.setLong(5, requestDto.getRequestId());

                                statement3.executeUpdate();

                                requestDto.setStatus(ApprovalConstants.IN_PROGRESS);
                                requestDto.setResponse(ApprovalConstants.REQUEST_ASSIGN_TO_NEXT_APPROVERS);

                            } else {

                                String sqlAssignToNextApprover2 = "UPDATE ndc_am_request SET  STATUS=? , REMARKS=? , MODIFYDATE=? , MODIFYBY=?   WHERE ID=?";
                                PreparedStatement statement3 = connection.prepareStatement(sqlAssignToNextApprover2);
                                statement3.setString(1, ApprovalConstants.APPROVED);
                                statement3.setString(2, requestDto.getRemarks());
                                statement3.setString(3, LocalDateTime.now().toString());
                                statement3.setString(4, requestDto.getApproverId());
                                statement3.setLong(5, requestDto.getRequestId());

                                statement3.executeUpdate();

                                requestDto.setStatus(ApprovalConstants.APPROVED);
                                requestDto.setResponse(ApprovalConstants.REQUEST_APPROVED_SUCCESSFULLY);

                            }


                                String sqlAssignToNextApprover2 = "UPDATE ndc_am_request SET  STATUS=? , REMARKS=? , MODIFYDATE=? , MODIFYBY=?  WHERE ID=?";
                                PreparedStatement statement3 = connection.prepareStatement(sqlAssignToNextApprover2);
                                statement3.setString(1, ApprovalConstants.IN_PROGRESS);
                                statement3.setString(2, requestDto.getRemarks());
                                statement3.setString(3, LocalDateTime.now().toString());
                                statement3.setString(4, requestDto.getApproverId());
                                statement3.setLong(5, requestDto.getRequestId());

                                statement3.executeUpdate();

                                requestDto.setStatus(ApprovalConstants.IN_PROGRESS);
                                requestDto.setResponse(ApprovalConstants.REQUEST_PENDING_FROM_OTHER_APPROVERS);
                            }

                        } else {

                            /* NON SEQUENTIAL */

                            // Updating group Status //

                            String sqlAssignToNextApprove = "UPDATE ndc_am_instances SET  GROUPSTATUS=? WHERE REQUESTID=? AND SEQUENCENO=? AND  GROUPNO=?  ";
                            PreparedStatement statement = connection.prepareStatement(sqlAssignToNextApprove);
                            statement.setString(1, ApprovalConstants.APPROVED);
                            statement.setLong(2, requestDto.getRequestId());
                            statement.setLong(3, requestDto.getSequenceNo());
                            statement.setInt(4, requestDto.getGroupNo());

                            statement.executeUpdate();

                            CallableStatement callableStatement1 = connection.prepareCall("{CALL "+ ApprovalConstants.GET_NON_SEQ_STATUS +"(?,?,?)}");

                            callableStatement1.setLong(1, requestDto.getRequestId());
                            callableStatement1.setString(2, ApprovalConstants.APPROVED);
                            callableStatement1.setInt(3, requestDto.getSequenceNo());

                            callableStatement1.execute();

                            ResultSet resultSet3 = callableStatement1.getResultSet();

                            if (resultSet3.next()) {


                                String sqlAssignToNextApprove1 = "UPDATE ndc_am_instances SET  SEQSTATUS=? WHERE REQUESTID=? AND SEQUENCENO=? ";
                                PreparedStatement statement2 = connection.prepareStatement(sqlAssignToNextApprove1);
                                statement2.setString(1, ApprovalConstants.APPROVED);
                                statement2.setLong(2, requestDto.getRequestId());
                                statement2.setInt(3, requestDto.getSequenceNo());

                                statement2.executeUpdate();

                                String sqlAssignToNextApprover2 = "UPDATE ndc_am_request SET  STATUS=? , REMARKS=? , MODIFYDATE=? , MODIFYBY=?   WHERE ID=?";
                                PreparedStatement statement3 = connection.prepareStatement(sqlAssignToNextApprover2);
                                statement3.setString(1, ApprovalConstants.APPROVED);
                                statement3.setString(2, requestDto.getRemarks());
                                statement3.setString(3, LocalDateTime.now().toString());
                                statement3.setString(4, requestDto.getApproverId());
                                statement3.setLong(5, requestDto.getRequestId());

                                statement3.executeUpdate();

                                requestDto.setStatus(ApprovalConstants.APPROVED);
                                requestDto.setResponse(ApprovalConstants.REQUEST_APPROVED_SUCCESSFULLY);

                            }
                            else {

                                String sqlAssignToNextApprove1 = "UPDATE ndc_am_instances SET  STATUS=?, GROUPSTATUS=?  WHERE REQUESTID=? AND SEQUENCENO=? AND STATUS=? AND GROUPSTATUS=? ";
                                PreparedStatement statement2 = connection.prepareStatement(sqlAssignToNextApprove1);
                                statement2.setString(1, ApprovalConstants.PENDING);
                                statement2.setString(2, ApprovalConstants.PENDING);
                                statement2.setLong(3, requestDto.getRequestId());
                                statement2.setInt(4, requestDto.getSequenceNo());
                                statement2.setString(5, ApprovalConstants.NOT_ASSIGNED);
                                statement2.setString(6, ApprovalConstants.NOT_ASSIGNED);

                                statement2.executeUpdate();

                                String sqlAssignToNextApprover2 = "UPDATE ndc_am_request SET  STATUS=? , REMARKS=? , MODIFYDATE=? , MODIFYBY=?  WHERE ID=?";
                                PreparedStatement statement3 = connection.prepareStatement(sqlAssignToNextApprover2);
                                statement3.setString(1, ApprovalConstants.IN_PROGRESS);
                                statement3.setString(2, requestDto.getRemarks());
                                statement3.setString(3, LocalDateTime.now().toString());
                                statement3.setString(4, requestDto.getApproverId());
                                statement3.setLong(5, requestDto.getRequestId());

                                statement3.executeUpdate();

                                requestDto.setStatus(ApprovalConstants.IN_PROGRESS);
                                requestDto.setResponse(ApprovalConstants.REQUEST_PENDING_FROM_OTHER_APPROVERS);
                            }

                        }

                    } else {

                        String sqlAssignToNextApprover2 = "UPDATE ndc_am_request SET  STATUS=? , REMARKS=? , MODIFYDATE=? , MODIFYBY=?  WHERE ID=?";
                        PreparedStatement statement3 = connection.prepareStatement(sqlAssignToNextApprover2);
                        statement3.setString(1, ApprovalConstants.IN_PROGRESS);
                        statement3.setString(2, requestDto.getRemarks());
                        statement3.setString(3, LocalDateTime.now().toString());
                        statement3.setString(4, requestDto.getApproverId());
                        statement3.setLong(5, requestDto.getRequestId());

                        statement3.executeUpdate();

                        requestDto.setStatus(ApprovalConstants.IN_PROGRESS);
                        requestDto.setResponse(ApprovalConstants.REQUEST_PENDING_FROM_OTHER_APPROVERS);
                    }

                }else{

                    if(resultSet.getString("STATUS").equalsIgnoreCase(ApprovalConstants.APPROVED)){

                        requestDto.setResponse(ApprovalConstants.REQUEST_ALREADY_APPROVED);
                    }
                    else if (resultSet.getString("STATUS").equalsIgnoreCase(ApprovalConstants.REJECTED)){

                        requestDto.setResponse(ApprovalConstants.REQUEST_ALREADY_REJECTED);
                    }
                    else if (resultSet.getString("STATUS").equalsIgnoreCase(ApprovalConstants.CANCEL)){

                        requestDto.setResponse(ApprovalConstants.REQUEST_ALREADY_CANCELED);
                    }
                    else if(resultSet.getString("STATUS").equalsIgnoreCase(ApprovalConstants.IN_PROGRESS) &&  resultSet.getString("APPROVERSTATUS").equalsIgnoreCase(ApprovalConstants.APPROVED)  ){

                        requestDto.setResponse(ApprovalConstants.REQUEST_ALREADY_APPROVED_BY_USER);
                    }
                    else if(resultSet.getString("STATUS").equalsIgnoreCase(ApprovalConstants.IN_PROGRESS) &&  resultSet.getString("SEQSTATUS").equalsIgnoreCase(ApprovalConstants.APPROVED)  && resultSet.getString("APPROVERSTATUS").equalsIgnoreCase(ApprovalConstants.PENDING) ){

                        requestDto.setResponse(ApprovalConstants.REQUEST_ALREADY_APPROVED);
                    }
                    else if(resultSet.getString("STATUS").equalsIgnoreCase(ApprovalConstants.IN_PROGRESS) &&  resultSet.getString("SEQSTATUS").equalsIgnoreCase(ApprovalConstants.PENDING)  && resultSet.getString("GROUPSTATUS").equalsIgnoreCase(ApprovalConstants.APPROVED) ){

                        requestDto.setResponse(ApprovalConstants.REQUEST_ALREADY_APPROVED);
                    }
                    else if(resultSet.getString("STATUS").equalsIgnoreCase(ApprovalConstants.IN_PROGRESS) &&  resultSet.getString("SEQSTATUS").equalsIgnoreCase(ApprovalConstants.PENDING)  && resultSet.getString("GROUPSTATUS").equalsIgnoreCase(ApprovalConstants.NOT_ASSIGNED) ){

                        requestDto.setResponse(ApprovalConstants.REQUEST_NOT_ASSIGNED);
                    }

                }

            }else{

                requestDto.setResponse(ApprovalConstants.NO_REQUEST_FOUND);
            }


        } catch (SQLException e) {
            try {
                connection.rollback();
                connection.close();
                requestDto.setResponse(e.getMessage());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            e.printStackTrace();
        }

        return requestDto;

    }
}
