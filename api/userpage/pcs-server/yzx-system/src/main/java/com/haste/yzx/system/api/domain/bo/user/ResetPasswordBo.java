package com.haste.yzx.system.api.domain.bo.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResetPasswordBo {
    @NotBlank(message = "邮箱不为空")
    private String email;
    @NotNull(message = "验证码不为空")
    private Integer code;
    @NotNull(message = "新密码不为空")
    private String newPassword;
}
