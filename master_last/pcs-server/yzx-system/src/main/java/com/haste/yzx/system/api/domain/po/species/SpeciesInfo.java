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
@Tag(name = "Species Classification Entity")
public class SpeciesInfo extends BasePo {

    @TableId
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @Schema(description = "Species Classification")
    private String speciesClassification;

    @Schema(description = "Species Name")
    private String name;

    @Schema(description = "Species Image Path")
    private String imgPath;

    @Schema(description = "Species Description")
    private String descp;

    @Schema(description = "Growth Area")
    private String growthArea;

    @Schema(description = "Optimal Temperature")
    private String optimalTemperature;

    @Schema(description = "Is Visible")
    private Integer visible = 0;

    @Schema(description = "Status")
    @TableField(value = "`status`", fill = FieldFill.INSERT_UPDATE)
    private Integer status;

    @Schema(description = "Sort Order")
    @TableField(fill = FieldFill.INSERT)
    private Integer sort;

    private String latinName;

    private Integer popularClassification;

    private Integer seasonClassification;
}