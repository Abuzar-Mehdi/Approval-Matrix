package ndc.approvalmatrix.service.javaservice.dao;

import ndc.approvalmatrix.service.javaservice.commons.Queries;
import ndc.approvalmatrix.service.javaservice.dto.ApprovalRow;
import ndc.approvalmatrix.service.javaservice.dto.ApprovalWorkFlow;
import ndc.approvalmatrix.service.javaservice.dto.WorkFlowFeatureAction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

            while(resultSet1.next()){

                ApprovalRow approvalRow = new ApprovalRow();
                approvalRow.setWorkflowId(resultSet1.getInt("workflowid"));
                approvalRow.setSequenceNo(resultSet1.getInt("sequenceno"));
                approvalRow.setGroupNo(resultSet1.getInt("groupno"));
                approvalRow.setRole(resultSet1.getString("role"));
                approvalRow.setIsChecker(resultSet1.getInt("ischecker"));
                approvalRow.setApprovalRule(resultSet1.getInt("rulevalue"));

                approvalRows.add(approvalRow);
            }

            //String sqlWorkflowFeature="SELECT workflowid,issequential,featureactionid,minamount,maxamount FROM ndc_am_workflow where matrixid=? AND workflowid=? ";

            PreparedStatement statement2 = connection.prepareStatement(Queries.GW_QUERIES.GW_QUERY3);

            statement2.setLong(1,approvalWorkFlow.getMatrixId()); //Matrix
            statement2.setInt(2,approvalWorkFlow.getWorkflowId()); //workflow
            ResultSet resultSet2 =  statement2.executeQuery();

            while (resultSet2.next()){

                WorkFlowFeatureAction workFlowFeatureAction = new WorkFlowFeatureAction();

                workFlowFeatureAction.setWorkflowId(resultSet2.getInt("workflowid"));
                workFlowFeatureAction.setIsSequential(resultSet2.getInt("issequential"));
                workFlowFeatureAction.setFeatureActionId(resultSet2.getString("featureactionid"));
                workFlowFeatureAction.setMinAmount(resultSet2.getDouble("minamount"));
                workFlowFeatureAction.setMaxAmount(resultSet2.getDouble("maxamount"));

                workFlowFeatureActions.add(workFlowFeatureAction);
            }

            approvalWorkFlow.setApprovalRowList(approvalRows);
            approvalWorkFlow.setWorkFlowFeatureActions(workFlowFeatureActions);

        }catch (Exception exception){

            try {
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
