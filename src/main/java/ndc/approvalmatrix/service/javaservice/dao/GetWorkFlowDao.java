package ndc.approvalmatrix.service.javaservice.dao;

import ndc.approvalmatrix.service.javaservice.commons.ApprovalConstants;
import ndc.approvalmatrix.service.javaservice.commons.Queries;
import ndc.approvalmatrix.service.javaservice.commons.StoredProcedure;
import ndc.approvalmatrix.service.javaservice.dto.ApprovalRow;
import ndc.approvalmatrix.service.javaservice.dto.ApprovalWorkFlow;
import ndc.approvalmatrix.service.javaservice.dto.WorkFlowFeatureAction;
import org.json.HTTP;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GetWorkFlowDao {

    private Connection connection;


    public GetWorkFlowDao() {

    }

    public GetWorkFlowDao(Connection connection) {
        super();
        this.connection = connection;
    }

    public ApprovalWorkFlow getApprovalWorkFlow(ApprovalWorkFlow approvalWorkFlow){

        String result = null;
        List<ApprovalRow> approvalRows = new ArrayList<>();
        List<WorkFlowFeatureAction> workFlowFeatureActions = new ArrayList<>();

        try {

            //String sqlMain = "SELECT id FROM ndc_am_matrix where contractid=? and accountno=? and softdelete=0 ";

            PreparedStatement statementM = connection.prepareStatement(Queries.GW_QUERIES.GW_QUERY1);

            statementM.setString(1,approvalWorkFlow.getContractId()); //ContractID
            statementM.setString(2,approvalWorkFlow.getAccountNo()); //AccountNo

            ResultSet resultSet =  statementM.executeQuery();

            if(resultSet.next()){

                approvalWorkFlow.setMatrixId(resultSet.getLong("id"));
            }

           // String sqlApprovalRow="SELECT workflowid,sequenceno,groupno,role,ischecker,rulevalue FROM ndc_am_matrix_detail where matrixid=? AND workflowid=? ";

            PreparedStatement statement1 = connection.prepareStatement(Queries.GW_QUERIES.GW_QUERY2);

            statement1.setLong(1,approvalWorkFlow.getMatrixId()); //Matrix
            statement1.setInt(2,approvalWorkFlow.getWorkflowId()); //workflow
            ResultSet resultSet1 =  statement1.executeQuery();

            if(resultSet1.next() == false ) {

                approvalWorkFlow.setResponse(444);

            }else{

                do {

                    ApprovalRow approvalRow = new ApprovalRow();
                    approvalRow.setWorkflowId(resultSet1.getInt("workflowid"));
                    approvalRow.setSequenceNo(resultSet1.getInt("sequenceno"));
                    approvalRow.setGroupNo(resultSet1.getInt("groupno"));
                    approvalRow.setRole(resultSet1.getString("role"));
                    approvalRow.setIsChecker(resultSet1.getInt("ischecker"));
                    //approvalRow.setApprovalRule(resultSet1.getInt("rulevalue"));

                    Integer rule = resultSet1.getInt("rulevalue");

                    if (ApprovalConstants.ANY_ONE_VALUE.equals(rule)) {

                        //rule = ApprovalConstants.ANY_ONE_VALUE;
                        approvalRow.setApprovalRule(ApprovalConstants.ANY_ONE);

                    } else if (ApprovalConstants.ANY_TWO_VALUE.equals(rule)) {

                        // rule = ApprovalConstants.ANY_TWO_VALUE;
                        approvalRow.setApprovalRule(ApprovalConstants.ANY_TWO);

                    } else if (ApprovalConstants.ANY_THREE_VALUE.equals(rule)) {

                        //rule = ApprovalConstants.ANY_THREE_VALUE;
                        approvalRow.setApprovalRule(ApprovalConstants.ANY_THREE);

                    } else if (ApprovalConstants.ANY_FOUR_VALUE.equals(rule)) {

                        // rule = ApprovalConstants.ANY_FOUR_VALUE;
                        approvalRow.setApprovalRule(ApprovalConstants.ANY_FOUR);

                    } else if (ApprovalConstants.ALL_VALUE.equals(rule)) {

                        // rule = ApprovalConstants.ALL_VALUE;
                        approvalRow.setApprovalRule(ApprovalConstants.ALL);

                    }

                    approvalRows.add(approvalRow);

                }while (resultSet1.next());

                approvalWorkFlow.setResponse(200);

            }

            CallableStatement callableStatement = connection.prepareCall("{CALL "+ StoredProcedure.GET_FEATURE_ACTION +"(?,?)}");
            callableStatement.setString(1, approvalWorkFlow.getContractId());
            callableStatement.setString(2, approvalWorkFlow.getAccountNo());

            callableStatement.execute();

            ResultSet resultSet2 =  callableStatement.getResultSet();

            if(resultSet2.next() == false ) {

                approvalWorkFlow.setResponse(444);

            }else{

                do {

                    //String sqlWorkflowFeature="SELECT workflowid,issequential,featureactionid,minamount,maxamount FROM ndc_am_workflow where matrixid=? AND workflowid=? ";

                    PreparedStatement statement2 = connection.prepareStatement(Queries.GW_QUERIES.GW_QUERY3);

                    statement2.setLong(1, approvalWorkFlow.getMatrixId()); //Matrix
                    statement2.setInt(2, approvalWorkFlow.getWorkflowId()); //workflow
                    statement2.setString(3, resultSet2.getString("featureactionid"));
                    ResultSet resultSet3 = statement2.executeQuery();

                    WorkFlowFeatureAction workFlowFeatureAction;

                    if (resultSet3.next()) {

                        workFlowFeatureAction = new WorkFlowFeatureAction();

                        workFlowFeatureAction.setWorkflowId(resultSet3.getInt("workflowid"));
                        workFlowFeatureAction.setIsSequential(resultSet3.getInt("issequential"));
                        workFlowFeatureAction.setFeatureActionId(resultSet3.getString("featureactionid"));
                        workFlowFeatureAction.setMinAmount(resultSet3.getDouble("minamount"));
                        workFlowFeatureAction.setMaxAmount(resultSet3.getDouble("maxamount"));


                    } else {

                        workFlowFeatureAction = new WorkFlowFeatureAction();

                        workFlowFeatureAction.setWorkflowId(approvalWorkFlow.getWorkflowId());
                        workFlowFeatureAction.setIsSequential(resultSet2.getInt("issequential"));
                        workFlowFeatureAction.setFeatureActionId(resultSet2.getString("featureactionid"));
                        workFlowFeatureAction.setMinAmount(0d);
                        workFlowFeatureAction.setMaxAmount(0d);
                    }

                    workFlowFeatureActions.add(workFlowFeatureAction);

                }while (resultSet2.next());

                approvalWorkFlow.setResponse(200);

            }

            approvalWorkFlow.setApprovalRowList(approvalRows);
            approvalWorkFlow.setWorkFlowFeatureActions(workFlowFeatureActions);

        }catch (Exception exception){

            try {
                exception.printStackTrace();
                result ="Error in getting Approval workFlow :";
                connection.rollback();
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
        return  approvalWorkFlow;
    }

}
