package com.haste.yzx.system.api.service.species;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.haste.yzx.system.api.domain.bo.species.SpeciesInfoBo;
import com.haste.yzx.system.api.domain.bo.species.SpeciesQueryInfo;
import com.haste.yzx.system.api.domain.po.species.SpeciesInfo;

public interface ISpeciesService extends IService<SpeciesInfo> {
    IPage<SpeciesInfo> getSpeciesNewsList(SpeciesQueryInfo queryInfo);
    void publishSpecies(Long id, Integer visible, String uid);

    void likeSpecies(Long id, Integer act, String uid);

    void collectSpecies(Long id, Integer act, String uid);

    void commentSpecies(Long id, String comment, String uid);

    void delCommentSpecies(Long id,Long commentId, String uid);

    SpeciesInfoBo getSpeciesInfoBo(Long id, String uid);

    void getCommentList(SpeciesInfoBo infoBo);


}
