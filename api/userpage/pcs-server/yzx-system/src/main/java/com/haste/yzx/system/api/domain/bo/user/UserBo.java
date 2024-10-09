package com.haste.yzx.system.api.domain.bo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "用户业务类")
public class UserBo {
    @Schema(description = "用户名",required = true)
    @NotBlank(message = "用户名不为空")
    private String username;

    @Schema(description = "密码",required = true)
    @NotBlank(message = "密码不为空")
    private String password;

    @Schema(description = "性别",required = true)
    private String gender;

    @Schema(description = "邮箱",required = true)
    private String email;

    @Schema(description = "验证码",required = true)
    private String verCode;
}
