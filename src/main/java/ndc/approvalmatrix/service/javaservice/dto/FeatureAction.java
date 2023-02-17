package ndc.approvalmatrix.service.javaservice.dto;

import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeatureAction {

    private String featureActionId;
    private String name;
    private Integer isEnabled;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
}
