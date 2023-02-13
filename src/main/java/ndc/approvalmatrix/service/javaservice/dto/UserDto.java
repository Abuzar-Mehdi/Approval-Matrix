package ndc.approvalmatrix.service.javaservice.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class UserDto {

    private Long approverId;
    private String assignDate;
}
