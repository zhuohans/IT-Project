package com.haste.yzx.system.api.dao.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.haste.yzx.system.api.domain.po.user.GardenPo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GardenDao extends BaseMapper<GardenPo> {
}
