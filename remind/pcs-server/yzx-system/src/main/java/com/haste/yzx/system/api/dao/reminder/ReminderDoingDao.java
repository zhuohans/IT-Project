package com.haste.yzx.system.api.dao.reminder;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.haste.yzx.system.api.domain.po.reminder.ReminderDoingPo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReminderDoingDao extends BaseMapper<ReminderDoingPo> {
}
