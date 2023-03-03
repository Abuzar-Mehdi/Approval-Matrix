package ndc.approvalmatrix.service.javaservice.dao;

import ndc.approvalmatrix.service.javaservice.commons.ApprovalConstants;
import ndc.approvalmatrix.service.javaservice.commons.StoredProcedure;
import ndc.approvalmatrix.service.javaservice.dto.FetchRequestDto;
import ndc.approvalmatrix.service.javaservice.dto.RequestDto;
import ndc.approvalmatrix.service.javaservice.dto.UserDto;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FetchAllMyPendingApprovalsDao {

    private Connection connection;

    public FetchAllMyPendingApprovalsDao() {

    }

    public FetchAllMyPendingApprovalsDao(Connection connection) {
        super();
        this.connection = connection;
    }

    public FetchRequestDto fetchPendingApprovals(RequestDto requestDto){


        FetchRequestDto fetchRequestDto = null;
        
        try {
            List<UserDto> userDtoList =  new ArrayList<>();
            CallableStatement callableStatement;

            callableStatement = connection.prepareCall("{CALL "+ StoredProcedure.FETCH_APPROVAL +"(?)}");
            callableStatement.setString(1,requestDto.getApproverId());
            callableStatement.execute();

            ResultSet resultSet =callableStatement.getResultSet();

            while (resultSet.next()){

                fetchRequestDto = FetchRequestDto.builder()
                        .contractId(resultSet.getString("contractid"))
                        .featureActionId(resultSet.getString("featureActionId"))
                        .referenceNo(resultSet.getString("referenceNo"))
                        .build();

                UserDto userDto = UserDto.builder()
                        .approverId(resultSet.getLong("approverId"))
                        .assignDate(resultSet.getString("assignDate"))
                        .build();

                userDtoList.add(userDto);
            }

            fetchRequestDto.setApprover(userDtoList);

        }catch (Exception exception){

            try {
                exception.printStackTrace();
                connection.rollback();
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return fetchRequestDto;
    }
}
