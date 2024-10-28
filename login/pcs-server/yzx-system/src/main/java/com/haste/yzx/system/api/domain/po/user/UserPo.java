package com.haste.yzx.system.api.domain.po.user;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.haste.yzx.common.domain.po.BasePo;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("t_pcs_sys_user")
@Tag(name = "User Entity")
public class UserPo extends BasePo {
    @TableId
    private String userId;

    @Schema(description = "Account")
    private String username;

    @Schema(description = "User name")
    private String nickName;

    @Schema(description = "Password")
    private String password;

    @Schema(description = "Associated Application ID")
    private String appId;

    @Schema(description = "User Type (0: Regular User)")
    private Integer userType;

    @Schema(description = "Expiration Time")
    private Date expiredTime;

    @Schema(description = "Password Reset Time")
    private Date pwdResetTime;

    @Schema(description = "Gender")
    private String gender;

    @Schema(description = "Birthday")
    private Date birthday;

    @Schema(description = "Age")
    private Integer age;

    @Schema(description = "Phone")
    private String phone;

    @Schema(description = "Email")
    private String email;

    @Schema(description = "Avatar")
    private String avatar;

    @Schema(description = "User Config")
    private String userConfig;
}
