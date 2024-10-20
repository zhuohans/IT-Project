package com.haste.yzx.system.api.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haste.yzx.system.api.domain.bo.user.ResetPasswordBo;
import com.haste.yzx.system.api.domain.bo.user.UserBo;
import com.haste.yzx.system.api.domain.bo.user.UserVerificationCodeBo;
import com.haste.yzx.system.api.domain.po.user.UserPo;

import java.util.Map;

public interface IUserService extends IService<UserPo> {
    Map<String, Object> login(UserBo user);

    Map<String, Object> verificationCodeLogin(UserVerificationCodeBo userVerificationCodeBo);

    void register(UserBo userBo);

    void sendMail(String email);

    void sendRegisterMail(String email);

    void resetPassword(ResetPasswordBo resetPasswordBo);

    UserPo getUserInfo();

    void updateUserInfo(UserPo userPo);

    void changePassword(UserBo userBo);

}
