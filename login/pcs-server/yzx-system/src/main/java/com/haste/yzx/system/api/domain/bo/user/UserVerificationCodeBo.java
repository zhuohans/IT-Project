package com.haste.yzx.system.api.domain.bo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Schema(description = "用户验证码登录业务类")
@Data
public class UserVerificationCodeBo {

    @Schema(description = "手机号",required = true)
    @NotBlank(message = "手机号不为空")
    private String phoneNo;

    @Schema(description = "验证码",required = true)
    @NotBlank(message = "验证码不为空")
    private String code;
}
