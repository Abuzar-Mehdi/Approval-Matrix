package ndc.approvalmatrix.service.javaservice;

import com.google.gson.Gson;
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

//    private static String json="{  \n" +
//            "  \"featureActions\": [\n" +
//            "    {\n" +
//            "      \"featureActionId\": \"ACH_COLLECTION_CREATE\",\n" +
//            "      \"name\": \"Initiate ACH Collection\",\n" +
//            "      \"isEnabled\": 1\n" +
//            "    },\n" +
//            "\t{\n" +
//            "      \"featureActionId\": \"ACH_PAYMENT\",\n" +
//            "      \"name\": \"Initiate ACH Payment\",\n" +
//            "      \"isEnabled\": 1\n" +
//            "    }\n" +
//            "  ]\n" +
//            "}";



    @Override
    public Object invoke(String s, Object[] objects, DataControllerRequest dataControllerRequest, DataControllerResponse dataControllerResponse) throws Exception {

        Result result = new Result();

       try {

           Connection connection = new DatabaseConnection().getDatabaseConnection();
           connection.setAutoCommit(false);

           FeatureActionConfigDto featureActionConfigDto = new Gson().fromJson(dataControllerRequest.getParameter("featureActions"), FeatureActionConfigDto.class);

           featureActionConfigDto.setContractId(dataControllerRequest.getParameter("contractId"));
           featureActionConfigDto.setCreateBy(dataControllerRequest.getParameter("createBy"));
           featureActionConfigDto.setModifyBy(dataControllerRequest.getParameter("modifyBy"));
           featureActionConfigDto.setIsInsert(Integer.valueOf(dataControllerRequest.getParameter("isInsert")));

           FeatureActionConfigDao featureActionConfigDao = new FeatureActionConfigDao(connection);
           result.addParam("data", featureActionConfigDao.createFeatureActionConfiguration(featureActionConfigDto));

           connection.commit();
           connection.close();

       }catch (Exception exception){
           exception.printStackTrace();
       }

        return result;
    }

   /* public static void main(String[] args) throws SQLException {


        Connection connection = new DatabaseConnection().getDatabaseConnection();
        connection.setAutoCommit(false);

        FeatureActionConfigDto featureActionConfigDto = new Gson().fromJson(json1, FeatureActionConfigDto.class);

        featureActionConfigDto.setContractId("8436131351");
        featureActionConfigDto.setCreateBy("11883");
        featureActionConfigDto.setModifyBy("11883");
        featureActionConfigDto.setIsInsert(1);

        FeatureActionConfigDao featureActionConfigDao = new FeatureActionConfigDao(connection);
        //result.addParam("data", featureActionConfigDao.createFeatureActionConfiguration(featureActionConfigDto));

        System.out.println("featureActionConfigDao = " + featureActionConfigDao.createFeatureActionConfiguration(featureActionConfigDto));
        connection.commit();
        connection.close();
    }*/
}
