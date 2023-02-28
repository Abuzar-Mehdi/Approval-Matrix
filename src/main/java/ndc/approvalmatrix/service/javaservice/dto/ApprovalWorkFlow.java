package ndc.approvalmatrix.service.javaservice.dto;

import lombok.*;

import java.math.BigInteger;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApprovalWorkFlow {

    private Long matrixId;
    private String contractId;
    private String accountNo;
    private Integer workflowId;
    private List<ApprovalRow> approvalRowList;
    private List<WorkFlowFeatureAction> workFlowFeatureActions;
}
