package com.haste.yzx.system.config;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class MpAutoFill implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        Date currentTime = new Date();
        String userId = null;
        try {
            userId = StpUtil.getLoginId().toString();
        } catch (Exception e) {
            log.error("获取当前用户失败：{}",e.getMessage());
        }
        this.strictInsertFill(metaObject, "createTime", Date.class, currentTime);
        this.strictInsertFill(metaObject, "createBy", String.class, userId);
        this.strictInsertFill(metaObject, "updateTime", Date.class, currentTime);
        this.strictInsertFill(metaObject, "updateBy", String.class, userId);
        this.strictInsertFill(metaObject,"sort",Integer.class,0);
        this.strictInsertFill(metaObject,"status",Integer.class,1);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Date currentTime = new Date();
        String userId = null;
        try {
            userId = StpUtil.getLoginId().toString();
        } catch (Exception e) {
            log.error("获取当前用户失败：{}",e.getMessage());
        }
        this.strictUpdateFill(metaObject, "updateTime", Date.class, currentTime);
        this.strictUpdateFill(metaObject, "updateBy", String.class, userId);
    }
}
