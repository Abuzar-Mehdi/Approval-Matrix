package ndc.approvalmatrix.service.javaservice.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class FetchRequestDto {

    private String requesterId;
    private String contractId;
    private String referenceNo;
    private String approverId;
    private String featureActionId;
    private Integer sequenceNo;
    List<UserDto> approver;

}
