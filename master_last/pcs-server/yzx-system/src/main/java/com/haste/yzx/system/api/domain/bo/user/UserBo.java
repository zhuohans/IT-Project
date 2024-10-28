package com.haste.yzx.system.api.domain.bo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "User Business Object")
public class UserBo {

    @Schema(description = "Username", required = true)
    @NotBlank(message = "Username cannot be blank")
    private String username;

    @Schema(description = "Password", required = true)
    @NotBlank(message = "Password cannot be blank")
    private String password;

    @Schema(description = "Gender", required = true)
    private String gender;

    @Schema(description = "Email", required = true)
    private String email;

    @Schema(description = "Verification Code", required = true)
    private String verCode;
}