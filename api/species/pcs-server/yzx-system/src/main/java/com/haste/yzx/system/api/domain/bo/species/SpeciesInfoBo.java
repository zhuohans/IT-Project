package com.haste.yzx.system.api.domain.bo.species;

import cn.hutool.core.bean.BeanUtil;
import com.haste.yzx.system.api.domain.po.species.SpeciesInfo;
import com.haste.yzx.system.api.domain.po.user.CommentPo;
import com.haste.yzx.system.api.domain.po.user.UserPo;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class SpeciesInfoBo extends SpeciesInfo {

    private long likeCnt;

    private long commentCnt;

    private long viewCnt;

    private long collectCnt;

    private List<CommentPo> commentList;

    private int collectStatus;

    private int likeStatus;

    private UserPo userInfo;

    public SpeciesInfoBo(SpeciesInfo info){
        BeanUtil.copyProperties(info,this);
    }
}
