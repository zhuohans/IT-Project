package com.haste.yzx.system.api.service.species.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haste.yzx.common.Constants;
import com.haste.yzx.system.api.dao.species.SpeciesDao;
import com.haste.yzx.system.api.dao.species.SpeciesLikeDao;
import com.haste.yzx.system.api.domain.bo.species.SpeciesInfoBo;
import com.haste.yzx.system.api.domain.bo.species.SpeciesQueryInfo;
import com.haste.yzx.system.api.domain.po.species.SpeciesInfo;
import com.haste.yzx.system.api.domain.po.user.CommentPo;
import com.haste.yzx.system.api.domain.po.user.LikePo;
import com.haste.yzx.system.api.domain.po.user.UserPo;
import com.haste.yzx.system.api.service.species.ISpeciesService;
import com.haste.yzx.system.api.service.user.ICommentService;
import com.haste.yzx.system.api.service.user.IUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SpeciesServiceImpl extends ServiceImpl<SpeciesDao, SpeciesInfo> implements ISpeciesService {

    @Resource
    ICommentService commentService;
    @Resource
    SpeciesLikeDao speciesLikeDao;

    @Resource
    SpeciesDao speciesDao;
    @Resource
    private IUserService userService;


    @Override
    public IPage<SpeciesInfo> getSpeciesNewsList(SpeciesQueryInfo queryInfo) {

        if (queryInfo == null) {
            queryInfo = new SpeciesQueryInfo();
        }
        if (queryInfo.getPage() == null) {
            queryInfo.setPage(new Page(1, 10));
        }
        LambdaQueryWrapper<SpeciesInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SpeciesInfo::getVisible, Constants.YES);
        if (StrUtil.isNotEmpty(queryInfo.getName())) {
            wrapper.eq(SpeciesInfo::getName, queryInfo.getName());
        }
        if (StrUtil.isNotEmpty(queryInfo.getLatinName())) {
            wrapper.eq(SpeciesInfo::getLatinName, queryInfo.getLatinName());
        }
        if (queryInfo.getPopularClassification() != null) {
            wrapper.eq(SpeciesInfo::getPopularClassification, queryInfo.getPopularClassification());
        }
        if (queryInfo.getSeasonClassification() != null) {
            wrapper.eq(SpeciesInfo::getSeasonClassification, queryInfo.getSeasonClassification());
        }
        String latinNameLike = queryInfo.getLatinNameLike();
        if (StrUtil.isNotEmpty(latinNameLike)) {
            latinNameLike = latinNameLike.replace("  ", " ").trim();
            if (StrUtil.isNotEmpty(latinNameLike)) {
                wrapper.like(SpeciesInfo::getLatinName, latinNameLike);
            }
        }
        wrapper.orderByDesc(SpeciesInfo::getCreateTime);

        return this.page(queryInfo.getPage(), wrapper);
    }

    @Override
    public void publishSpecies(Long id, Integer visible, String uid) {
        UpdateWrapper<SpeciesInfo> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", id).set("visible", visible);
        SpeciesInfo info = new SpeciesInfo();
        info.setId(id);
        info.setVisible(visible);
        info.commonSetUpdate(uid);
        getBaseMapper().updateById(info);
    }

    @Override
    public void likeSpecies(Long id, Integer act, String uid) {
        UpdateWrapper<LikePo> wrapper = new UpdateWrapper<>();
        wrapper.eq("species_id", id).isNotNull("likee").eq("create_by", uid).set("likee", act).set("update_time", DateTime.now());
        int i = speciesLikeDao.update(wrapper);
        if (i == 0) {
            LikePo po = new LikePo();
            po.setSpeciesId(id);
            po.setLikee(act);
            po.commonSet(uid);
            speciesLikeDao.insert(po);
        }
    }

    @Override
    public void collectSpecies(Long id, Integer act, String uid) {
        UpdateWrapper<LikePo> wrapper = new UpdateWrapper<>();
        wrapper.eq("species_id", id).isNotNull("collect").eq("create_by", uid).set("collect", act).set("update_time", DateTime.now());
        int i = speciesLikeDao.update(wrapper);
        if (i == 0) {
            LikePo po = new LikePo();
            po.setSpeciesId(id);
            po.setCollect(act);
            po.commonSet(uid);
            speciesLikeDao.insert(po);
        }
    }

    @Override
    public void commentSpecies(Long id, String comment, String uid) {
        CommentPo po = new CommentPo();
        po.setVieww(Constants.NO);
        po.setComment(comment);
        po.setSpeciesId(id);
        try {
            po.setCreateBy(uid);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        commentService.save(po);
    }

    @Override
    public void delCommentSpecies(Long id, Long commentId, String uid) {

        if (commentId == null || StrUtil.isEmpty(uid)) {
            return;
        }

        CommentPo commentPo = commentService.getBaseMapper().selectById(commentId);
        if (commentPo != null && commentPo.getCreateBy().equals(uid)) {
            // comment self can del
            commentService.getBaseMapper().deleteById(commentId);
        } else {
            SpeciesInfo speciesInfo = this.getBaseMapper().selectById(id);
            if (speciesInfo != null && speciesInfo.getCreateBy().equals(uid)) {
                // species self can del
                commentService.getBaseMapper().deleteById(commentId);
            }
        }
    }


    @Override
    public SpeciesInfoBo getSpeciesInfoBo(Long id, String uid) {

        SpeciesInfo speciesInfo = this.getById(id);

        if (speciesInfo == null) {
            return null;
        }

        SpeciesInfoBo speciesInfoBo = new SpeciesInfoBo(speciesInfo);

        if (speciesInfoBo.getVisible() == Constants.NO) {
            if (StrUtil.isNotEmpty(uid) && !uid.equals(speciesInfo.getCreateBy())) {
                //not self
                return null;
            }
        }
        commentService.getStaticsNum(speciesInfoBo);
        getCommentList(speciesInfoBo);
        speciesInfoBo.setUserInfo(userService.getById(speciesInfoBo.getCreateBy()));
        if (StrUtil.isNotEmpty(uid)) {
            QueryWrapper<LikePo> wrapper = new QueryWrapper<>();
            wrapper.eq("species_id", id).eq("create_by", uid);
            List<LikePo> likePos = speciesLikeDao.selectList(wrapper);
            if (likePos != null) {
                for (LikePo likePo : likePos) {
                    if (likePo.getLikee() != null && likePo.getLikee() == Constants.YES) {
                        speciesInfoBo.setLikeStatus(Constants.YES);
                    }
                    if (likePo.getCollect() != null && likePo.getCollect() == Constants.YES) {
                        speciesInfoBo.setCollectStatus(Constants.YES);
                    }
                }
            }
        }
        if (StrUtil.isNotEmpty(uid) && uid.equals(speciesInfo.getCreateBy())) {
            // self

        } else {
            //not self, add one view
            CommentPo viewPo = new CommentPo();
            viewPo.setVieww(Constants.YES);
            viewPo.setSpeciesId(id);
            viewPo.commonSet(uid);
            commentService.save(viewPo);
        }

        return speciesInfoBo;
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

    @Override
    public void getCommentList(SpeciesInfoBo infoBo) {
        LambdaQueryWrapper<CommentPo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommentPo::getSpeciesId, infoBo.getId()).isNotNull(CommentPo::getComment).orderByDesc(CommentPo::getUpdateTime);
        List<CommentPo> commentPos = commentService.getBaseMapper().selectList(wrapper);

        if (CollUtil.isNotEmpty(commentPos)) {
            Set<String> userIds = commentPos.stream().map(it -> it.getCreateBy()).collect(Collectors.toSet());
            LambdaQueryWrapper<UserPo> userWrapper = new LambdaQueryWrapper<>();
            userWrapper.in(CollUtil.isNotEmpty(userIds), UserPo::getUserId, userIds);
            List<UserPo> userList = userService.list(userWrapper);

            for (CommentPo commentPo : commentPos) {
                if (CollUtil.isNotEmpty(userList)) {
                    List<UserPo> users = userList.stream()
                            .filter(user -> StrUtil.equals(user.getUserId(), commentPo.getCreateBy()))
                            .collect(Collectors.toList());

                    if (CollUtil.isNotEmpty(users)) {
                        commentPo.setUserInfo(users.get(0));
                    }
                }

            }
        }

        infoBo.setCommentList(commentPos);
    }
}
