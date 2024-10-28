package com.haste.yzx.system.api.domain.bo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Schema(description = "User Verification Code Login Business Object")
@Data
public class UserVerificationCodeBo {

    @Schema(description = "Phone Number", required = true)
    @NotBlank(message = "Phone number cannot be blank")
    private String phoneNo;

    @Schema(description = "Verification Code", required = true)
    @NotBlank(message = "Verification code cannot be blank")
    private String code;
}