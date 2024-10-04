package com.haste.yzx.system.api.domain.bo.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResetPasswordBo {
    @NotBlank(message = "Mailbox is not null")
    private String email;
    @NotNull(message = "Verification code is not null")
    private Integer code;
    @NotNull(message = "New password is not null")
    private String newPassword;
}
