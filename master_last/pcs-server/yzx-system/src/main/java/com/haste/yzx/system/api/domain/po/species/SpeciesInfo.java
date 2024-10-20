package com.haste.yzx.system.api.domain.po.species;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.haste.yzx.common.domain.po.BasePo;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("t_pcs_species_info")
@Tag(name = "物种分类实体")
public class SpeciesInfo extends BasePo {
    @TableId
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @Schema(description = "物种分类")
    private String speciesClassification;

    @Schema(description = "物种名称")
    private String name;

    @Schema(description = "物种图片路径")
    private String imgPath;

    @Schema(description = "物种描述")
    private String descp;

    @Schema(description = "生长区域")
    private String growthArea;

    @Schema(description = "最佳温度")
    private String optimalTemperature;

    @Schema(description = "最佳土壤")
    private String optimalSoil;

    @Schema(description = "生长时间")
    private String growthTime;

    @Schema(description = "收获时间")
    private String harvestTime;

    @Schema(description = "最佳湿度")
    private String optimalHumidity;

    @Schema(description = "经度")
    private String lng;

    @Schema(description = "纬度")
    private String lat;

    @Schema(description = "购买种子的最佳公司")
    private String bestCompany;

    @Schema(description = "是否可见")
    private Integer visible = 0;

    @Schema(description = "状态")
    @TableField(value = "`status`", fill = FieldFill.INSERT_UPDATE)
    private Integer status;

    @Schema(description = "排序")
    @TableField(fill = FieldFill.INSERT)
    private Integer sort;

    private String latinName;

    private Integer popularClassification;

    private Integer seasonClassification;

}
