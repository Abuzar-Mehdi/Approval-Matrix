package ndc.approvalmatrix.service.javaservice.dao;

import ndc.approvalmatrix.service.javaservice.commons.ApprovalConstants;
import ndc.approvalmatrix.service.javaservice.dto.ApprovalRequestDto;
import ndc.approvalmatrix.service.javaservice.dto.ApprovalRow;
import ndc.approvalmatrix.service.javaservice.dto.WorkFlowFeatureAction;

import java.sql.*;

public class CreateApprovalMatrixDao {

    private Connection connection;


    public CreateApprovalMatrixDao() {

    }

    public CreateApprovalMatrixDao(Connection connection) {
        super();
        this.connection = connection;
    }

    public String createApprovalMatrix(ApprovalRequestDto approvalRequestDto){

        String result;
        try {

            String sqlMaster ="INSERT INTO ndc_am_matrix (CONTRACTID, ACCOUNTNO,USERID )"+  //, ISSEQUENTIAL)" +
                    "VALUES(?, ?, ?)";

            PreparedStatement statement = connection.prepareStatement(sqlMaster, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1,approvalRequestDto.getContractId()); //ContractID
            statement.setString(2,approvalRequestDto.getAccountNo()); //ContractID
            statement.setString(3,approvalRequestDto.getUserId()); //userid

            statement.executeUpdate();

            ResultSet keys = statement.getGeneratedKeys();
            keys.next();

            /// getting request id from table //

            approvalRequestDto.setRequestId(keys.getLong(1));

            // getting all members of Role

            for(ApprovalRow approvalRow : approvalRequestDto.getApprovalRowList()) {

//                String sqlRoleMember = "SELECT A.CUSTOMER_ID AS APPROVERID FROM customergroup A " +
//                        "INNER JOIN membergroup B  ON A.GROUP_ID = B.ID " +
//                        "WHERE A.CONTRACTID =? AND A.CORECUSTOMERID =? AND B.NAME =? ";
//
//                PreparedStatement statement1 = connection.prepareStatement(sqlRoleMember);
//                statement1.setString(1, approvalRequestDto.getContractId());
//                statement1.setString(2, approvalRequestDto.getCoreCustomerId());
//                statement1.setString(3, approvalRow.getRole());
//
//                ResultSet resultSet = statement1.executeQuery();

                int i = 0;
                String rule = null;


                String sqlDetail = "INSERT INTO ndc_am_matrix_detail" +
                        "(matrixid, workflowid, sequenceno, groupno, role, ischecker, rulevalue, rule,approvalorder)" +
                        "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";



                PreparedStatement statement2 = connection.prepareStatement(sqlDetail);

                statement2.setLong(1, approvalRequestDto.getRequestId());
                statement2.setInt(2, approvalRow.getWorkflowId());
                statement2.setInt(3, approvalRow.getSequenceNo());
                statement2.setInt(4, approvalRow.getGroupNo() == null ? 1 : approvalRow.getGroupNo());
                statement2.setString(5, approvalRow.getRole());
                statement2.setInt(6, approvalRow.getIsChecker());

                if (ApprovalConstants.ANY_ONE_VALUE == approvalRow.getApprovalRule()) {

                    rule = ApprovalConstants.ANY_ONE;

                } else if (ApprovalConstants.ANY_TWO_VALUE == approvalRow.getApprovalRule()) {

                    rule = ApprovalConstants.ANY_TWO;

                } else if (ApprovalConstants.ANY_THREE_VALUE == approvalRow.getApprovalRule()) {

                    rule = ApprovalConstants.ANY_THREE;

                } else if (ApprovalConstants.ANY_FOUR_VALUE == approvalRow.getApprovalRule()) {

                    rule = ApprovalConstants.ANY_FOUR;

                } else if (ApprovalConstants.ALL_VALUE == approvalRow.getApprovalRule()) {

                    rule = ApprovalConstants.ALL;

                }

                statement2.setInt(7, approvalRow.getApprovalRule());
                statement2.setString(8, rule);
                statement2.setInt(9, approvalRow.getGroupNo() == null ? 1 : approvalRow.getGroupNo());

                statement2.executeUpdate();

            }

            for ( WorkFlowFeatureAction workFlowFeatureAction : approvalRequestDto.getWorkFlowFeatureActions() ) {

                String sql="INSERT INTO ndc_am_workflow " +
                        "(matrixid, workflowid, issequential, featureactionid, minamount, maxamount) " +
                        "VALUES(?, ?, ?, ?, ?, ?) ";

                PreparedStatement statement1 = connection.prepareStatement(sql);
                statement1.setLong(1, approvalRequestDto.getRequestId());
                statement1.setInt(2, workFlowFeatureAction.getWorkflowId());
                statement1.setInt(3, workFlowFeatureAction.getIsSequential());
                statement1.setString(4, workFlowFeatureAction.getFeatureActionId());
                statement1.setDouble(5, workFlowFeatureAction.getMinAmount());
                statement1.setDouble(6, workFlowFeatureAction.getMaxAmount());

                statement1.executeUpdate();

            }

            result="Approval Matrix Successfully created :"+approvalRequestDto.getRequestId();

        }catch (Exception exception){

            try {
                result ="Error in creating Approval Matrix :"+exception.getMessage();
                connection.rollback();
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            exception.printStackTrace();
        }

        return result;
    }
}
