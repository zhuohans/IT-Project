package com.haste.yzx.system.api.domain.po.reminder;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.haste.yzx.common.domain.po.BasePo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("t_pcs_reminder_doing")
@NoArgsConstructor
public class ReminderDoingPo extends BasePo {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long remindId;

    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Australia/Melbourne")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    private Date time;

    private Integer status;


    public ReminderDoingPo(ReminderPo remind){
        this.remindId = remind.getId();
        remind.setId(null);
        BeanUtil.copyProperties(remind,this);
        DateTime now = DateTime.now();
        DateTime ntime = DateTime.of(this.time);
        now.setField(DateField.HOUR,ntime.getField(DateField.HOUR));
        now.setField(DateField.MINUTE,ntime.getField(DateField.MINUTE));
        now.setField(DateField.SECOND,0);
        this.time = now;
        this.commonSet(this.getCreateBy());

    }

}
