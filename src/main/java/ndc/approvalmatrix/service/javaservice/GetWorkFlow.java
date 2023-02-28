package ndc.approvalmatrix.service.javaservice;

import com.google.gson.Gson;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import ndc.approvalmatrix.service.javaservice.commons.DatabaseConnection;
import ndc.approvalmatrix.service.javaservice.dao.GetWorkFlowDao;
import ndc.approvalmatrix.service.javaservice.dto.ApprovalWorkFlow;

import java.sql.Connection;
import java.sql.SQLException;

public class GetWorkFlow implements JavaService2 {

    static  String json ="{\"matrixId\":13,\"approvalRowList\":[{\"workflowId\":3,\"sequenceNo\":1,\"groupNo\":1,\"approvalRule\":2,\"role\":\"Approver\",\"isChecker\":1},{\"workflowId\":3,\"sequenceNo\":1,\"groupNo\":2,\"approvalRule\":3,\"role\":\"Authorizer\",\"isChecker\":0},{\"workflowId\":3,\"sequenceNo\":2,\"groupNo\":1,\"approvalRule\":-1,\"role\":\"Administrator\",\"isChecker\":1}],\"accountNo\":\"1234545667\",\"workFlowFeatureActions\":[{\"workflowId\":3,\"isSequential\":0,\"featureActionId\":\"BILL_PAY_CREATE\",\"minAmount\":100001,\"maxAmount\":9999999}]}";
  
    @Override
    public Object invoke(String s, Object[] objects, DataControllerRequest dataControllerRequest, DataControllerResponse dataControllerResponse) {

        Result result = new Result();
        Connection connection = null;
        try {

            ServicesManager sm = dataControllerRequest.getServicesManager();
            ConfigurableParametersHelper paramHelper = sm.getConfigurableParametersHelper();

            String hostURL = paramHelper.getServerProperty("DBX_HOST_URL").split("//")[1];
            String dbxPort = paramHelper.getServerProperty("DBX_PORT");
            String dbxSchemaName = paramHelper.getServerProperty("DBX_SCHEMA_NAME");
            String dbxDbUsername = paramHelper.getServerProperty("DBX_DB_USERNAME");
            String dbxDbPassword = paramHelper.getServerProperty("DBX_DB_PASSWORD");


            connection = new DatabaseConnection().getDatabaseConnection(hostURL.split(":")[0],dbxPort,dbxSchemaName,dbxDbUsername,dbxDbPassword);

            connection.setAutoCommit(false);

            ApprovalWorkFlow approvalWorkFlow = new ApprovalWorkFlow();

            //approvalWorkFlow.setMatrixId(Long.valueOf(dataControllerRequest.getParameter("matrixId")));
            approvalWorkFlow.setContractId(dataControllerRequest.getParameter("contractId"));
            approvalWorkFlow.setAccountNo(dataControllerRequest.getParameter("accountNo"));
            approvalWorkFlow.setWorkflowId(Integer.valueOf(dataControllerRequest.getParameter("workflowId")));

            GetWorkFlowDao getWorkFlowDao = new GetWorkFlowDao(connection);

            result.addParam("data",new Gson().toJson(getWorkFlowDao.getApprovalWorkFlow(approvalWorkFlow)));

            connection.commit();
            connection.close();

        }catch (Exception exception){

            try {
                connection.rollback();
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

//    public static void main(String[] args)  {
//
//
//        Connection connection = null;
//        try {
//
//            connection = new DatabaseConnection().getDatabaseConnection();
//            connection.setAutoCommit(false);
//
//
//            ApprovalWorkFlow approvalWorkFlow = new ApprovalWorkFlow();
//
//          //  approvalWorkFlow.setMatrixId(Long.valueOf(dataControllerRequest.getParameter("matrixId")));
//            approvalWorkFlow.setContractId("8436131351");
//            approvalWorkFlow.setAccountNo("1234545667");
//            approvalWorkFlow.setWorkflowId(1);
//
//
//            GetWorkFlowDao getWorkFlowDao = new GetWorkFlowDao(connection);
//
//
//            System.out.println("workflow :"+ new Gson().toJson(getWorkFlowDao.getApprovalWorkFlow(approvalWorkFlow)));
//
//            connection.commit();
//            connection.close();
//
//        }catch (Exception exception){
//
//
//        }
//
//    }
}
