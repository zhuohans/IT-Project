package com.haste.yzx.system.api.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haste.yzx.common.Constants;
import com.haste.yzx.system.api.dao.species.SpeciesLikeDao;
import com.haste.yzx.system.api.dao.user.CommentDao;
import com.haste.yzx.system.api.domain.bo.species.SpeciesInfoBo;
import com.haste.yzx.system.api.domain.po.user.CommentPo;
import com.haste.yzx.system.api.domain.po.user.LikePo;
import com.haste.yzx.system.api.service.user.ICommentService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentDao,CommentPo> implements ICommentService {


    @Resource
    SpeciesLikeDao speciesLikeDao;

    @Override
    public void getStaticsNum(SpeciesInfoBo infoBo) {
        Long speciesId = infoBo.getId();

        LambdaQueryWrapper<CommentPo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommentPo::getSpeciesId,speciesId).isNotNull(CommentPo::getComment);
        infoBo.setCommentCnt(getBaseMapper().selectCount(wrapper));

        wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommentPo::getSpeciesId,speciesId).eq(CommentPo::getVieww, Constants.YES);
        infoBo.setViewCnt(getBaseMapper().selectCount(wrapper));

        LambdaQueryWrapper<LikePo> wrapperLike = new LambdaQueryWrapper<>();
        wrapperLike.eq(LikePo::getSpeciesId,speciesId).eq(LikePo::getLikee, Constants.YES);
        infoBo.setLikeCnt(speciesLikeDao.selectCount(wrapperLike));

        wrapperLike = new LambdaQueryWrapper<>();
        wrapperLike.eq(LikePo::getSpeciesId,speciesId).eq(LikePo::getCollect, Constants.YES);
        infoBo.setCollectCnt(speciesLikeDao.selectCount(wrapperLike));
    }




}
