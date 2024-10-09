package com.haste.yzx.system.api.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haste.yzx.system.api.domain.bo.species.SpeciesInfoBo;
import com.haste.yzx.system.api.domain.po.user.CommentPo;

public interface ICommentService extends IService<CommentPo> {
    void getStaticsNum(SpeciesInfoBo infoBo);

}
