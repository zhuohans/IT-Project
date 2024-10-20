package com.haste.yzx.system.api.dao.species;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.haste.yzx.system.api.domain.po.species.SpeciesInfo;
import com.haste.yzx.system.api.domain.po.user.LikePo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SpeciesLikeDao extends BaseMapper<LikePo> {
}
