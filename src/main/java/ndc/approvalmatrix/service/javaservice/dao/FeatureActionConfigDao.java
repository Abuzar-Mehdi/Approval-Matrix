package ndc.approvalmatrix.service.javaservice.dao;

import ndc.approvalmatrix.service.javaservice.commons.Queries;
import ndc.approvalmatrix.service.javaservice.dto.FeatureAction;
import ndc.approvalmatrix.service.javaservice.dto.FeatureActionConfigDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class FeatureActionConfigDao {

    private Connection connection;

    public FeatureActionConfigDao() {

    }

    public FeatureActionConfigDao(Connection connection) {
        super();
        this.connection = connection;
    }

    public String createFeatureActionConfiguration(FeatureActionConfigDto featureActionConfigDto){

        String message= null;
        PreparedStatement preparedStatement1 = null;
        PreparedStatement preparedStatement2 = null;
        int batchSize = 50;
        int count1 = 0;
        int count2 = 0;

        try {



            for (FeatureAction featureAction : featureActionConfigDto.getFeatureActions()) {

                PreparedStatement preparedStatementx = connection.prepareStatement(Queries.FAC_QUERIES.FAC_QUERY3);

                preparedStatementx.setString(1, featureActionConfigDto.getContractId());
                preparedStatementx.setString(2, featureActionConfigDto.getAccountNo());
                preparedStatementx.setString(3, featureAction.getFeatureActionId());

                ResultSet resultSet = preparedStatementx.executeQuery();

                if(resultSet.next()){

                    preparedStatement2 = connection.prepareStatement(Queries.FAC_QUERIES.FAC_QUERY2);
                    preparedStatement2.setString(1, featureAction.getFeatureActionId());
                    preparedStatement2.setString(2, featureAction.getName());
                    preparedStatement2.setString(3, featureActionConfigDto.getModifyBy());
                    preparedStatement2.setString(4, LocalDate.now().toString());
                    preparedStatement2.setInt(5, featureAction.getIsEnabled() ? 1 : 0);
                    preparedStatement2.setString(6, featureActionConfigDto.getContractId());
                    preparedStatement2.setString(7, featureActionConfigDto.getAccountNo());
                    preparedStatement2.setString(8, featureAction.getFeatureActionId());

                    preparedStatement2.execute();

                }else{

                    preparedStatement1 = connection.prepareStatement(Queries.FAC_QUERIES.FAC_QUERY1);
                    preparedStatement1.setString(1, featureAction.getFeatureActionId());
                    preparedStatement1.setString(2, featureAction.getName());
                    preparedStatement1.setString(3, featureActionConfigDto.getContractId());
                    preparedStatement1.setString(4, featureActionConfigDto.getCreateBy());
                    preparedStatement1.setString(5, LocalDate.now().toString());
                    preparedStatement1.setString(6, featureActionConfigDto.getModifyBy());
                    preparedStatement1.setString(7, LocalDate.now().toString());
                    preparedStatement1.setInt(8, featureAction.getIsEnabled() ? 1 : 0);

                    preparedStatement1.setString(9, featureActionConfigDto.getAccountNo());

                    preparedStatement1.execute();

                }


            }



        }catch (Exception e){
            try {
                connection.rollback();
                connection.close();
                message = "ERROR IN RECORD INSERTION / UPDATION  "+e.getMessage();
                e.printStackTrace();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        }
        return message;
    }
}
