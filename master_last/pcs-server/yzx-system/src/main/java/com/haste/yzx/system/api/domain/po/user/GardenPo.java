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
@Tag(name = "花园")
public class GardenPo extends BasePo {
    @TableId
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    
    @Schema(description = "物种id")
    private Long speciesId;

    @Schema(description = "经度")
    private String lng;

    @Schema(description = "纬度")
    private String lat;

    @Schema(description = "排序字段")
    private Integer sort;

    @Schema(description = "物种图片")
    private String imgPath;

    @Schema(description = "拉丁名")
    private String latinName;

    @Schema(description = "中文名")
    private String cnName;

}
