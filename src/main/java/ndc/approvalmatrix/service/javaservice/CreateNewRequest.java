package ndc.approvalmatrix.service.javaservice;

import java.sql.Connection;
import java.sql.SQLException;

import com.google.gson.Gson;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;

import com.konylabs.middleware.dataobject.Result;
import ndc.approvalmatrix.service.javaservice.commons.DatabaseConnection;
import ndc.approvalmatrix.service.javaservice.dao.CreateNewRequestDao;
import ndc.approvalmatrix.service.javaservice.dto.RequestDto;

public class CreateNewRequest  implements JavaService2 {

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response)  {

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
					.featureActionId(request.getParameter("featureActionId"))
					.build();

			CreateNewRequestDao createNewRequestDao = new CreateNewRequestDao(connection);

			requestDto = createNewRequestDao.createNewRequest(requestDto);

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


	public static void main(String[] args) throws SQLException {

		RequestDto requestDto = RequestDto.builder()
				.requesterId("6284824056")
				.contractId("8436131351")
				.referenceNo("e8e2c8d6-97ea-11ed-a8fc-0242ac120003")
				.remarks("request created by 6284824056")
				.featureActionId("BILL_PAY")
				.build();

		Connection connection = new DatabaseConnection().getDatabaseConnection();
		connection.setAutoCommit(false);

		//RequestDto requestDto = new Gson().fromJson(request.getParameter("data"), RequestDto.class);

		CreateNewRequestDao createNewRequestDao = new CreateNewRequestDao(connection);

		createNewRequestDao.createNewRequest(requestDto);

		connection.commit();
		connection.close();
	}
}
