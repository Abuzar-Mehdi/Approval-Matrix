package ndc.approvalmatrix.service.javaservice;

import com.google.gson.Gson;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.mysql.cj.x.protobuf.MysqlxPrepare;
import ndc.approvalmatrix.service.javaservice.commons.DatabaseConnection;
import ndc.approvalmatrix.service.javaservice.dao.CancelRequestDao;
import ndc.approvalmatrix.service.javaservice.dao.FetchAllMyPendingApprovalsDao;
import ndc.approvalmatrix.service.javaservice.dao.FetchAllMyPendingRequestDao;
import ndc.approvalmatrix.service.javaservice.dto.FetchRequestDto;
import ndc.approvalmatrix.service.javaservice.dto.RequestDto;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class FetchAllMyPendingRequest implements JavaService2 {

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

            RequestDto requestDto = RequestDto.builder()
                    .requesterId(dataControllerRequest.getParameter("requesterId"))
                    .build();

            FetchAllMyPendingRequestDao fetchAllMyPendingRequestDao = new FetchAllMyPendingRequestDao(connection);

           FetchRequestDto fetchRequestDto = fetchAllMyPendingRequestDao.fetchPendingRequest(requestDto);

            JSONObject resultObject = new JSONObject();
            resultObject.put("records",  new JSONObject(fetchRequestDto));
            result = JSONToResult.convert(resultObject.toString());

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

   /* public static void main(String[] args) throws SQLException {

        Connection connection = null;

        try {

            connection = new DatabaseConnection().getDatabaseConnection();
            connection.setAutoCommit(false);

            RequestDto requestDto = RequestDto.builder()
                    .requesterId("4646038518")
                    .build();

            FetchAllMyPendingRequestDao fetchAllMyPendingRequestDao = new FetchAllMyPendingRequestDao(connection);

            FetchRequestDto fetchRequestDto = fetchAllMyPendingRequestDao.fetchPendingRequest(requestDto);

            JSONObject resultObject = new JSONObject();
            resultObject.put("records",  new Gson().toJson(fetchRequestDto));

            System.out.println("requestDtoList = " + resultObject);

            connection.commit();
            connection.close();

        }catch (Exception exception){
            connection.rollback();
        }
    }*/
}
