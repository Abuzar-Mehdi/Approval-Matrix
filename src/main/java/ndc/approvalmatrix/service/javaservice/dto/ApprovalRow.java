package ndc.approvalmatrix.service.javaservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApprovalRow {

    private Integer workflowId;
    private Integer sequenceNo;
    private Integer groupNo;
    private Integer approvalRule;
    private String role;
    private Integer isChecker;
}
