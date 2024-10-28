package com.haste.yzx.system.api.domain.po.user;

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
@TableName("t_pcs_garden")
@Tag(name = "Garden")
public class GardenPo extends BasePo {
    
    @TableId
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @Schema(description = "Species ID")
    private Long speciesId;

    @Schema(description = "Longitude")
    private String lng;

    @Schema(description = "Latitude")
    private String lat;

    @Schema(description = "Sort Field")
    private Integer sort;

    @Schema(description = "Species Image")
    private String imgPath;

    @Schema(description = "Latin Name")
    private String latinName;

    @Schema(description = "Chinese Name")
    private String cnName;
}