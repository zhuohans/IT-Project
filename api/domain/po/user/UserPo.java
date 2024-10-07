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
@Tag(name = "user entity")
public class UserPo extends BasePo {
    @TableId
    private String userId;

    @Schema(description = "username")
    private String username;

    @Schema(description = "nickname")
    private String nickName;

    @Schema(description = "password")
    private String password;

    @Schema(description = "appID")
    private String appId;

    @Schema(description = "user type(0:normal user)")
    private Integer userType;

    @Schema(description = "expired time")
    private Date expiredTime;

    @Schema(description = "password reset time")
    private Date pwdResetTime;

    @Schema(description = "gender")
    private String gender;

    @Schema(description = "birthday")
    private Date birthday;

    @Schema(description = "age")
    private Integer age;

    @Schema(description = "location")
    private String location;

    @Schema(description = "phone number")
    private String phone;

    @Schema(description = "email")
    private String email;

    @Schema(description = "user avatar")
    private String avatar;

    @Schema(description = "user defined configuration")
    private String userConfig;
}
