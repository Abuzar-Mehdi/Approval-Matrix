package ndc.approvalmatrix.service.javaservice.dao;

import ndc.approvalmatrix.service.javaservice.commons.ApprovalConstants;
import ndc.approvalmatrix.service.javaservice.dto.ApprovalRequestDto;
import ndc.approvalmatrix.service.javaservice.dto.ApprovalRow;

import java.sql.*;

public class CreateApprovalMatrixDao {

    private Connection connection;


    public CreateApprovalMatrixDao() {

    }

    public CreateApprovalMatrixDao(Connection connection) {
        super();
        this.connection = connection;
    }

    public String createApprovalMatrix(ApprovalRequestDto approvalRequestDto){

        String result;
        try {

            String sqlMaster ="INSERT INTO ndc_approvalmatrixmaster (CONTRACTID, USERID, ISSEQUENTIAL)" +
                    "VALUES(?, ?, ?)";

            PreparedStatement statement = connection.prepareStatement(sqlMaster, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1,approvalRequestDto.getContractId()); //ContractID
            statement.setString(2,approvalRequestDto.getUserId()); //userid
            statement.setInt(3,approvalRequestDto.getIsSequential()); //Sequential 1, Non-sequential 0

            statement.executeUpdate();

            ResultSet keys = statement.getGeneratedKeys();
            keys.next();

            /// getting request id from table //

            approvalRequestDto.setRequestId(keys.getLong(1));

            // getting all members of Role

            for(ApprovalRow approvalRow : approvalRequestDto.getApprovalRowList()) {

                String sqlRoleMember = "SELECT A.CUSTOMER_ID AS APPROVERID FROM customergroup A " +
                        "INNER JOIN membergroup B  ON A.GROUP_ID = B.ID " +
                        "WHERE A.CONTRACTID =? AND A.CORECUSTOMERID =? AND B.NAME =? ";

                PreparedStatement statement1 = connection.prepareStatement(sqlRoleMember);
                statement1.setString(1, approvalRequestDto.getContractId());
                statement1.setString(2, approvalRequestDto.getCoreCustomerId());
                statement1.setString(3, approvalRow.getRole());

                ResultSet resultSet = statement1.executeQuery();
                int i = 0;
                String rule = null;

                while (resultSet.next()) {

                    i++;

                    String sqlDetail = "INSERT INTO ndc_approvalmatrixdetail (APPROVALMASTERID, APPROVERID, SEQUENCENO, GROUPNO,APPROVALORDER, ROLE) " +
                            "VALUES(?, ?, ?, ?, ?, ?)";

                    PreparedStatement statement2 = connection.prepareStatement(sqlDetail);
                    statement2.setLong(1, approvalRequestDto.getRequestId());
                    statement2.setString(2, resultSet.getString("approverid"));
                    statement2.setInt(3, approvalRow.getSequenceNo());
                    statement2.setInt(4, approvalRow.getGroupNo() == null ? 1 :approvalRow.getGroupNo() );
                    statement2.setInt(5, approvalRow.getSequenceNo());
                    statement2.setString(6, approvalRow.getRole());

                    statement2.executeUpdate();

                }

                //Insertion into Sequence Rule

                String sqlRule = "INSERT INTO ndc_sequencerule" +
                        "(APPROVALMASTERID, SEQUENCENO, GROUPNO ,RULE, RULEVALUE)" +
                        "VALUES(?, ?, ?, ?, ?)";

                PreparedStatement statement3 = connection.prepareStatement(sqlRule);
                statement3.setLong(1, approvalRequestDto.getRequestId());
                statement3.setInt(2,  approvalRow.getSequenceNo());
                statement3.setInt(3, approvalRow.getGroupNo() == null ? 1 :approvalRow.getGroupNo() );


                if (ApprovalConstants.ANY_ONE_VALUE == approvalRow.getApprovalRule()) {

                    if (i > 1) {
                        i = 1;
                    }
                    rule=ApprovalConstants.ANY_ONE;

                } else if (ApprovalConstants.ANY_TWO_VALUE == approvalRow.getApprovalRule() ) {

                    if (i > 2) {
                        i = 2;
                    }
                    rule=ApprovalConstants.ANY_TWO;

                } else if (ApprovalConstants.ANY_THREE_VALUE == approvalRow.getApprovalRule()) {

                    if (i > 3) {
                        i = 3;
                    }
                    rule=ApprovalConstants.ANY_THREE;

                }   else if (ApprovalConstants.ANY_FOUR_VALUE == approvalRow.getApprovalRule()) {

                if (i > 4) {
                    i = 4;
                }
                rule=ApprovalConstants.ANY_FOUR;

            }
                else if (ApprovalConstants.ALL_VALUE == approvalRow.getApprovalRule()) {

                    rule=ApprovalConstants.ALL;
                }

                statement3.setString(4, rule); // ALL ,ANY_ONE,ANY_TWO,ANY_THREE,ANY_FOUR
                statement3.setInt(5, i);

                statement3.executeUpdate();
            }

            result="Approval Matrix Successfully created :"+approvalRequestDto.getRequestId();

        }catch (Exception exception){

            try {
                result ="Error in creating Approval Matrix :"+exception.getMessage();
                connection.rollback();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            exception.printStackTrace();
        }

        return result;
    }
}
