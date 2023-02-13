package ndc.approvalmatrix.service.javaservice;

import com.google.gson.Gson;
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

            connection = new DatabaseConnection().getDatabaseConnection();
            connection.setAutoCommit(false);


            RequestDto requestDto = RequestDto.builder()
                    .requesterId(request.getParameter("requesterId"))
                    .contractId(request.getParameter("contractId"))
                    .referenceNo(request.getParameter("referenceNo"))
                    .remarks(request.getParameter("remarks"))
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
