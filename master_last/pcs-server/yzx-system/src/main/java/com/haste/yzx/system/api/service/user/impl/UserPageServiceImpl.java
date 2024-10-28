package com.haste.yzx.system.api.service.user.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.haste.yzx.common.enums.UserActEnum;
import com.haste.yzx.system.api.dao.species.SpeciesDao;
import com.haste.yzx.system.api.domain.bo.species.SpeciesInfoBo;
import com.haste.yzx.system.api.domain.po.species.SpeciesInfo;
import com.haste.yzx.system.api.service.user.IUserPageService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;


@Service
public class UserPageServiceImpl implements IUserPageService {


    @Resource
    SpeciesDao speciesDao;

    @Override
    public IPage getFootprintsList(Page page,String uid,Integer type) {
        if(type == null){
            type = UserActEnum.ALL.ordinal();
        }
        if(type == UserActEnum.COMMENT.ordinal()){
            return speciesDao.getCommentList(page,uid);
        }
        if(type == UserActEnum.VIEW.ordinal()){
            return speciesDao.getViewList(page,uid);
        }
        if(type == UserActEnum.LIKE.ordinal()){
            return speciesDao.getLikeList(page,uid);
        }
        if(type == UserActEnum.COLLECT.ordinal()){
            return speciesDao.getCollectList(page,uid);
        }

        return speciesDao.getFootprintsList(page, uid);
    }

    public Page<SpeciesInfo> getPhotosList(Page page, String uid) {

        return speciesDao.getPhotosList(page, uid);
    }
    public Page<SpeciesInfo> getCollectPhotosList(Page page, String uid) {

        return speciesDao.getCollectPhotosList(page, uid);
    }

    public SpeciesInfoBo getUserStatisticNum(String uid) {

        return speciesDao.getUserStatisticNum(uid);
    }
}
