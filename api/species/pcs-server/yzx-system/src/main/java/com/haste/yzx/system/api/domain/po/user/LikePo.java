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
@TableName("t_pcs_species_like")
@Tag(name = "点赞收藏实体")
public class LikePo extends BasePo {
    @TableId
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    
    @Schema(description = "物种id")
    private Long speciesId;

    @Schema(description = "喜欢")
    private Integer likee;

    @Schema(description = "收藏")
    private Integer collect;


}
