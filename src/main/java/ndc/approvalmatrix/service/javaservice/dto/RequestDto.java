package ndc.approvalmatrix.service.javaservice.dto;

import lombok.*;

import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RequestDto {

	private String requesterId;
	private String contractId;
	private String referenceNo;
	private Long requestId;
	private String approverId;
	private String remarks;
	private String status;
	private String featureActionId;
	private Integer ruleValue;
	private Integer sequenceNo;
	private Integer groupNo;
    private String response;
	private String assignDate;
	private String accountNo;
}
