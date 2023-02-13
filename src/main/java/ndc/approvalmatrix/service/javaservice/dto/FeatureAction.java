package ndc.approvalmatrix.service.javaservice.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeatureAction {

    private String featureActionId;
    private String name;
    private Integer isEnabled;
}
