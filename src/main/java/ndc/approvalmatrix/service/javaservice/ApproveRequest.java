package ndc.approvalmatrix.service.javaservice;

import com.google.gson.Gson;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
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
					.approverId(request.getParameter("approverId"))
					.contractId(request.getParameter("contractId"))
					.referenceNo(request.getParameter("referenceNo"))
					.accountNo(request.getParameter("accountNo"))
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
				connection.close();
			} catch (SQLException ex) {
				throw new RuntimeException(ex);
			}
		}

		return result;
	}

//	public static void main(String[] args) throws SQLException {
//
//
//
//		RequestDto requestDto =	RequestDto.builder()
//				.contractId("8436131351")
//				.approverId("4111935994")
//						.accountNo("1234545667")
//				.referenceNo("e8e2c8d6-97ea-11ed-a8fc-0242ac120011")
//				.remarks("Transaction Approved")
//				.build();
//
//		Connection connection = new DatabaseConnection().getDatabaseConnection();
//		connection.setAutoCommit(false);
//
//		ApproveRequestDao approveRequestDao = new ApproveRequestDao(connection);
//
//		requestDto =approveRequestDao.approveRequest(requestDto);
//
//		System.out.println("requestDto = " + requestDto.getResponse());
//
//		connection.commit();
//		connection.close();
//	}

}
