package ndc.approvalmatrix.service.javaservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApprovalRequestDto {

    private Long requestId;
    private String userId;
    private String contractId;
    private String accountNo;
    private Integer workFlowId;
    private List<ApprovalRow> approvalRowList;
    private List<WorkFlowFeatureAction> workFlowFeatureActions;
    private Long matrixId;
    private Integer isEdit;
}
