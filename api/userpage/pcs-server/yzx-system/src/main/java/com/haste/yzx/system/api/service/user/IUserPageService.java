package com.haste.yzx.system.api.service.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.haste.yzx.system.api.domain.bo.species.SpeciesInfoBo;
import com.haste.yzx.system.api.domain.po.species.SpeciesInfo;
import com.haste.yzx.system.api.domain.po.user.UserPo;

public interface IUserPageService {

    IPage getFootprintsList(Page page, String uid, Integer type);

    Page<SpeciesInfo> getPhotosList(Page page, String uid);

    Page<SpeciesInfo> getCollectPhotosList(Page page, String uid);

    SpeciesInfoBo getUserStatisticNum(String uid);
}
