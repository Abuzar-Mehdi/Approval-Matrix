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
    private Double minAmount;
    private Double maxAmount;
}
