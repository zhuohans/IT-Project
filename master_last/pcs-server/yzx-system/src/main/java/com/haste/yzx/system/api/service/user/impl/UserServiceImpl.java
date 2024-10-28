package com.haste.yzx.system.api.service.user.impl;

import cn.dev33.satoken.stp.SaLoginConfig;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haste.yzx.common.domain.bo.ToEmail;
import com.haste.yzx.common.enums.ResponseEnum;
import com.haste.yzx.common.exception.BadRequestException;
import com.haste.yzx.common.utils.CommonUtil;
import com.haste.yzx.common.utils.MailSender;
import com.haste.yzx.common.utils.PasswordUtil;
import com.haste.yzx.common.utils.RedisUtil;
import com.haste.yzx.system.api.dao.user.UserDao;
import com.haste.yzx.system.api.domain.bo.user.ResetPasswordBo;
import com.haste.yzx.system.api.domain.bo.user.UserBo;
import com.haste.yzx.system.api.domain.bo.user.UserVerificationCodeBo;
import com.haste.yzx.system.api.domain.po.user.UserPo;
import com.haste.yzx.system.api.service.user.IUserService;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, UserPo> implements IUserService {

    @Resource
    PasswordEncoder passwordEncoder;

    @Value("${privateKey}")
    private String privateKey;

    @Resource(name = "customMailSender")
    private MailSender mailSender;

    @Resource
    private RedisUtil redisUtil;

    private Integer VERIFICATION_CODE_EX = 900;

    private String REGISTER_KEY_PRE = "register:";


    @Override
    public void register(UserBo userBo) {
        long expire = redisUtil.getExpire(REGISTER_KEY_PRE + userBo.getEmail());
        if (expire<=0){
            throw new BadRequestException(ResponseEnum.VERIFICATION_CODE_EXCEPTION.code, ResponseEnum.VERIFICATION_CODE_EXCEPTION.msg);
        }

        String code = redisUtil.get(REGISTER_KEY_PRE + userBo.getEmail()).toString();
        if (!StrUtil.equals(code,userBo.getVerCode())){
            throw new BadRequestException(ResponseEnum.VERIFICATION_CODE_EXCEPTION.code, ResponseEnum.VERIFICATION_CODE_EXCEPTION.msg);
        }

        String username = userBo.getUsername();
        LambdaQueryWrapper<UserPo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserPo::getUsername,username);
        List<UserPo> userList = this.list(wrapper);
        if (CollUtil.isNotEmpty(userList)){
            throw new BadRequestException(ResponseEnum.USER_EXISTS.code, ResponseEnum.USER_EXISTS.msg);
        }

        String password = userBo.getPassword();
        String decode = PasswordUtil.decode(password, privateKey);

        String encoded = passwordEncoder.encode(decode);
        UserPo userPo = new UserPo();
        userPo.setUsername(username);
        userPo.setPassword(encoded);
        userPo.setGender(userBo.getGender());
        userPo.setEmail(userBo.getEmail());
        this.save(userPo);
        redisUtil.del(REGISTER_KEY_PRE + userBo.getEmail());
    }

    @Override
    public Map<String, Object> login(UserBo user) {
        String username = user.getUsername();
        String password = user.getPassword();

        LambdaQueryWrapper<UserPo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserPo::getUsername,username);
        UserPo dbUser = this.getOne(wrapper);

        if (null == dbUser){
            throw new BadRequestException(ResponseEnum.USER_NOT_REGISTER_EXCEPTION.code, ResponseEnum.USER_NOT_REGISTER_EXCEPTION.msg);
        }
        String decode = PasswordUtil.decode(password, privateKey);
        if (passwordEncoder.matches(decode,dbUser.getPassword())){
            StpUtil.login(dbUser.getUserId(), SaLoginConfig
                    .setExtra("userId",dbUser.getUserId()));
            Map<String, Object> resMap = new HashMap<>();
            resMap.put("token",StpUtil.getTokenValue());
            return resMap;
        }
        else {
            throw new BadRequestException(ResponseEnum.USERNAME_PASSWORD_EXCEPTION.code,
                    ResponseEnum.USERNAME_PASSWORD_EXCEPTION.msg);
        }
    }

    @Override
    public Map<String, Object> verificationCodeLogin(UserVerificationCodeBo userVerificationCodeBo) {

        String phoneNo = userVerificationCodeBo.getPhoneNo();
        Boolean isPhoneNumber = CommonUtil.isValidPhoneNumber(phoneNo);
        String code = userVerificationCodeBo.getCode();

        if (isPhoneNumber && StrUtil.equals("6666",code)){
            StpUtil.login(phoneNo, SaLoginConfig
                    .setExtra("userId",phoneNo));
            Map<String, Object> resMap = new HashMap<>();
            resMap.put("token",StpUtil.getTokenValue());
            return resMap;
        }
        else {
            throw new BadRequestException(ResponseEnum.VERIFICATION_CODE_EXCEPTION.code,
                    ResponseEnum.VERIFICATION_CODE_EXCEPTION.msg);
        }
    }

    @Override
    public void sendMail(String email) {
        LambdaQueryWrapper<UserPo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserPo::getEmail,email);
        List<UserPo> list = this.list(wrapper);
        if (CollUtil.isEmpty(list)){
            throw new BadRequestException(ResponseEnum.USER_NOT_REGISTER_EXCEPTION.code, ResponseEnum.USER_NOT_REGISTER_EXCEPTION.msg);
        }
        ToEmail toEmail = new ToEmail();
        toEmail.setTos(new String[]{email});
        toEmail.setSubject("Reset Password");
        Integer code = RandomUtil.randomInt(100000,999999);
        toEmail.setContent("<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><title>PCS password reset</title></head><body><div style=\"width: 600px\"><h1 style=\"background-color: #b9f3de;height: 104px;display: flex;flex-direction: column;justify-content: end;padding-bottom: 16px;;padding-left: 16px\">PCS Verification code</h1><p>Dear PCS User,</p><p>We have received a request to access your PCS account "+email+" using your email address. Your PCS verification code is:</p><p style=\"font-weight: 800;font-size: 18px;text-align: center\">"+code+"</p><p>If you did not request this verification code, someone may be trying to access the following PCS account:"+email+". <span style=\"font-weight: 800\">Please do not forward or provide this verification code to anyone.</span></p><p>You are receiving this email because this email address has been set as a secondary email for the PCS account email.</p><p>Sincerely,</p><p>The PCS Account Team</p></div></body></html>");
        try {
            mailSender.htmlEmail(toEmail);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        redisUtil.setExKeyValue(email,code,VERIFICATION_CODE_EX);
    }

    @Override
    public void sendRegisterMail(String email) {
        System.out.println("done");
        LambdaQueryWrapper<UserPo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserPo::getEmail,email);
        List<UserPo> list = this.list(wrapper);
        if (CollUtil.isNotEmpty(list)){
            throw new BadRequestException(ResponseEnum.USER_EXISTS.code, ResponseEnum.USER_EXISTS.msg);
        }

        ToEmail toEmail = new ToEmail();
        toEmail.setTos(new String[]{email});
        toEmail.setSubject("Register verification code");
        Integer code = RandomUtil.randomInt(100000,999999);
        toEmail.setContent("<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><title>PCS register verification code</title></head><body><div style=\"width: 600px;\"><p style=\"font-weight: 800;font-size: 32px;background-color:#b9f3de;height: 104px;display: flex;align-items: center\">PCS Verification code</p><p>Dear PCS User,</p><p>We have received a request to access your PCS account "+email+" using your email address. Your PCS verification code is:</p><p style=\"font-weight: 800;text-align: center;font-size: 18px\">"+code+"</p><p>If you did not request this verification code, someone may be trying to access the following PCS account:"+email+". Please do not forward or provide this verification code to anyone.</p><p>You are receiving this email because this email address has been set as a secondary email for the PCS account email.</p><p>Sincerely,</p><p>The PCS Account Team</p></div></body></html>");
        try {
            mailSender.htmlEmail(toEmail);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        redisUtil.setExKeyValue(REGISTER_KEY_PRE+email,code,VERIFICATION_CODE_EX);
    }

    @Override
    public void resetPassword(ResetPasswordBo resetPasswordBo) {
        String email = resetPasswordBo.getEmail();
        long expire = redisUtil.getExpire(email);
        String decode = PasswordUtil.decode(resetPasswordBo.getNewPassword(),privateKey);
        String encode = passwordEncoder.encode(decode);
        if (expire < 0){
            throw new BadRequestException(ResponseEnum.VERIFICATION_CODE_EXPIRED.code,ResponseEnum.VERIFICATION_CODE_EXPIRED.msg);
        }
        Integer code = Integer.valueOf(redisUtil.get(email).toString());
        if (!code.equals(resetPasswordBo.getCode())){
            throw new BadRequestException(ResponseEnum.VERIFICATION_CODE_EXCEPTION.code,ResponseEnum.VERIFICATION_CODE_EXCEPTION.msg);
        }
        UpdateWrapper<UserPo> wrapper = new UpdateWrapper<>();
        wrapper.eq("email",resetPasswordBo.getEmail());
        wrapper.set("password",encode);
        this.update(wrapper);
    }

    @Override
    public UserPo getUserInfo() {
        try {
            Object loginId = StpUtil.getLoginId();
            LambdaQueryWrapper<UserPo> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(UserPo::getUserId,loginId);
            return this.getOne(wrapper);
        }catch (Exception e){

        }
        return null;
    }

    @Override
    public void updateUserInfo(UserPo userPo) {

        LambdaQueryWrapper<UserPo> wrapper = new LambdaQueryWrapper<>();


        wrapper.eq(UserPo::getEmail, userPo.getEmail());
        long emailCount = this.count(wrapper);
        if (emailCount > 0 && (userPo.getUserId() == null || !this.getById(userPo.getUserId()).getEmail().equals(userPo.getEmail()))) {
            throw new BadRequestException(ResponseEnum.USER_EXISTS.code, ResponseEnum.USER_EXISTS.msg);
        }


        wrapper.clear();


        wrapper.eq(UserPo::getUsername, userPo.getUsername());
        long usernameCount = this.count(wrapper);
        if (usernameCount > 0 && (userPo.getUserId() == null || !this.getById(userPo.getUserId()).getUsername().equals(userPo.getUsername()))) {
            throw new BadRequestException(ResponseEnum.USER_EXISTS.code, ResponseEnum.USER_EXISTS.msg);
        }


        this.updateById(userPo);
    }

    @Override
    public void changePassword(UserBo userBo) {
        Object loginId = StpUtil.getLoginId();
        UpdateWrapper<UserPo> wrapper = new UpdateWrapper<>();
        wrapper.eq("user_id",loginId);

        String decode = PasswordUtil.decode(userBo.getPassword(),privateKey);
        String encoded = passwordEncoder.encode(decode);
        wrapper.set("password",encoded);
        this.update(wrapper);
    }
}
