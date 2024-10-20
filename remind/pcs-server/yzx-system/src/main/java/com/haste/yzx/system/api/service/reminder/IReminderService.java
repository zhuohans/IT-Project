package com.haste.yzx.system.api.service.reminder;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haste.yzx.system.api.domain.po.reminder.ReminderDoingPo;
import com.haste.yzx.system.api.domain.po.reminder.ReminderPo;

import java.util.List;

public interface IReminderService extends IService<ReminderPo> {
    Boolean create(ReminderPo reminderPo);

    List<ReminderPo> getReminderList(String uid);

    List<ReminderDoingPo> getReminderListDoing(String uid);

    Boolean delete(Long id);

    void createRemindEvent();
}
