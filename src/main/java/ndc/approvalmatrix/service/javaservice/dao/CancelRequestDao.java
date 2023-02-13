package ndc.approvalmatrix.service.javaservice.dao;

import com.konylabs.middleware.controller.DataControllerResponse;
import io.goodforgod.http.common.HttpStatus;
import ndc.approvalmatrix.service.javaservice.commons.ApprovalConstants;
import ndc.approvalmatrix.service.javaservice.dto.RequestDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class CancelRequestDao {

    private Connection connection;

    public CancelRequestDao() {

    }

    public CancelRequestDao(Connection connection) {
        super();
        this.connection = connection;
    }

    public RequestDto cancelRequest(RequestDto requestDto) {


        try {


            String sqlWorkFlow="SELECT * FROM ndc_request WHERE  CONTRACTID=? AND REFERENCENO=? AND  SOFTDELETE=0 ";

            PreparedStatement statement = connection.prepareStatement(sqlWorkFlow);

            statement.setString(1, requestDto.getContractId());
            statement.setString(2, requestDto.getReferenceNo());

            ResultSet resultSet =  statement.executeQuery();

            if(resultSet.next()){

                /// update request to reject  ///

                // CHECKING SAME USER AS REQUESTER //

                if(resultSet.getString("REQUESTERID").equalsIgnoreCase(requestDto.getRequesterId())) {

                    if (resultSet.getString("STATUS").equalsIgnoreCase(ApprovalConstants.IN_PROGRESS)) {

                        String sqlCancel = "UPDATE ndc_request SET  STATUS=? , REMARKS=? , MODIFYDATE=? , MODIFYBY=?  WHERE  REQUESTERID=? AND CONTRACTID=? AND REFERENCENO=?";
                        PreparedStatement statement3 = connection.prepareStatement(sqlCancel);
                        statement3.setString(1, ApprovalConstants.CANCEL);
                        statement3.setString(2, requestDto.getRemarks());
                        statement3.setString(3, LocalDateTime.now().toString());
                        statement3.setString(4, requestDto.getRequesterId());

                        statement3.setString(5, requestDto.getRequesterId());
                        statement3.setString(6, requestDto.getContractId());
                        statement3.setString(7, requestDto.getReferenceNo());

                        statement3.executeUpdate();

                        requestDto.setStatus(ApprovalConstants.CANCEL);
                        requestDto.setResponse(ApprovalConstants.REQUEST_CANCELED);

                    } else if (resultSet.getString("STATUS").equalsIgnoreCase(ApprovalConstants.APPROVED)) {

                        requestDto.setResponse(ApprovalConstants.REQUEST_ALREADY_APPROVED);
                    } else if (resultSet.getString("STATUS").equalsIgnoreCase(ApprovalConstants.REJECTED)) {

                        requestDto.setResponse(ApprovalConstants.REQUEST_ALREADY_REJECTED);
                    } else if (resultSet.getString("STATUS").equalsIgnoreCase(ApprovalConstants.CANCEL)) {

                        requestDto.setResponse(ApprovalConstants.REQUEST_ALREADY_CANCELED);
                    }
                }
                else{

                    requestDto.setResponse(ApprovalConstants.ONLY_SAME_USER_CAN_CANCEL);

                }

            }else{

                requestDto.setResponse(ApprovalConstants.NO_REQUEST_FOUND);

            }

        } catch (SQLException e) {

            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            e.printStackTrace();
        }

        return requestDto;
    }
}
