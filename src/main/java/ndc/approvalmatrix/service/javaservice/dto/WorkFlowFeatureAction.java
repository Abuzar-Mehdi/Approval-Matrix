package ndc.approvalmatrix.service.javaservice.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class WorkFlowFeatureAction {

    private Integer workflowId;
    private Integer isSequential;
    private String featureActionId;
    private Double minAmount=0d;
    private Double maxAmount=0d;
}
