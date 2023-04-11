package ndc.approvalmatrix.service.javaservice;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;

import com.google.gson.Gson;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;

import com.konylabs.middleware.dataobject.Result;
import io.goodforgod.http.common.HttpStatus;
import ndc.approvalmatrix.service.javaservice.commons.DatabaseConnection;
import ndc.approvalmatrix.service.javaservice.dao.CreateNewRequestDao;
import ndc.approvalmatrix.service.javaservice.dto.RequestDto;
import org.json.HTTP;

public class CreateNewRequest  implements JavaService2 {

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response)  {

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
					.featureActionId(request.getParameter("featureActionId"))
					.accountNo(request.getParameter("accountNo"))
					.amount(Double.valueOf(request.getParameter("amount")))
					.coreCustomerId(request.getParameter("coreCustomerId"))
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
				connection.rollback();
			} catch (SQLException ex) {
				throw new RuntimeException(ex);
			}
		}
		return result;
	}


	public static void main(String[] args) throws SQLException {

//		String json = "{\n" +
//				"  \"requesterId\": \"1234567\",\n" +
//				"  \"contractId\": \"8436131351\",\n" +
//				"  \"referenceNo\": \"e8e2c8d6-97ea-11ed-a8fc-0242ac120113\",\n" +
//				"  \"featureActionId\": \"BILL_PAY_CREATE\",\n" +
//				"  \"accountNo\": \"1234545667\",\n" +
//				"  \"remarks\": \"Transaction created\",\n" +
//				"  \"amount\": \"5000\"\n" +
//				"}";



		RequestDto requestDto = RequestDto.builder()
				.requesterId("6284824056")
				.contractId("8436131351")
				.accountNo("1234545667")
				.referenceNo("e8e2c8d6-97ea-11ed-a8fc-0242ac120113")
				.remarks("request created by 6284824056")
				.featureActionId("BILL_PAY_CREATE")
				.amount(5000d)
				.coreCustomerId("102190")
				.build();

		//RequestDto requestDto = new Gson().fromJson(json,RequestDto.class);
		Connection connection = new DatabaseConnection().getDatabaseConnection();
		connection.setAutoCommit(false);


		CreateNewRequestDao createNewRequestDao = new CreateNewRequestDao(connection);

		createNewRequestDao.createNewRequest(requestDto);
		System.out.println("requestDto = " + requestDto.getResponse());

		connection.commit();
		connection.close();
	}
}
