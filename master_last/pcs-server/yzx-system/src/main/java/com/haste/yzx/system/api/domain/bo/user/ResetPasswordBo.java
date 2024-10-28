package com.haste.yzx.system.api.domain.bo.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResetPasswordBo {
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @NotNull(message = "Verification code cannot be null")
    private Integer code;

    @NotNull(message = "New password cannot be null")
    private String newPassword;
}