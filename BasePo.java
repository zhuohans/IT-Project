package com.haste.yzx.common.domain.po;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Base entity class with audit fields for create/update metadata, automatically filled during insert/update operations
 */
@Data
public class BasePo {
    @Schema(description = "Insert_person")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @Schema(description = "Update_person")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;


    @Schema(description = "Insert_time")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    private Date createTime;

    @Schema(description = "Update_time")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    private Date updateTime;


    public void commonSet(String uid) {
        commonSetCreate(uid);
        commonSetUpdate(uid);
    }

    public void commonSetCreate(String uid) {
        this.setCreateBy(uid);
        this.setCreateTime(DateTime.now());
    }

    public void commonSetUpdate(String uid) {
        this.setUpdateBy(uid);
        this.setUpdateTime(DateTime.now());
    }

}
