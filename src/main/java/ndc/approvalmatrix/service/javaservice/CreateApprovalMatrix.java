package ndc.approvalmatrix.service.javaservice;

import com.google.gson.Gson;
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


   /* static String json ="{\n" +
            "  \"approvalRowList\": [\n" +
            "    {\n" +
            "      \"sequenceNo\": 1,\n" +
            "      \"approvalRule\": \"2\",\n" +
            "      \"role\": \"Approver\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"sequenceNo\": 2,\n" +
            "      \"approvalRule\": \"3\",\n" +
            "      \"role\": \"Authorizer\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"sequenceNo\": 3,\n" +
            "      \"approvalRule\": \"-1\",\n" +
            "      \"role\": \"Administrator\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";*/

    static String json ="{\n" +
            " \"approvalRowList\": [\n" +
            "    {\n" +
            "      \"sequenceNo\": 1,\n" +
            "       \"groupNo\": 1 ,\n" +
            "      \"approvalRule\": \"2\",     \n" +
            "      \"role\": \"Approver\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"sequenceNo\": 1,\n" +
            "       \"groupNo\": 2, \n" +
            "      \"approvalRule\": \"3\",\n" +
            "      \"role\": \"Authorizer\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"sequenceNo\": 2,\n" +
            "      \"groupNo\" : 1, \n" +
            "      \"approvalRule\": \"-1\",\n" +
            "      \"role\": \"Administrator\"\n" +
            "    }\n" +
            "  ]\n" +
            "  }";

    @Override
    public Object invoke(String s, Object[] objects, DataControllerRequest dataControllerRequest, DataControllerResponse dataControllerResponse) throws SQLException {

        Result result = new Result();
        Connection connection = null;

        try {

             connection = new DatabaseConnection().getDatabaseConnection();
            connection.setAutoCommit(false);

            ApprovalRequestDto approvalRequestDto = new Gson().fromJson(dataControllerRequest.getParameter("approvalRowList"), ApprovalRequestDto.class);

            approvalRequestDto.setUserId(dataControllerRequest.getParameter("userId"));
            approvalRequestDto.setContractId(dataControllerRequest.getParameter("contractId"));
            approvalRequestDto.setIsSequential(Integer.valueOf(dataControllerRequest.getParameter("isSequential")));
            approvalRequestDto.setCoreCustomerId(dataControllerRequest.getParameter("coreCustomerId"));

            CreateApprovalMatrixDao approvalMatrixDao = new CreateApprovalMatrixDao(connection);
            result.addParam("data", approvalMatrixDao.createApprovalMatrix(approvalRequestDto));

            connection.commit();
            connection.close();


        }catch (Exception e){
            connection.rollback();
            e.printStackTrace();
        }


        return result;
    }

    public static void main(String[] args) throws  SQLException {


        Connection connection = new DatabaseConnection().getDatabaseConnection();
        connection.setAutoCommit(false);         ;


        ApprovalRequestDto approvalRequestDto =  new Gson().fromJson(json, ApprovalRequestDto.class);
        approvalRequestDto.setUserId("4646038518");
        approvalRequestDto.setContractId("8436131351");
        approvalRequestDto.setIsSequential(0);
        approvalRequestDto.setCoreCustomerId("102190");

        System.out.println("approvalRowList = " + approvalRequestDto);

        CreateApprovalMatrixDao approvalMatrixDao = new CreateApprovalMatrixDao(connection);
        String Message = approvalMatrixDao.createApprovalMatrix(approvalRequestDto);

        System.out.println("Message = " + Message);

        connection.commit();
        connection.close();
    }

}
