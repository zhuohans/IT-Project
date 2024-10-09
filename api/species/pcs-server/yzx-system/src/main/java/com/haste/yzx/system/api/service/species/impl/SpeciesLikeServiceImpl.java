package com.haste.yzx.system.api.service.species.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haste.yzx.system.api.dao.species.SpeciesLikeDao;
import com.haste.yzx.system.api.domain.po.user.LikePo;
import com.haste.yzx.system.api.service.species.ISpeciesLikeService;
import org.springframework.stereotype.Service;

@Service
public class SpeciesLikeServiceImpl extends ServiceImpl<SpeciesLikeDao, LikePo> implements ISpeciesLikeService {

}
