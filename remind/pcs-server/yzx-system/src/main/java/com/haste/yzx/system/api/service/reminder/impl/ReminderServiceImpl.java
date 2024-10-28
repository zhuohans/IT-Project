package com.haste.yzx.system.api.service.reminder.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haste.yzx.common.Constants;
import com.haste.yzx.common.enums.RemindModelConstant;
import com.haste.yzx.common.enums.ResponseEnum;
import com.haste.yzx.common.exception.BadRequestException;
import com.haste.yzx.system.api.dao.reminder.ReminderDao;
import com.haste.yzx.system.api.dao.reminder.ReminderDoingDao;
import com.haste.yzx.system.api.domain.po.reminder.ReminderDoingPo;
import com.haste.yzx.system.api.domain.po.reminder.ReminderPo;
import com.haste.yzx.system.api.service.reminder.IReminderService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReminderServiceImpl extends ServiceImpl<ReminderDao, ReminderPo> implements IReminderService {

    @Resource
    ReminderDoingDao reminderDoingDao;

    @Override
    public Boolean create(ReminderPo reminderPo) {
        return this.save(reminderPo);
    }

    @Override
    public List<ReminderPo> getReminderList(String uid) {

        LambdaQueryWrapper<ReminderPo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ReminderPo::getCreateBy, uid);
        return this.list(wrapper);
    }

    @Override
    public List<ReminderDoingPo> getReminderListDoing(String uid) {
        if (null == uid) {
            throw new BadRequestException(ResponseEnum.UNAUTHORIZED.code, ResponseEnum.UNAUTHORIZED.msg);
        }
        LambdaQueryWrapper<ReminderDoingPo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ReminderDoingPo::getCreateBy, uid);
        wrapper.eq(ReminderDoingPo::getStatus, Constants.NO);
        wrapper.orderByDesc(ReminderDoingPo::getCreateTime);
        return reminderDoingDao.selectList(wrapper);
    }

    @Override
    public Boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public void createRemindEvent() {
        List<ReminderPo> list = this.list();
        DateTime now = DateTime.now().setField(DateField.SECOND, 0);
        List<ReminderPo> toEventList = new ArrayList<>();
        for (ReminderPo remind : list) {
            if (remind.getModel() == null) {
                continue;
            }
            String dbTimeStr = null;
            String nowTimeStr = null;
            DateTime dbRemindTime = DateTime.of(remind.getTime());
            switch (remind.getModel()) {
                case RemindModelConstant.ONCE:
                    if (dbRemindTime.toString().equals(now.toString())) {
                        toEventList.add(remind);
                    }
                    break;
                case RemindModelConstant.EVERY_DAY:
                    dbTimeStr = dbRemindTime.toTimeStr();
                    nowTimeStr = now.toTimeStr();
                    if (dbTimeStr.equals(nowTimeStr)) {
                        toEventList.add(remind);
                    }
                    break;
                case RemindModelConstant.EVERY_DAY_IN_WEEK:
                    int dbDayOfWeek = dbRemindTime.getField(DateField.DAY_OF_WEEK);
                    int nowDayOfWeek = now.getField(DateField.DAY_OF_WEEK);
                    if (dbDayOfWeek != nowDayOfWeek) {
                        continue;
                    }
                    dbTimeStr = dbRemindTime.toTimeStr();
                    nowTimeStr = now.toTimeStr();
                    if (dbTimeStr.equals(nowTimeStr)) {
                        toEventList.add(remind);
                    }
                    break;
                case RemindModelConstant.EVERY_DAY_IN_MONTH:
                    int dbDayOfMonth = dbRemindTime.getField(DateField.DAY_OF_MONTH);
                    int nowDayOfMonth = now.getField(DateField.DAY_OF_MONTH);
                    if (dbDayOfMonth != nowDayOfMonth) {
                        continue;
                    }
                    dbTimeStr = dbRemindTime.toTimeStr();
                    nowTimeStr = now.toTimeStr();
                    if (dbTimeStr.equals(nowTimeStr)) {
                        toEventList.add(remind);
                    }
                    break;
            }

        }
        if (toEventList.size() == 0) {
            return;
        }
        for (ReminderPo reminderPo : toEventList) {
            ReminderDoingPo event = new ReminderDoingPo(reminderPo);
            reminderDoingDao.insert(event);
            System.out.println(JSONUtil.toJsonStr(event));;
        }
    }
}
