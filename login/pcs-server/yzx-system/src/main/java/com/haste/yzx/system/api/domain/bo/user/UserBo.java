package com.haste.yzx.system.api.domain.bo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "User service class")
public class UserBo {
    @Schema(description = "User name",required = true)
    @NotBlank(message = "User name is not null")
    private String username;

    @Schema(description = "Password",required = true)
    @NotBlank(message = "Password is not null")
    private String password;

    @Schema(description = "Gender",required = true)
    private String gender;

    @Schema(description = "Email",required = true)
    private String email;

    @Schema(description = "Verification Code",required = true)
    private String verCode;
}
