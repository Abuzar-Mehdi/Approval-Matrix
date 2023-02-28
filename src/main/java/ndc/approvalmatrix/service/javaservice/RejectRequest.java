package ndc.approvalmatrix.service.javaservice;

import com.google.gson.Gson;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
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
					.approverId(request.getParameter("rejectorId"))
					.contractId(request.getParameter("contractId"))
					.referenceNo(request.getParameter("referenceNo"))
					.remarks(request.getParameter("remarks"))
					.accountNo(request.getParameter("accountNo"))
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
