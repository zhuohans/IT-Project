package com.haste.yzx.system.api.controller.user;

import com.haste.yzx.common.exception.BadRequestException;
import com.haste.yzx.common.response.Response;
import com.haste.yzx.common.utils.QrUtil;
import com.haste.yzx.system.api.domain.bo.user.ResetPasswordBo;
import com.haste.yzx.system.api.domain.bo.user.UserBo;
import com.haste.yzx.system.api.domain.bo.user.UserVerificationCodeBo;
import com.haste.yzx.system.api.domain.po.user.UserPo;
import com.haste.yzx.system.api.service.user.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@RestController
@RequestMapping("/auth/user")
@Tag(name = "用户认证服务")
public class UserController {

    @Resource
    private IUserService userService;

    @Operation(summary = "登录")
    @PostMapping("/login")
    public Response login(@Validated @RequestBody UserBo userBo){
        return Response.success(userService.login(userBo));
    }

    @Operation(summary = "验证码登录")
    @PostMapping("/login/verification/code")
    public Response loginByVerificationCode(@Validated @RequestBody UserVerificationCodeBo userVerificationCodeBo){
        return Response.success(userService.verificationCodeLogin(userVerificationCodeBo));
    }

    @Operation(summary = "注册")
    @PostMapping("/register")
    public Response register(@Validated @RequestBody UserBo userBo){
        userService.register(userBo);
        return Response.success();
    }

    @Operation(summary = "生成二维码")
    @GetMapping("/genQr")
    public Response genQr(@RequestParam String content){
        String res = null;
        try {
            BufferedImage bfImg = new QrUtil().buildQrCodeImage(content);
            // 将 BufferedImage 转换成 byte 数组
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bfImg, "png", baos);
            byte[] imageBytes = baos.toByteArray();

            // 使用 Base64 编码
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            // 返回 Base64 字符串
            res = "data:image/png;base64," + base64Image;
        } catch (IOException e) {
            throw new BadRequestException(400,e.getMessage());
        }
        return Response.success(res);
    }

    @Operation(summary = "发送邮件")
    @GetMapping("/send/mail")
    public Response sendMail(@RequestParam String email){
        userService.sendMail(email);

        return Response.success();
    }

    @Operation(summary = "发送注册验证码")
    @GetMapping("/send/register/mail")
    public Response sendRegisterMail(@RequestParam String email){
        userService.sendRegisterMail(email);

        return Response.success();
    }

    @Operation(summary = "重置密码")
    @PostMapping("/reset/password")
    public Response resetPassword(@RequestBody @Validated ResetPasswordBo resetPasswordBo){
        userService.resetPassword(resetPasswordBo);
        return Response.success();
    }

    @Operation(summary = "获取用户详情")
    @GetMapping("/info")
    public Response getUserInfo(){
        UserPo userPo = userService.getUserInfo();
        return Response.success(userPo);
    }

    @Operation(summary = "修改用户信息")
    @PostMapping("/update")
    public Response updateUserInfo(@RequestBody UserPo userPo){
        userService.updateUserInfo(userPo);
        return Response.success();
    }

    @Operation(summary = "修改用户密码")
    @PostMapping("/change/password")
    public Response changePassword(@RequestBody UserBo userBo){
        userService.changePassword(userBo);
        return Response.success();
    }
}
