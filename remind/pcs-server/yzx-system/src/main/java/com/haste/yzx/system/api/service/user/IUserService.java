package com.haste.yzx.system.api.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haste.yzx.system.api.domain.po.user.UserPo;

public interface IUserService extends IService<UserPo> {


    UserPo getUserInfo();



}
