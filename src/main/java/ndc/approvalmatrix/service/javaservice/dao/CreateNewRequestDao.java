package ndc.approvalmatrix.service.javaservice.dao;

import java.sql.*;
import java.time.LocalDateTime;

import com.konylabs.middleware.dataobject.Result;
import com.mysql.cj.xdevapi.PreparableStatement;

import ndc.approvalmatrix.service.javaservice.commons.ApprovalConstants;
import ndc.approvalmatrix.service.javaservice.dto.RequestDto;

public class CreateNewRequestDao {
	
	private Connection connection;
	
	public CreateNewRequestDao() {
		
	}

	public CreateNewRequestDao(Connection connection) {
		super();
		this.connection = connection;
	}
	
	public RequestDto  createNewRequest(RequestDto requestDto) {

		try {


			CallableStatement callableStatement = connection.prepareCall("{CALL " + ApprovalConstants.GET_FEATUREACTION_APPROVER_LIMIT + "(?,?,?,?,?)}");
			callableStatement.setString(1, requestDto.getContractId());
			callableStatement.setString(2, requestDto.getAccountNo());
			callableStatement.setString(3, requestDto.getFeatureActionId());
			callableStatement.setDouble(4, requestDto.getAmount());
			callableStatement.registerOutParameter(5, java.sql.Types.INTEGER);
			callableStatement.execute();

			//ResultSet resultSet = callableStatement.getResultSet();

			if(callableStatement.getInt(5) == 1) {

				CallableStatement callableStatement1 = connection.prepareCall("{CALL " + ApprovalConstants.GET_CONTRACT_WORKFLOW + "(?,?)}");
				callableStatement1.setString(1, requestDto.getContractId());
				callableStatement1.setString(2, requestDto.getAccountNo());
				callableStatement1.execute();


				ResultSet resultSet = callableStatement1.getResultSet();

				int i = 0;

				while (resultSet.next()) {

					if (i == 0) {

						String sqlString = "INSERT INTO ndc_request" +
								"(approvalmasterid, requesterid,  status,  modifyby, remarks, referenceno, featureactionid,contractId,issequential,accountno)" +
								"VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

						PreparedStatement statementRequest = connection.prepareStatement(sqlString, Statement.RETURN_GENERATED_KEYS);

						statementRequest.setLong(1, resultSet.getLong("ID"));
						statementRequest.setLong(2, resultSet.getLong("USERID"));
						statementRequest.setString(3, ApprovalConstants.IN_PROGRESS);
						statementRequest.setLong(4, resultSet.getLong("USERID"));
						statementRequest.setString(5, requestDto.getRemarks());
						statementRequest.setString(6, requestDto.getReferenceNo());
						statementRequest.setString(7, requestDto.getFeatureActionId());
						statementRequest.setString(8, requestDto.getContractId());
						statementRequest.setInt(9, resultSet.getInt("ISSEQUENTIAL"));
						statementRequest.setString(10, requestDto.getAccountNo());

						statementRequest.executeUpdate();

						ResultSet keys = statementRequest.getGeneratedKeys();
						keys.next();

						/// getting request id from table //

						requestDto.setRequestId(keys.getLong(1));

					}


					String sqlWorkflow = "INSERT INTO ndc_requestworkflow" +
							"(requestid, approverid, sequenceno, approverorder, approvedate, status, remarks, rulevalue, seqstatus,groupno,groupstatus)" +
							"VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

					PreparedStatement statement1 = connection.prepareStatement(sqlWorkflow);

					if (resultSet.getInt("ISSEQUENTIAL") == 1) {

						statement1.setLong(1, requestDto.getRequestId());
						statement1.setString(2, resultSet.getString("APPROVERID"));
						statement1.setString(3, resultSet.getString("SEQUENCENO"));
						statement1.setString(4, resultSet.getString("APPROVALORDER"));
						statement1.setString(5, LocalDateTime.now().toString());

						if (resultSet.getInt("GROUPNO") == 1) {
							statement1.setString(6, ApprovalConstants.PENDING);
							statement1.setString(9, ApprovalConstants.PENDING);
							statement1.setString(11, ApprovalConstants.PENDING);

						} else {
							statement1.setString(6, ApprovalConstants.NOT_ASSIGNED);
							statement1.setString(9, (resultSet.getInt("SEQUENCENO") == 1 ? ApprovalConstants.PENDING : ApprovalConstants.NOT_ASSIGNED));
							statement1.setString(11, ApprovalConstants.NOT_ASSIGNED);
						}

						statement1.setString(7, requestDto.getRemarks());
						statement1.setString(8, resultSet.getString("RULEVALUE"));
						statement1.setString(10, resultSet.getString("GROUPNO"));

					} else {

						statement1.setLong(1, requestDto.getRequestId());
						statement1.setString(2, resultSet.getString("APPROVERID"));
						statement1.setString(3, resultSet.getString("SEQUENCENO"));
						statement1.setString(4, resultSet.getString("APPROVALORDER"));
						statement1.setString(5, LocalDateTime.now().toString());
						statement1.setString(6, ApprovalConstants.PENDING);
						statement1.setString(7, requestDto.getRemarks());
						statement1.setString(8, resultSet.getString("RULEVALUE"));
						statement1.setString(9, ApprovalConstants.PENDING);
						statement1.setString(10, resultSet.getString("GROUPNO"));
						statement1.setString(11, ApprovalConstants.PENDING);

					}
					statement1.executeUpdate();
					i++;
				}
				requestDto.setResponse(ApprovalConstants.REQUEST_CREATED_SUCCESSFULLY);

			}else {

				requestDto.setResponse(ApprovalConstants.REQUEST_CANNOT_CREATE);
			}


			
		} catch (SQLException e) {

			try {
				connection.rollback();
				requestDto.setResponse(e.getStackTrace().toString());
			} catch (SQLException ex) {
				throw new RuntimeException(ex);
			}
			e.printStackTrace();
		}

		return  requestDto;
	}

}
