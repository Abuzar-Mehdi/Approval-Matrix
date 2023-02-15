package ndc.approvalmatrix.service.javaservice;

import com.google.gson.Gson;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import ndc.approvalmatrix.service.javaservice.commons.DatabaseConnection;
import ndc.approvalmatrix.service.javaservice.dao.CreateNewRequestDao;
import ndc.approvalmatrix.service.javaservice.dao.RejectRequestDao;
import ndc.approvalmatrix.service.javaservice.dto.RequestDto;

import java.sql.Connection;
import java.sql.SQLException;

public class RejectRequest implements JavaService2 {

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {

		Result result = new Result();
		Connection connection = null;

		try {


			connection = new DatabaseConnection().getDatabaseConnection();
			connection.setAutoCommit(false);

			RequestDto requestDto = RequestDto.builder()
					.approverId(request.getParameter("rejectorId"))
					.contractId(request.getParameter("contractId"))
					.referenceNo(request.getParameter("referenceNo"))
					.remarks(request.getParameter("remarks"))
					.accountNo("accountNo")
					.build();

			RejectRequestDao rejectRequestDao = new RejectRequestDao(connection);

			requestDto = rejectRequestDao.rejectRequest(requestDto);

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

		RequestDto requestDto = RequestDto.builder()
				.contractId("8436131351")
				.approverId("1758657333")
				.referenceNo("e8e2c8d6-97ea-11ed-a8fc-0242ac120002")
				.remarks("i show reject")
				.build();

		Connection connection = new DatabaseConnection().getDatabaseConnection();
		connection.setAutoCommit(false);

		//RequestDto requestDto = new Gson().fromJson(request.getParameter("data"), RequestDto.class);

		RejectRequestDao rejectRequestDao = new RejectRequestDao(connection);

		rejectRequestDao.rejectRequest(requestDto);

		connection.commit();
		connection.close();
	}*/
}
