package com.haste.yzx.system.api.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haste.yzx.system.api.dao.user.UserDao;
import com.haste.yzx.system.api.domain.po.user.UserPo;
import com.haste.yzx.system.api.service.user.IUserService;
import org.springframework.stereotype.Service;

/**
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, UserPo> implements IUserService {


    @Override
    public UserPo getUserInfo() {
        try {
            Object loginId = 1832981039760220161L;
            LambdaQueryWrapper<UserPo> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(UserPo::getUserId,loginId);
            return this.getOne(wrapper);
        }catch (Exception e){

        }
        return null;
    }

}
