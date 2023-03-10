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


//    static String json ="{\n" +
//            " \"approvalRowList\": [\n" +
//            "    {\n" +
//            "      \"sequenceNo\": 1,\n" +
//            "       \"groupNo\": 1 ,\n" +
//            "      \"approvalRule\": \"2\",     \n" +
//            "      \"role\": \"Approver\"\n" +
//            "    },\n" +
//            "    {\n" +
//            "      \"sequenceNo\": 1,\n" +
//            "       \"groupNo\": 2, \n" +
//            "      \"approvalRule\": \"3\",\n" +
//            "      \"role\": \"Authorizer\"\n" +
//            "    },\n" +
//            "    {\n" +
//            "      \"sequenceNo\": 2,\n" +
//            "      \"groupNo\" : 1, \n" +
//            "      \"approvalRule\": \"-1\",\n" +
//            "      \"role\": \"Administrator\"\n" +
//            "    }\n" +
//            "  ]\n" +
//            "  }";
//
//    static String json1 ="{\n" +
//            "  \"approvalRowList\": [\n" +
//            "    {\n" +
//            "      \"workflowId\": 1,\n" +
//            "      \"sequenceNo\": 1,\n" +
//            "      \"groupNo\": 1,\n" +
//            "      \"approvalRule\": \"2\",\n" +
//            "      \"role\": \"Approver\"\n" +
//            "    },\n" +
//            "    {\n" +
//            "      \"workflowId\": 1,\n" +
//            "      \"sequenceNo\": 1,\n" +
//            "      \"groupNo\": 2,\n" +
//            "      \"approvalRule\": \"3\",\n" +
//            "      \"role\": \"Authorizer\"\n" +
//            "    },\n" +
//            "    {\n" +
//            "      \"workflowId\": 1,\n" +
//            "      \"sequenceNo\": 3,\n" +
//            "      \"groupNo\": 1,\n" +
//            "      \"approvalRule\": \"-1\",\n" +
//            "      \"role\": \"Administrator\"\n" +
//            "    },\n" +
//            "    {\n" +
//            "      \"workflowId\": 2,\n" +
//            "      \"sequenceNo\": 1,\n" +
//            "      \"groupNo\": 1,\n" +
//            "      \"approvalRule\": \"2\",\n" +
//            "      \"role\": \"Approver\"\n" +
//            "    },\n" +
//            "    {\n" +
//            "      \"workflowId\": 2,\n" +
//            "      \"sequenceNo\": 1,\n" +
//            "      \"groupNo\": 2,\n" +
//            "      \"approvalRule\": \"3\",\n" +
//            "      \"role\": \"Authorizer\"\n" +
//            "    }\n" +
//            "  ]\n" +
//            "}";
//
    static  String json2="{\n" +
            "  \"requestId\": 0,\n" +
            "  \"userId\": \"4646038518\",\n" +
            "  \"contractId\": \"8436131351\",\n" +
            "  \"accountNo\": \"1234545667\",\n" +
            "  \"isSequential\": 0,\n" +
            "  \"coreCustomerId\": \"102190\",\n" +
            " \n" +
            " \"approvalRowList\": [\n" +
            "    {\n" +
            "\t  \"workflowId\":1,\t\n" +
            "      \"sequenceNo\": 1,\n" +
            "       \"groupNo\": 1 ,\n" +
            "      \"approvalRule\": \"2\",     \n" +
            "      \"role\": \"Approver\"\n" +
            "    },\n" +
            "    {\n" +
            "\t  \"workflowId\":1,\t\n" +
            "      \"sequenceNo\": 1,\n" +
            "       \"groupNo\": 2, \n" +
            "      \"approvalRule\": \"3\",\n" +
            "      \"role\": \"Authorizer\"\n" +
            "    },\n" +
            "    {\n" +
            "\t  \"workflowId\":1,\t\n" +
            "      \"sequenceNo\": 1,\n" +
            "      \"groupNo\" : 1, \n" +
            "      \"approvalRule\": \"-1\",\n" +
            "      \"role\": \"Administrator\"\n" +
            "    },\n" +
            "\t  {\n" +
            "\t  \"workflowId\":2,\t\n" +
            "      \"sequenceNo\": 1,\n" +
            "       \"groupNo\": 1 ,\n" +
            "      \"approvalRule\": \"2\",     \n" +
            "      \"role\": \"Approver\"\n" +
            "    },\n" +
            "    {\n" +
            "\t  \"workflowId\":2,\t\n" +
            "      \"sequenceNo\": 1,\n" +
            "       \"groupNo\": 2, \n" +
            "      \"approvalRule\": \"3\",\n" +
            "      \"role\": \"Authorizer\"\n" +
            "    }\n" +
            "  ]\n" +
            " ,\n" +
            "  \"workFlowFeatureActions\": [\n" +
            "    {\n" +
            "      \"workflowId\": 1,\n" +
            "      \"isSequential\": 0,\n" +
            "      \"featureActionId\": \"BILL_PAY_CREATE\",\n" +
            "      \"minAmount\": 1,\n" +
            "      \"maxAmount\": 50000\n" +
            "    },\n" +
            "\t{\n" +
            "      \"workflowId\": 2,\n" +
            "      \"isSequential\": 1,\n" +
            "      \"featureActionId\": \"BILL_PAY_CREATE\",\n" +
            "      \"minAmount\": 50001,\n" +
            "      \"maxAmount\": 100000\n" +
            "    }\n" +
            "\t\n" +
            "  ]\n" +
            "}";

    static String json3 = "{\"requestId\":\"0\",\"userId\":\"4646038518\",\"contractId\":\"8436131351\",\"isSequential\":\"1\",\"coreCustomerId\":\"102190\",\"approvalRowList\":[{\"sequenceNo\":1,\"groupNo\":1,\"approvalRule\":2,\"role\":\"Approver\",\"isChecker\":1},{\"sequenceNo\":1,\"groupNo\":2,\"approvalRule\":3,\"role\":\"Authorizer\",\"isChecker\":0},{\"sequenceNo\":2,\"groupNo\":1,\"approvalRule\":-1,\"role\":\"Administrator\",\"isChecker\":1},{\"workflowId\":2,\"sequenceNo\":1,\"groupNo\":1,\"approvalRule\":2,\"role\":\"Approver\",\"isChecker\":1},{\"workflowId\":2,\"sequenceNo\":1,\"groupNo\":2,\"approvalRule\":3,\"role\":\"Authorizer\",\"isChecker\":0}],\"accountNo\":\"1234545667\",\"workFlowFeatureActions\":[{\"workflowId\":1,\"isSequential\":0,\"featureActionId\":\"BILL_PAY_CREATE\",\"minAmount\":1,\"maxAmount\":50000},{\"workflowId\":2,\"isSequential\":1,\"featureActionId\":\"BILL_PAY_CREATE\",\"minAmount\":50001,\"maxAmount\":100000}]}";
    static String json4 = "{\"userId\":\"4646038518\",\"contractId\":\"8436131351\",\"accountNo\":\"1234545667\",\"approvalRowList\":[{\"sequenceNo\":1,\"groupNo\":1,\"approvalRule\":2,\"role\":\"Approver\",\"isChecker\":1},{\"sequenceNo\":1,\"groupNo\":2,\"approvalRule\":3,\"role\":\"Authorizer\",\"isChecker\":0},{\"sequenceNo\":2,\"groupNo\":1,\"approvalRule\":-1,\"role\":\"Administrator\",\"isChecker\":1}],\"workFlowFeatureActions\":[{\"isSequential\":0,\"featureActionId\":\"BILL_PAY_CREATE\",\"minAmount\":100001,\"maxAmount\":125000}],\"isEdit\":\"0\",\"workFlowId\":\"0\"}";
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

            CreateApprovalMatrixDao approvalMatrixDao = new CreateApprovalMatrixDao(connection);
            result.addParam("data", approvalMatrixDao.createApprovalMatrix(approvalRequestDto));

            connection.commit();
            connection.close();

        }catch (Exception e){
            try {
                e.printStackTrace();
                connection.rollback();
                connection.close();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        return result;
    }

    public static void main(String[] args) throws  SQLException {


        Connection connection = new DatabaseConnection().getDatabaseConnection();
        connection.setAutoCommit(false);         ;


        ApprovalRequestDto approvalRequestDto =  new Gson().fromJson(json4, ApprovalRequestDto.class);
//        approvalRequestDto.setUserId("4646038518");
//        approvalRequestDto.setContractId("8436131351");
//        approvalRequestDto.setAccountNo("1234545667");
//        approvalRequestDto.setIsSequential(1);
//        approvalRequestDto.setCoreCustomerId("102190");

        System.out.println("approvalRowList = " + new Gson().toJson(approvalRequestDto));

        //CreateApprovalMatrixDao approvalMatrixDao = new CreateApprovalMatrixDao(connection);
        //String Message = approvalMatrixDao.createApprovalMatrix(approvalRequestDto);

      //  System.out.println("Message = " + Message);

        connection.commit();
        connection.close();
    }

}
