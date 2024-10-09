package com.haste.yzx.system.api.controller.species;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.haste.yzx.common.Constants;
import com.haste.yzx.common.enums.ResponseEnum;
import com.haste.yzx.common.response.Response;
import com.haste.yzx.common.utils.FileInfoUtil;
import com.haste.yzx.system.api.controller.user.BaseUserController;
import com.haste.yzx.system.api.domain.bo.species.SpeciesInfoBo;
import com.haste.yzx.system.api.domain.bo.species.SpeciesQueryInfo;
import com.haste.yzx.system.api.domain.po.species.SpeciesInfo;
import com.haste.yzx.system.api.service.species.ISpeciesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/species")
@Tag(name = "物种服务")
public class SpeciesController extends BaseUserController {

    @Resource
    private ISpeciesService speciesService;



    @Operation(summary = "获取新上传的物种列表")
    @PostMapping("/list/news")
    public Response<Page<SpeciesInfo>> getSpeciesNewsList(@RequestBody SpeciesQueryInfo queryInfo) {
        return Response.success(speciesService.getSpeciesNewsList(queryInfo));
    }



    @PostMapping(value = "uploadFile", headers = "content-type=multipart/form-data")
    @Operation(summary = "上传文件，上限为10M,返回http地址")
    public Response<String> uploadFile(@RequestParam(value = "file", required = true) MultipartFile file) throws IOException {

        return Response.success(FileInfoUtil.saveFile(file));
    }

    @Operation(summary = "删除物种")
    @PostMapping("/del/{id}")
    public Response delSpeciesList(@PathVariable Long id) {
        if (null != id) {
            speciesService.removeById(id);
        }
        return Response.success();
    }

    @Operation(summary = "新增物种")
    @PostMapping("/add")
    public Response addSpecies(@RequestBody SpeciesInfo info) {
        String uid = super.getUserInfo().getUserId();
        if (StrUtil.isEmpty(uid)) {
            return Response.error(ResponseEnum.UNAUTHORIZED.code, ResponseEnum.UNAUTHORIZED.msg);
        }
        if (info != null) {
            info.commonSet(uid);
            info.setVisible(Constants.YES);
            speciesService.save(info);
            return Response.success(info);
        }
        return Response.error(" execute addSpecies failed!");


    }

    @Operation(summary = "修改物种")
    @PostMapping("/update")
    public Response updateSpecies(@RequestBody SpeciesInfo info) {
        String uid = super.getUserInfo().getUserId();
        if (StrUtil.isEmpty(uid)) {
            return Response.error(ResponseEnum.UNAUTHORIZED.code, ResponseEnum.UNAUTHORIZED.msg);
        }
        if (info != null) {
            info.commonSetUpdate(uid);
            speciesService.updateById(info);
            return Response.success(info);
        }
        return Response.error(" execute updateSpecies failed!");
    }

    @Operation(summary = "发布/不发布物种")
    @PostMapping("/publish/{id}")
    public Response publishSpecies(@PathVariable Long id, Integer visible) {
        String uid = super.getUserInfo().getUserId();
        if (StrUtil.isEmpty(uid)) {
            return Response.error(ResponseEnum.UNAUTHORIZED.code, ResponseEnum.UNAUTHORIZED.msg);
        }
        if (visible == null) {
            visible = Constants.NO;
        }
        speciesService.publishSpecies(id, visible, uid);

        return Response.success();
    }

    @Operation(summary = "点赞")
    @PostMapping("/like/{id}")
    public Response likeSpecies(@PathVariable Long id, Integer like) {
        if (like == null) {
            like = Constants.NO;
        }
        String uid = super.getUserInfo().getUserId();
        if (StrUtil.isEmpty(uid)) {
            return Response.error(ResponseEnum.UNAUTHORIZED.code, ResponseEnum.UNAUTHORIZED.msg);
        }
        speciesService.likeSpecies(id, like, uid);
        return Response.success();
    }

    @Operation(summary = "收藏")
    @PostMapping("/collect/{id}")
    public Response collectSpecies(@PathVariable Long id, Integer collect) {
        if (collect == null) {
            collect = Constants.NO;
        }
        String uid = super.getUserInfo().getUserId();
        if (StrUtil.isEmpty(uid)) {
            return Response.error(ResponseEnum.UNAUTHORIZED.code, ResponseEnum.UNAUTHORIZED.msg);
        }

        speciesService.collectSpecies(id, collect, uid);
        return Response.success();
    }

    @Operation(summary = "浏览")
    @PostMapping("/view/{id}")
    public Response<SpeciesInfoBo> viewSpecies(@PathVariable Long id) {
        String uid = super.getUserInfo().getUserId();
        SpeciesInfoBo speciesInfoBo = speciesService.getSpeciesInfoBo(id, uid);
        if (speciesInfoBo == null) {
            return Response.error("can't find the species");
        }
        return Response.success(speciesInfoBo);
    }

    @Operation(summary = "评论")
    @PostMapping("/comment/{id}")
    public Response<SpeciesInfoBo> commentSpecies(@PathVariable Long id, @RequestBody Map<String,String> map, Integer act) {
        String uid = super.getUserInfo().getUserId();
        if (StrUtil.isEmpty(uid)) {
            return Response.error(ResponseEnum.UNAUTHORIZED.code, ResponseEnum.UNAUTHORIZED.msg);
        }
        String comment = map.get("comment");
        speciesService.commentSpecies(id, comment, uid);

        return commentSpecies(id);
    }

    /**
     * @param id  帖子id
     * @param commentId  评论id
     * @return
     */
    @Operation(summary = "删除评论")
    @PostMapping("/comment/del/{id}/{commentId}")
    public Response<SpeciesInfoBo> delCommentSpecies(@PathVariable(name = "id") Long id,@PathVariable(name = "commentId") Long commentId) {
        String uid = super.getUserInfo().getUserId();
        if (StrUtil.isEmpty(uid)) {
            return Response.error(ResponseEnum.UNAUTHORIZED.code, ResponseEnum.UNAUTHORIZED.msg);
        }
        speciesService.delCommentSpecies(id,commentId, uid);

        return commentSpecies(id);
    }

    @Operation(summary = "评论列表")
    @PostMapping("/comment/list")
    public Response<SpeciesInfoBo> commentSpecies(@PathVariable Long id) {
        SpeciesInfoBo infoBo = new SpeciesInfoBo();
        infoBo.setId(id);
        speciesService.getCommentList(infoBo);
        return Response.success(infoBo);
    }

}
