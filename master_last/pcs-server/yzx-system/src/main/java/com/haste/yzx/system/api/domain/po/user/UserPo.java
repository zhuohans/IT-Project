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
@Tag(name = "用户实体")
public class UserPo extends BasePo {
    @TableId
    private String userId;

    @Schema(description = "账号")
    private String username;

    @Schema(description = "用户名")
    private String nickName;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "所属应用ID")
    private String appId;

    @Schema(description = "用户类型(0:普通用户)")
    private Integer userType;

    @Schema(description = "过期时间")
    private Date expiredTime;

    @Schema(description = "密码重置时间")
    private Date pwdResetTime;

    @Schema(description = "性别")
    private String gender;

    @Schema(description = "出生日期")
    private Date birthday;

    @Schema(description = "年龄")
    private Integer age;

    @Schema(description = "所在地")
    private String location;

    @Schema(description = "最高学历")
    private String mostEducation;

    @Schema(description = "毕业学校")
    private String graduatedSchool;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "用户图片")
    private String avatar;

    @Schema(description = "用户自定义配置")
    private String userConfig;
}
