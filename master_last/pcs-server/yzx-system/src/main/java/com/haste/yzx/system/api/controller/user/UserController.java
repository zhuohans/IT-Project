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
@Tag(name = "User Authentication Services")
public class UserController {

    @Resource
    private IUserService userService;

    @Operation(summary = "Login")
    @PostMapping("/login")
    public Response login(@Validated @RequestBody UserBo userBo) {
        return Response.success(userService.login(userBo));
    }

    @Operation(summary = "Login via Verification Code")
    @PostMapping("/login/verification/code")
    public Response loginByVerificationCode(@Validated @RequestBody UserVerificationCodeBo userVerificationCodeBo) {
        return Response.success(userService.verificationCodeLogin(userVerificationCodeBo));
    }

    @Operation(summary = "Register")
    @PostMapping("/register")
    public Response register(@Validated @RequestBody UserBo userBo) {
        userService.register(userBo);
        return Response.success();
    }

    @Operation(summary = "Generate QR Code")
    @GetMapping("/genQr")
    public Response genQr(@RequestParam String content) {
        String res = null;
        try {
            BufferedImage bfImg = new QrUtil().buildQrCodeImage(content);
            // Convert BufferedImage to byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bfImg, "png", baos);
            byte[] imageBytes = baos.toByteArray();

            // Use Base64 encoding
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            // Return Base64 string
            res = "data:image/png;base64," + base64Image;
        } catch (IOException e) {
            throw new BadRequestException(400, e.getMessage());
        }
        return Response.success(res);
    }

    @Operation(summary = "Send Email")
    @GetMapping("/send/mail")
    public Response sendMail(@RequestParam String email) {
        userService.sendMail(email);
        return Response.success();
    }

    @Operation(summary = "Send Registration Verification Code")
    @GetMapping("/send/register/mail")
    public Response sendRegisterMail(@RequestParam String email) {
        userService.sendRegisterMail(email);
        return Response.success();
    }

    @Operation(summary = "Reset Password")
    @PostMapping("/reset/password")
    public Response resetPassword(@RequestBody @Validated ResetPasswordBo resetPasswordBo) {
        userService.resetPassword(resetPasswordBo);
        return Response.success();
    }

    @Operation(summary = "Get User Details")
    @GetMapping("/info")
    public Response getUserInfo() {
        UserPo userPo = userService.getUserInfo();
        return Response.success(userPo);
    }

    @Operation(summary = "Update User Information")
    @PostMapping("/update")
    public Response updateUserInfo(@RequestBody UserPo userPo) {
        userService.updateUserInfo(userPo);
        return Response.success();
    }

    @Operation(summary = "Change User Password")
    @PostMapping("/change/password")
    public Response changePassword(@RequestBody UserBo userBo) {
        userService.changePassword(userBo);
        return Response.success();
    }
}