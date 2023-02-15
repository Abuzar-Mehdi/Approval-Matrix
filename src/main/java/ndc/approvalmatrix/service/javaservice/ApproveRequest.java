package ndc.approvalmatrix.service.javaservice;

import com.google.gson.Gson;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import ndc.approvalmatrix.service.javaservice.commons.DatabaseConnection;
import ndc.approvalmatrix.service.javaservice.dao.ApproveRequestDao;
import ndc.approvalmatrix.service.javaservice.dao.CreateNewRequestDao;
import ndc.approvalmatrix.service.javaservice.dto.RequestDto;

import java.sql.Connection;
import java.sql.SQLException;

public class ApproveRequest  implements JavaService2{


	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {

		Result result = new Result();
		Connection connection = null;

		try {

			connection = new DatabaseConnection().getDatabaseConnection();
			connection.setAutoCommit(false);

			RequestDto requestDto = RequestDto.builder()
					.approverId(request.getParameter("approverId"))
					.contractId(request.getParameter("contractId"))
					.referenceNo(request.getParameter("referenceNo"))
					.accountNo("accountNo")
					.remarks(request.getParameter("remarks"))
					.build();


			ApproveRequestDao approveRequestDao = new ApproveRequestDao(connection);

			requestDto = approveRequestDao.approveRequest(requestDto);

			result.addParam("reqStatus",requestDto.getStatus() );
			result.addParam("reqResponse",requestDto.getResponse() );

			connection.commit();
			connection.close();

		}catch (Exception e){

			try {
				connection.rollback();
			} catch (SQLException ex) {
				throw new RuntimeException(ex);
			}
		}

		return result;
	}

	/*public static void main(String[] args) throws SQLException {

		 String json ="{\n" +
				 "  \"approverId\": \"8188523666\",\n" +
				 "  \"contractId\": \"8436131351\",\n" +
				 "  \"referenceNo\": \"e8e2c8d6-97ea-11ed-a8fc-0242ac120003\",\n" +
				 "  \"remarks\": \"Transaction Approved\"\n" +
				 "}";

		RequestDto requestDto = //new Gson().fromJson(json,RequestDto.class);
				RequestDto.builder()
				.contractId("8436131351")
				.approverId("4111935994")
						.accountNo("1234545667")
				.referenceNo("e8e2c8d6-97ea-11ed-a8fc-0242ac120004")
				.remarks("Transaction Approved")
				.build();

		Connection connection = new DatabaseConnection().getDatabaseConnection();
		connection.setAutoCommit(false);

		ApproveRequestDao approveRequestDao = new ApproveRequestDao(connection);

		requestDto =approveRequestDao.approveRequest(requestDto);

		System.out.println("requestDto = " + requestDto.getResponse());

		connection.commit();
		connection.close();
	}*/

}
