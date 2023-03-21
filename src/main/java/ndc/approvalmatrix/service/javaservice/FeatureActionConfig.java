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
import ndc.approvalmatrix.service.javaservice.dao.CreateNewRequestDao;
import ndc.approvalmatrix.service.javaservice.dao.FeatureActionConfigDao;
import ndc.approvalmatrix.service.javaservice.dto.ApprovalRequestDto;
import ndc.approvalmatrix.service.javaservice.dto.FeatureAction;
import ndc.approvalmatrix.service.javaservice.dto.FeatureActionConfigDto;
import ndc.approvalmatrix.service.javaservice.dto.RequestDto;

import java.sql.Connection;
import java.sql.SQLException;

public class FeatureActionConfig implements JavaService2 {

    private static String json="{\n" +
            "  \n" +
            "    \"featureActions\": [\n" +
            "      {\n" +
            "        \"featureActionId\": \"BILL_PAY_CREATE\",\n" +
            "        \"name\": \"Initiate Bill Payments\",\n" +
            "        \"isEnabled\": 1,\n" +
            "        \"minAmount\": 47,\n" +
            "        \"maxAmount\": 68\n" +
            "      },\n" +
            "      {\n" +
            "        \"featureActionId\": \"INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE\",\n" +
            "        \"name\": \"Create/Edit Transfer\",\n" +
            "        \"isEnabled\": 1,\n" +
            "        \"minAmount\": 47,\n" +
            "        \"maxAmount\": 68\n" +
            "      }\n" +
            "    ]\n" +
            "  }";

    static String json2 = "{\"featureActions\":[{\"featureActionId\":\"BILL_PAY_CREATE\",\"name\":\"Initiate Bill Payments\",\"isEnabled\":1}]}";

    @Override
    public Object invoke(String s, Object[] objects, DataControllerRequest dataControllerRequest, DataControllerResponse dataControllerResponse)  {

        Result result = new Result();
        Connection connection =null;

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

           FeatureActionConfigDto featureActionConfigDto = new Gson().fromJson(dataControllerRequest.getParameter("featureActions"), FeatureActionConfigDto.class);

           featureActionConfigDto.setContractId(dataControllerRequest.getParameter("contractId"));
           featureActionConfigDto.setCreateBy(dataControllerRequest.getParameter("createBy"));
           featureActionConfigDto.setModifyBy(dataControllerRequest.getParameter("modifyBy"));
           featureActionConfigDto.setIsInsert(Integer.valueOf(dataControllerRequest.getParameter("isInsert")));
           featureActionConfigDto.setAccountNo(dataControllerRequest.getParameter("accountNo"));
           FeatureActionConfigDao featureActionConfigDao = new FeatureActionConfigDao(connection);
           result.addParam("data", featureActionConfigDao.createFeatureActionConfiguration(featureActionConfigDto));

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

    public static void main(String[] args) throws SQLException {


        Connection connection = new DatabaseConnection().getDatabaseConnection();
        connection.setAutoCommit(false);

        FeatureActionConfigDto featureActionConfigDto = new Gson().fromJson(json2, FeatureActionConfigDto.class);

        featureActionConfigDto.setContractId("8436131351");
        featureActionConfigDto.setCreateBy("11883");
        featureActionConfigDto.setModifyBy("11883");
        featureActionConfigDto.setIsInsert(0);
        featureActionConfigDto.setAccountNo("1234545667");

        FeatureActionConfigDao featureActionConfigDao = new FeatureActionConfigDao(connection);
        //result.addParam("data", featureActionConfigDao.createFeatureActionConfiguration(featureActionConfigDto));

        System.out.println("featureActionConfigDao = " + featureActionConfigDao.createFeatureActionConfiguration(featureActionConfigDto));
        connection.commit();
        connection.close();
    }
}
