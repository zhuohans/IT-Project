package com.haste.yzx.system.api.domain.po.reminder;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.haste.yzx.common.domain.po.BasePo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("t_pcs_reminder")
public class ReminderPo extends BasePo {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:00", timezone = "Asia/Shanghai")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:00", iso = DateTimeFormat.ISO.DATE_TIME)
    private Date time;

    private Integer status = 0;

    @Schema(description = "模式（1：仅一次，2：每天，3：每周，4：每月）")
    private Integer model = 1;
}
