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

    private static String json="{\"featureActions\":[{\"featureActionId\":\"ACH_COLLECTION_CREATE\",\"name\":\"Initiate ACH Collection\",\"isEnabled\":true},{\"featureActionId\":\"ACH_FILE_UPLOAD\",\"name\":\"Upload Files\",\"isEnabled\":false},{\"featureActionId\":\"ACH_PAYMENT_CREATE\",\"name\":\"Initiate ACH Payment\",\"isEnabled\":true},{\"featureActionId\":\"BILL_PAY_CREATE\",\"name\":\"Initiate Bill Payments\",\"isEnabled\":true},{\"featureActionId\":\"BULK_PAYMENT_REQUEST_SUBMIT\",\"name\":\"Submit Request\",\"isEnabled\":true},{\"featureActionId\":\"DOMESTIC_WIRE_TRANSFER_CREATE\",\"name\":\"Initiate Transfer\",\"isEnabled\":true},{\"featureActionId\":\"INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE\",\"name\":\"Create/Edit Transfer\",\"isEnabled\":true},{\"featureActionId\":\"INTERNATIONAL_WIRE_TRANSFER_CREATE\",\"name\":\"Initiate Transfer\",\"isEnabled\":true},{\"featureActionId\":\"INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE\",\"name\":\"Create/Edit Transfer\",\"isEnabled\":true},{\"featureActionId\":\"INTRA_BANK_FUND_TRANSFER_CREATE\",\"name\":\"Create/Edit Transfer\",\"isEnabled\":true},{\"featureActionId\":\"ORDER_BLOTTER_OPEN_ORDER_CREATE\",\"name\":\"Open Orders Create\",\"isEnabled\":true},{\"featureActionId\":\"P2P_CREATE\",\"name\":\"Initiate Transfer\",\"isEnabled\":true},{\"featureActionId\":\"PAY_MULTIPLE_BENEFICIARIES_CREATE_TRANSFER\",\"name\":\"Pay Multiple Beneficiaries Create Transfer\",\"isEnabled\":true},{\"featureActionId\":\"TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE\",\"name\":\"Create/Edit Transfer\",\"isEnabled\":true},{\"featureActionId\":\"UNIFIED_TRANSFER_CREATE\",\"name\":\"Show Unified Transfer\",\"isEnabled\":true}]}";


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
           //featureActionConfigDto.setIsInsert(Integer.valueOf(dataControllerRequest.getParameter("isInsert")));
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

        FeatureActionConfigDto featureActionConfigDto = new Gson().fromJson(json, FeatureActionConfigDto.class);

        featureActionConfigDto.setContractId("8436131351");
        featureActionConfigDto.setCreateBy("abuzar1");
        featureActionConfigDto.setModifyBy("aslam");
        featureActionConfigDto.setAccountNo("826612");

        FeatureActionConfigDao featureActionConfigDao = new FeatureActionConfigDao(connection);
        //result.addParam("data", featureActionConfigDao.createFeatureActionConfiguration(featureActionConfigDto));

        System.out.println("featureActionConfigDao = " + featureActionConfigDao.createFeatureActionConfiguration(featureActionConfigDto));
        connection.commit();
        connection.close();
    }
}
