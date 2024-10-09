package com.haste.yzx.system.api.domain.po.user;

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
@TableName("t_pcs_species_comment")
@Tag(name = "物种评论实体")
public class CommentPo extends BasePo {
    @TableId
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @Schema(description = "评论")
    private String comment;

    @Schema(description = "物种id")
    private Long speciesId;

    @Schema(description = "祖id")
    private Long ancestors;

    @Schema()
    private Integer vieww;

    @TableField(exist = false)
    private UserPo userInfo;
}
