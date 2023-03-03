package ndc.approvalmatrix.service.javaservice.dao;

import ndc.approvalmatrix.service.javaservice.commons.ApprovalConstants;
import ndc.approvalmatrix.service.javaservice.commons.Queries;
import ndc.approvalmatrix.service.javaservice.commons.StoredProcedure;
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

           // String sqlMain = "SELECT a.id,MAX(b.workflowid) as wid  FROM ndc_am_matrix a inner join ndc_am_matrix_detail b on a.id =b.matrixid  where a.contractid=? and a.accountno=? and softdelete=0";

            PreparedStatement statementX = connection.prepareStatement(Queries.CAM_QUERY.CAM_QUERY1);

            statementX.setString(1,approvalRequestDto.getContractId()); //ContractID
            statementX.setString(2,approvalRequestDto.getAccountNo()); //ContractID

            ResultSet resultSet =  statementX.executeQuery();

            if(resultSet.next() && resultSet.getString("id") != null){

                approvalRequestDto.setMatrixId(resultSet.getLong("id"));
                int tempWid = resultSet.getInt("wid");
                approvalRequestDto.setWorkFlowId(++tempWid);

            } else{

//                String sqlMaster = "INSERT INTO ndc_am_matrix (CONTRACTID, ACCOUNTNO,USERID )" +  //, ISSEQUENTIAL)" +
//                        "VALUES(?, ?, ?)";

                PreparedStatement statement = connection.prepareStatement(Queries.CAM_QUERY.CAM_QUERY2, Statement.RETURN_GENERATED_KEYS);

                statement.setString(1, approvalRequestDto.getContractId()); //ContractID
                statement.setString(2, approvalRequestDto.getAccountNo()); //ContractID
                statement.setString(3, approvalRequestDto.getUserId()); //userid

                statement.executeUpdate();

                ResultSet keys = statement.getGeneratedKeys();
                keys.next();
                approvalRequestDto.setMatrixId(keys.getLong(1));
                approvalRequestDto.setWorkFlowId(approvalRequestDto.getWorkFlowId() == 0 ? 1 : approvalRequestDto.getWorkFlowId());   ;

            }


            if(approvalRequestDto.getIsEdit() == 1){

                CallableStatement callableStatement = connection.prepareCall("{CALL " + StoredProcedure.REMOVE_TO_HISTORY + "(?,?,?)}");
                callableStatement.setLong(1, approvalRequestDto.getMatrixId());
                callableStatement.setLong(2, approvalRequestDto.getWorkFlowId());
                callableStatement.setString(3, approvalRequestDto.getUserId());
                callableStatement.execute();
            }

            // getting all members of Role

            for(ApprovalRow approvalRow : approvalRequestDto.getApprovalRowList()) {

                String rule = null;

//                String sqlDetail = "INSERT INTO ndc_am_matrix_detail" +
//                        "(matrixid, workflowid, sequenceno, groupno, role, ischecker, rulevalue, rule,approvalorder)" +
//                        "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";


                PreparedStatement statement2 = connection.prepareStatement(Queries.CAM_QUERY.CAM_QUERY3);

                statement2.setLong(1, approvalRequestDto.getMatrixId());
                statement2.setInt(2, approvalRequestDto.getWorkFlowId());
                statement2.setInt(3, approvalRow.getSequenceNo());
                statement2.setInt(4, approvalRow.getGroupNo() == null ? 1 : approvalRow.getGroupNo());
                statement2.setString(5, approvalRow.getRole());
                statement2.setInt(6, approvalRow.getIsChecker());

                if (ApprovalConstants.ANY_ONE_VALUE.equals(approvalRow.getApprovalRule())) {

                    rule = ApprovalConstants.ANY_ONE;

                } else if (ApprovalConstants.ANY_TWO_VALUE.equals(approvalRow.getApprovalRule())) {

                    rule = ApprovalConstants.ANY_TWO;

                } else if (ApprovalConstants.ANY_THREE_VALUE.equals(approvalRow.getApprovalRule())) {

                    rule = ApprovalConstants.ANY_THREE;

                } else if (ApprovalConstants.ANY_FOUR_VALUE.equals(approvalRow.getApprovalRule())) {

                    rule = ApprovalConstants.ANY_FOUR;

                } else if (ApprovalConstants.ALL_VALUE.equals(approvalRow.getApprovalRule())) {

                    rule = ApprovalConstants.ALL;

                }

                statement2.setInt(7, approvalRow.getApprovalRule());
                statement2.setString(8, rule);
                statement2.setInt(9, approvalRow.getGroupNo() == null ? 1 : approvalRow.getGroupNo());

                statement2.executeUpdate();

            }

            for ( WorkFlowFeatureAction workFlowFeatureAction : approvalRequestDto.getWorkFlowFeatureActions() ) {

//                String sql="INSERT INTO ndc_am_workflow " +
//                        "(matrixid, workflowid, issequential, featureactionid, minamount, maxamount) " +
//                        "VALUES(?, ?, ?, ?, ?, ?) ";

                PreparedStatement statement1 = connection.prepareStatement(Queries.CAM_QUERY.CAM_QUERY4);
                statement1.setLong(1, approvalRequestDto.getMatrixId());
                statement1.setInt(2, approvalRequestDto.getWorkFlowId());
                statement1.setInt(3, workFlowFeatureAction.getIsSequential());
                statement1.setString(4, workFlowFeatureAction.getFeatureActionId());
                statement1.setDouble(5, workFlowFeatureAction.getMinAmount());
                statement1.setDouble(6, workFlowFeatureAction.getMaxAmount());

                statement1.executeUpdate();

            }

            result="Approval Matrix Successfully created :"+approvalRequestDto.getMatrixId();

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
