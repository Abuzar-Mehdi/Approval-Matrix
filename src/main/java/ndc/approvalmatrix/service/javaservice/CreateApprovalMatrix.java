package ndc.approvalmatrix.service.javaservice;

import com.google.gson.Gson;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import ndc.approvalmatrix.service.javaservice.commons.DatabaseConnection;
import ndc.approvalmatrix.service.javaservice.dao.CreateApprovalMatrixDao;
import ndc.approvalmatrix.service.javaservice.dto.ApprovalRequestDto;

import java.sql.Connection;
import java.sql.SQLException;

public class CreateApprovalMatrix implements JavaService2 {

   // static  String json2="{\"requestId\":0,\"userId\":\"4646038518\",\"contractId\":\"8436131351\",\"accountNo\":\"1234545667\",\"workFlowId\":0,\"approvalRowList\":[{\"workflowId\":1,\"sequenceNo\":1,\"groupNo\":1,\"approvalRule\":\"2\",\"role\":\"Approver\"},{\"workflowId\":1,\"sequenceNo\":1,\"groupNo\":2,\"approvalRule\":\"3\",\"role\":\"Authorizer\"},{\"workflowId\":1,\"sequenceNo\":1,\"groupNo\":1,\"approvalRule\":\"-1\",\"role\":\"Administrator\"},{\"workflowId\":2,\"sequenceNo\":1,\"groupNo\":1,\"approvalRule\":\"2\",\"role\":\"Approver\"},{\"workflowId\":2,\"sequenceNo\":1,\"groupNo\":2,\"approvalRule\":\"3\",\"role\":\"Authorizer\"}],\"workFlowFeatureActions\":[{\"workflowId\":1,\"isSequential\":0,\"featureActionId\":\"BILL_PAY_CREATE\",\"minAmount\":1,\"maxAmount\":50000},{\"workflowId\":2,\"isSequential\":1,\"featureActionId\":\"BILL_PAY_CREATE\",\"minAmount\":50001,\"maxAmount\":100000}]}";

    //static String json3 = "{\"requestId\":\"0\",\"userId\":\"4646038518\",\"contractId\":\"8436131351\",\"isSequential\":\"1\",\"coreCustomerId\":\"102190\",\"approvalRowList\":[{\"sequenceNo\":1,\"groupNo\":1,\"approvalRule\":2,\"role\":\"Approver\",\"isChecker\":1},{\"sequenceNo\":1,\"groupNo\":2,\"approvalRule\":3,\"role\":\"Authorizer\",\"isChecker\":0},{\"sequenceNo\":2,\"groupNo\":1,\"approvalRule\":-1,\"role\":\"Administrator\",\"isChecker\":1},{\"workflowId\":2,\"sequenceNo\":1,\"groupNo\":1,\"approvalRule\":2,\"role\":\"Approver\",\"isChecker\":1},{\"workflowId\":2,\"sequenceNo\":1,\"groupNo\":2,\"approvalRule\":3,\"role\":\"Authorizer\",\"isChecker\":0}],\"accountNo\":\"1234545667\",\"workFlowFeatureActions\":[{\"workflowId\":1,\"isSequential\":0,\"featureActionId\":\"BILL_PAY_CREATE\",\"minAmount\":1,\"maxAmount\":50000},{\"workflowId\":2,\"isSequential\":1,\"featureActionId\":\"BILL_PAY_CREATE\",\"minAmount\":50001,\"maxAmount\":100000}]}";
    //static String json4 = "{\"userId\":\"4646038518\",\"contractId\":\"8436131351\",\"accountNo\":\"1234545667\",\"approvalRowList\":[{\"sequenceNo\":1,\"groupNo\":1,\"approvalRule\":2,\"role\":\"Approver\",\"isChecker\":1},{\"sequenceNo\":1,\"groupNo\":2,\"approvalRule\":3,\"role\":\"Authorizer\",\"isChecker\":0},{\"sequenceNo\":2,\"groupNo\":1,\"approvalRule\":-1,\"role\":\"Administrator\",\"isChecker\":1}],\"workFlowFeatureActions\":[{\"isSequential\":0,\"featureActionId\":\"BILL_PAY_CREATE\",\"minAmount\":100001,\"maxAmount\":125000}],\"isEdit\":\"0\",\"workFlowId\":\"0\"}";

    //static String json5 = "{\"userId\":\"4646038518\",\"contractId\":\"8436131351\",\"accountNo\":\"826612\",\"approvalRowList\":[{\"workflowId\":0,\"sequenceNo\":1,\"groupNo\":1,\"approvalRule\":\"Any one approval\",\"role\":\"Administrator\",\"isChecker\":0}],\"workFlowFeatureActions\":[{\"workflowId\":0,\"isSequential\":0,\"featureActionId\":\"ACH_PAYMENT_CREATE\",\"minAmount\":\"100\",\"maxAmount\":\"1\"},{\"workflowId\":0,\"isSequential\":0,\"featureActionId\":\"BILL_PAY_CREATE\"},{\"workflowId\":0,\"isSequential\":0,\"featureActionId\":\"BULK_PAYMENT_REQUEST_SUBMIT\"},{\"workflowId\":0,\"isSequential\":0,\"featureActionId\":\"INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE\"},{\"workflowId\":0,\"isSequential\":0,\"featureActionId\":\"INTERNATIONAL_WIRE_TRANSFER_CREATE\"},{\"workflowId\":0,\"isSequential\":0,\"featureActionId\":\"DOMESTIC_WIRE_TRANSFER_CREATE\"}],\"isEdit\":\"0\",\"workFlowId\":\"0\"}";
   static  String json6 ="{\"userId\":\"admin1\",\"contractId\":\"8436131351\",\"accountNo\":\"826612\",\"workFlowId\":0,\"approvalRowList\":[{\"workflowId\":0,\"sequenceNo\":1,\"groupNo\":1,\"approvalRule\":\"Any three approvals\",\"role\":\"Administrator\",\"isChecker\":0}],\"workFlowFeatureActions\":[{\"workflowId\":0,\"isSequential\":0,\"featureActionId\":\"ACH_PAYMENT_CREATE\",\"minAmount\":1.0,\"maxAmount\":10.0},{\"workflowId\":0,\"isSequential\":0,\"featureActionId\":\"BILL_PAY_CREATE\",\"minAmount\":1.0,\"maxAmount\":10.0},{\"workflowId\":0,\"isSequential\":0,\"featureActionId\":\"BULK_PAYMENT_REQUEST_SUBMIT\",\"minAmount\":1.0,\"maxAmount\":10.0},{\"workflowId\":0,\"isSequential\":0,\"featureActionId\":\"INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE\",\"minAmount\":0.0,\"maxAmount\":0.0},{\"workflowId\":0,\"isSequential\":0,\"featureActionId\":\"INTERNATIONAL_WIRE_TRANSFER_CREATE\",\"minAmount\":10.0,\"maxAmount\":20.0},{\"workflowId\":0,\"isSequential\":0,\"featureActionId\":\"DOMESTIC_WIRE_TRANSFER_CREATE\",\"minAmount\":20.0,\"maxAmount\":40.0}],\"isEdit\":0}";

   static String json7="{\"userId\":\"admin1\",\"contractId\":\"8436131351\",\"accountNo\":\"1234545667\",\"approvalRowList\":[{\"workflowId\":1,\"sequenceNo\":0,\"approvalRule\":\"Approver\",\"role\":\"Any two approvals\",\"isChecker\":0},{\"workflowId\":1,\"sequenceNo\":0,\"approvalRule\":\"Authorizer\",\"role\":\"Any three approvals\",\"isChecker\":0},{\"workflowId\":1,\"sequenceNo\":0,\"approvalRule\":\"Administrator\",\"role\":\"All Approvals\",\"isChecker\":0}],\"workFlowFeatureActions\":[{\"workflowId\":1,\"isSequential\":0,\"featureActionId\":\"ACH_PAYMENT_CREATE\",\"minAmount\":1,\"maxAmount\":100000},{\"workflowId\":1,\"isSequential\":0,\"featureActionId\":\"BILL_PAY_CREATE\",\"minAmount\":1,\"maxAmount\":50000},{\"workflowId\":1,\"isSequential\":0,\"featureActionId\":\"BULK_PAYMENT_REQUEST_SUBMIT\",\"minAmount\":\"1\",\"maxAmount\":\"100\"},{\"workflowId\":1,\"isSequential\":0,\"featureActionId\":\"INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE\",\"minAmount\":0,\"maxAmount\":0},{\"workflowId\":1,\"isSequential\":0,\"featureActionId\":\"INTERNATIONAL_WIRE_TRANSFER_CREATE\",\"minAmount\":0,\"maxAmount\":0},{\"workflowId\":1,\"isSequential\":0,\"featureActionId\":\"DOMESTIC_WIRE_TRANSFER_CREATE\",\"minAmount\":0,\"maxAmount\":0}],\"isEdit\":1,\"workFlowId\":1}";

   static String json8="{\"userId\":\"admin1\",\"contractId\":\"8436131351\",\"accountNo\":\"826612\",\"approvalRowList\":[{\"workflowId\":9,\"sequenceNo\":1,\"groupNo\":1,\"approvalRule\":\"Any three approvals\",\"role\":\"Administrator\",\"isChecker\":0}],\"workFlowFeatureActions\":[{\"workflowId\":9,\"isSequential\":1,\"featureActionId\":\"BILL_PAY_CREATE\",\"minAmount\":100,\"maxAmount\":300}],\"isEdit\":1,\"workFlowId\":9}";
    @Override
    public Object invoke(String s, Object[] objects, DataControllerRequest dataControllerRequest, DataControllerResponse dataControllerResponse)  {

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



            ApprovalRequestDto approvalRequestDto = new Gson().fromJson(dataControllerRequest.getParameter("approvalRowList"), ApprovalRequestDto.class);
            ApprovalRequestDto approvalRequestDto1 = new Gson().fromJson(dataControllerRequest.getParameter("workFlowFeatureActions"), ApprovalRequestDto.class);


            approvalRequestDto.setWorkFlowFeatureActions(approvalRequestDto1.getWorkFlowFeatureActions());
            approvalRequestDto.setUserId(dataControllerRequest.getParameter("userId"));
            approvalRequestDto.setContractId(dataControllerRequest.getParameter("contractId"));
            approvalRequestDto.setAccountNo(dataControllerRequest.getParameter("accountNo"));
            approvalRequestDto.setIsEdit(Integer.valueOf(dataControllerRequest.getParameter("isEdit")));
            approvalRequestDto.setWorkFlowId(Integer.valueOf(dataControllerRequest.getParameter("workFlowId")));

           // result.addParam("data1",new Gson().toJson(approvalRequestDto));
            CreateApprovalMatrixDao approvalMatrixDao = new CreateApprovalMatrixDao(connection);
            result.addParam("data", approvalMatrixDao.createApprovalMatrix(approvalRequestDto));

            connection.commit();
            connection.close();

        }catch (Exception e){
                result .addParam("error",e.getStackTrace().toString());
        }
        return result;
    }

    public static void main(String[] args) throws  SQLException {


        Connection connection = new DatabaseConnection().getDatabaseConnection();
        connection.setAutoCommit(false);         ;


        ApprovalRequestDto approvalRequestDto =  new Gson().fromJson(json8, ApprovalRequestDto.class);
//        approvalRequestDto.setUserId("4646038518");
//        approvalRequestDto.setContractId("8436131351");
//        approvalRequestDto.setAccountNo("1234545667");
//        approvalRequestDto.setIsSequential(1);
//        approvalRequestDto.setCoreCustomerId("102190");

        System.out.println("approvalRowList = " + new Gson().toJson(approvalRequestDto));

        CreateApprovalMatrixDao approvalMatrixDao = new CreateApprovalMatrixDao(connection);
        String Message = approvalMatrixDao.createApprovalMatrix(approvalRequestDto);

        System.out.println("Message = " + Message);

        connection.commit();
        connection.close();
    }

}
