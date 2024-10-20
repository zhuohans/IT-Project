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
