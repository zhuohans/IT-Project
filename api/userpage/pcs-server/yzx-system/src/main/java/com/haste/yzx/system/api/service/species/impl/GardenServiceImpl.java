package com.haste.yzx.system.api.service.species.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haste.yzx.system.api.dao.user.GardenDao;
import com.haste.yzx.system.api.domain.po.user.GardenPo;
import com.haste.yzx.system.api.service.species.IGardenService;
import org.springframework.stereotype.Service;

@Service
public class GardenServiceImpl extends ServiceImpl<GardenDao, GardenPo> implements IGardenService {

}
