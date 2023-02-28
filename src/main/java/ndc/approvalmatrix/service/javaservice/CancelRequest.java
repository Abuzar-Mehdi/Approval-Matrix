package ndc.approvalmatrix.service.javaservice;

import com.google.gson.Gson;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import ndc.approvalmatrix.service.javaservice.commons.DatabaseConnection;
import ndc.approvalmatrix.service.javaservice.dao.CancelRequestDao;
import ndc.approvalmatrix.service.javaservice.dao.RejectRequestDao;
import ndc.approvalmatrix.service.javaservice.dto.RequestDto;

import java.sql.Connection;
import java.sql.SQLException;

public class CancelRequest implements JavaService2 {
    @Override
    public Object invoke(String s, Object[] objects, DataControllerRequest request, DataControllerResponse dataControllerResponse)  {

        Result result = new Result();
        Connection connection = null;

        try {

            ServicesManager sm = request.getServicesManager();
            ConfigurableParametersHelper paramHelper = sm.getConfigurableParametersHelper();

            String hostURL = paramHelper.getServerProperty("DBX_HOST_URL").split("//")[1];
            String dbxPort = paramHelper.getServerProperty("DBX_PORT");
            String dbxSchemaName = paramHelper.getServerProperty("DBX_SCHEMA_NAME");
            String dbxDbUsername = paramHelper.getServerProperty("DBX_DB_USERNAME");
            String dbxDbPassword = paramHelper.getServerProperty("DBX_DB_PASSWORD");

            connection = new DatabaseConnection().getDatabaseConnection(hostURL.split(":")[0],dbxPort,dbxSchemaName,dbxDbUsername,dbxDbPassword);

            connection.setAutoCommit(false);


            RequestDto requestDto = RequestDto.builder()
                    .requesterId(request.getParameter("requesterId"))
                    .contractId(request.getParameter("contractId"))
                    .referenceNo(request.getParameter("referenceNo"))
                    .remarks(request.getParameter("remarks"))
                    .accountNo(request.getParameter("accountNo"))
                    .build();

            CancelRequestDao cancelRequestDao = new CancelRequestDao(connection);

            cancelRequestDao.cancelRequest(requestDto);

            result.addParam("reqStatus", requestDto.getStatus());
            result.addParam("reqResponse", requestDto.getResponse());

            connection.commit();
            connection.close();

        }catch (Exception e){

            try {
                connection.rollback();
                connection.close();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            e.printStackTrace();
        }

        return result;
    }

   /* public static void main(String[] args) throws SQLException {

        Connection connection = new DatabaseConnection().getDatabaseConnection();
        connection.setAutoCommit(false);

        RequestDto requestDto = RequestDto.builder()
                .contractId("8436131351")
                .requesterId("4646038518")
                .referenceNo("e8e2c8d6-97ea-11ed-a8fc-0242ac120003")
                .remarks("i show cancel")
                .build();

        //RequestDto requestDto = new Gson().fromJson(request.getParameter("data"), RequestDto.class);

        CancelRequestDao cancelRequestDao = new CancelRequestDao(connection);

        requestDto = cancelRequestDao.cancelRequest(requestDto);

        System.out.println("cancelRequestDao = " + requestDto.getResponse());

        connection.commit();
        connection.close();
    }*/
}
