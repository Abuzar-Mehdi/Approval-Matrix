package ndc.approvalmatrix.service.javaservice.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeatureActionConfigDto {

    private String contractId;
    private String createBy;
    private String modifyBy;
    private Integer isInsert ;
    private String accountNo;
    private List<FeatureAction> featureActions;
}
