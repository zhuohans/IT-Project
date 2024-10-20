package com.haste.yzx.system.api.controller.user;

import com.haste.yzx.system.api.domain.po.user.UserPo;
import com.haste.yzx.system.api.service.user.IUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class BaseUserController {

    @Resource
    private IUserService userService;


    public UserPo getUserInfo() {
        UserPo userInfo = userService.getUserInfo();
        if (userInfo == null) {
            userInfo = new UserPo();
        }
        return userInfo;
    }

}
