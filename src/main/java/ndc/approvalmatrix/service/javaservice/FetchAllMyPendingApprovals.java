package ndc.approvalmatrix.service.javaservice;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import ndc.approvalmatrix.service.javaservice.commons.DatabaseConnection;
import ndc.approvalmatrix.service.javaservice.dao.FetchAllMyPendingApprovalsDao;
import ndc.approvalmatrix.service.javaservice.dto.FetchRequestDto;
import ndc.approvalmatrix.service.javaservice.dto.RequestDto;
import org.json.JSONObject;

import java.sql.Connection;

public class FetchAllMyPendingApprovals implements JavaService2 {

    @Override
    public Object invoke(String s, Object[] objects, DataControllerRequest dataControllerRequest, DataControllerResponse dataControllerResponse) throws Exception {

        Result result = new Result();
        Connection connection = null;

        try {

            connection = new DatabaseConnection().getDatabaseConnection();
            connection.setAutoCommit(false);


            RequestDto requestDto = RequestDto.builder()
                    .approverId(dataControllerRequest.getParameter("approverId"))
                    .build();

           FetchAllMyPendingApprovalsDao fetchAllMyPendingApprovals = new FetchAllMyPendingApprovalsDao(connection);

            FetchRequestDto fetchRequestDto = fetchAllMyPendingApprovals.fetchPendingApprovals(requestDto);


            JSONObject resultObject = new JSONObject();
            resultObject.put("records",  new JSONObject(fetchRequestDto));
            result = JSONToResult.convert(resultObject.toString());

            connection.commit();
            connection.close();

        }catch (Exception exception){
            connection.rollback();
        }

        return result;
    }

   /* public static void main(String[] args) throws SQLException {

        Connection connection = null;

        try {

            connection = new DatabaseConnection().getDatabaseConnection();
            connection.setAutoCommit(false);


            RequestDto requestDto = RequestDto.builder()
                    .approverId("8049024214")
                    .build();

            FetchAllMyPendingApprovalsDao fetchAllMyPendingApprovals = new FetchAllMyPendingApprovalsDao(connection);

            FetchRequestDto fetchRequestDto = fetchAllMyPendingApprovals.fetchPendingApprovals(requestDto);

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
