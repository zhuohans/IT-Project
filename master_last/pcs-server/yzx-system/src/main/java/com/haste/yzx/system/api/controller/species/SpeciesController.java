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
@Tag(name = "Species Services")
public class SpeciesController extends BaseUserController {

    @Resource
    private ISpeciesService speciesService;

    @Operation(summary = "Get the list of newly uploaded species")
    @PostMapping("/list/news")
    public Response<Page<SpeciesInfo>> getSpeciesNewsList(@RequestBody SpeciesQueryInfo queryInfo) {
        return Response.success(speciesService.getSpeciesNewsList(queryInfo));
    }

    @PostMapping(value = "uploadFile", headers = "content-type=multipart/form-data")
    @Operation(summary = "Upload a file, max size is 10M, returns HTTP address")
    public Response<String> uploadFile(@RequestParam(value = "file", required = true) MultipartFile file) throws IOException {
        return Response.success(FileInfoUtil.saveFile(file));
    }

    @Operation(summary = "Delete species")
    @PostMapping("/del/{id}")
    public Response deleteSpecies(@PathVariable Long id) {
        if (id != null) {
            speciesService.removeById(id);
        }
        return Response.success();
    }

    @Operation(summary = "Add a species")
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
        return Response.error("Failed to execute addSpecies!");
    }

    @Operation(summary = "Update species")
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
        return Response.error("Failed to execute updateSpecies!");
    }

    @Operation(summary = "Publish/Unpublish species")
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

    @Operation(summary = "Like species")
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

    @Operation(summary = "Collect species")
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

    @Operation(summary = "View species")
    @PostMapping("/view/{id}")
    public Response<SpeciesInfoBo> viewSpecies(@PathVariable Long id) {
        String uid = super.getUserInfo().getUserId();
        SpeciesInfoBo speciesInfoBo = speciesService.getSpeciesInfoBo(id, uid);
        if (speciesInfoBo == null) {
            return Response.error("Can't find the species");
        }
        return Response.success(speciesInfoBo);
    }

    @Operation(summary = "Comment on species")
    @PostMapping("/comment/{id}")
    public Response<SpeciesInfoBo> commentSpecies(@PathVariable Long id, @RequestBody Map<String, String> map, Integer act) {
        String uid = super.getUserInfo().getUserId();
        if (StrUtil.isEmpty(uid)) {
            return Response.error(ResponseEnum.UNAUTHORIZED.code, ResponseEnum.UNAUTHORIZED.msg);
        }
        String comment = map.get("comment");
        speciesService.commentSpecies(id, comment, uid);

        return commentSpecies(id);
    }

    /**
     * @param id        Post ID
     * @param commentId Comment ID
     * @return Response
     */
    @Operation(summary = "Delete comment")
    @PostMapping("/comment/del/{id}/{commentId}")
    public Response<SpeciesInfoBo> deleteComment(@PathVariable(name = "id") Long id, @PathVariable(name = "commentId") Long commentId) {
        String uid = super.getUserInfo().getUserId();
        if (StrUtil.isEmpty(uid)) {
            return Response.error(ResponseEnum.UNAUTHORIZED.code, ResponseEnum.UNAUTHORIZED.msg);
        }
        speciesService.deleteComment(id, commentId, uid);

        return commentSpecies(id);
    }

    @Operation(summary = "List of comments")
    @PostMapping("/comment/list")
    public Response<SpeciesInfoBo> commentSpecies(@PathVariable Long id) {
        SpeciesInfoBo infoBo = new SpeciesInfoBo();
        infoBo.setId(id);
        speciesService.getCommentList(infoBo);
        return Response.success(infoBo);
    }

}