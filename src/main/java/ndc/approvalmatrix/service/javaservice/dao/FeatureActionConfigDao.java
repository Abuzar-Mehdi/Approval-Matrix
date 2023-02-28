package ndc.approvalmatrix.service.javaservice.dao;

import ndc.approvalmatrix.service.javaservice.dto.FeatureAction;
import ndc.approvalmatrix.service.javaservice.dto.FeatureActionConfigDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
        PreparedStatement preparedStatement = null;
        int batchSize = 50;
        int count = 0;

        try {

            if(featureActionConfigDto.getIsInsert() == 1) {

                String sql = "INSERT INTO ndc_featureactionconfig" +
                        "(featureactionid, name, contractid, createby, createdate, modifyby, modifydate,isenabled,accountno)" +
                        "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

                preparedStatement = connection.prepareStatement(sql);

                for (FeatureAction featureAction : featureActionConfigDto.getFeatureActions()) {

                    preparedStatement.setString(1, featureAction.getFeatureActionId());
                    preparedStatement.setString(2, featureAction.getName());
                    preparedStatement.setString(3, featureActionConfigDto.getContractId());
                    preparedStatement.setString(4, featureActionConfigDto.getCreateBy());
                    preparedStatement.setString(5, LocalDate.now().toString());
                    preparedStatement.setString(6, featureActionConfigDto.getModifyBy());
                    preparedStatement.setString(7, LocalDate.now().toString());
                    preparedStatement.setInt(8, featureAction.getIsEnabled());

                    preparedStatement.setString(9, featureActionConfigDto.getAccountNo());

                    preparedStatement.addBatch();

                    if(count % batchSize == 0){

                        preparedStatement.executeBatch();
                        message = "RECORD INSERTED SUCCESSFULLY";
                    }

                    count++;
                }

            }else {

                String sql = "UPDATE ndc_featureactionconfig" +
                        " SET featureactionid=?, name=?,  modifyby=?, modifydate=?, isenabled=?   " +
                        "WHERE contractid=? and  accountno=? and featureactionid=?  ";

                preparedStatement = connection.prepareStatement(sql);

                for (FeatureAction featureAction : featureActionConfigDto.getFeatureActions()) {

                    preparedStatement.setString(1, featureAction.getFeatureActionId());
                    preparedStatement.setString(2, featureAction.getName());
                    preparedStatement.setString(3, featureActionConfigDto.getModifyBy());
                    preparedStatement.setString(4, LocalDate.now().toString());
                    preparedStatement.setInt(5, featureAction.getIsEnabled());



                    preparedStatement.setString(6, featureActionConfigDto.getContractId());
                    preparedStatement.setString(7, featureActionConfigDto.getAccountNo());
                    preparedStatement.setString(8, featureAction.getFeatureActionId());

                    preparedStatement.addBatch();

                    if(count % batchSize == 0){

                        preparedStatement.executeBatch();
                        message = "RECORD UPDATED SUCCESSFULLY";
                    }

                    count++;
                }
            }

            preparedStatement.executeBatch();

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
