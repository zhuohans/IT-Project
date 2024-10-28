package com.haste.yzx.system.api.controller.user;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.haste.yzx.common.enums.ResponseEnum;
import com.haste.yzx.common.response.Response;
import com.haste.yzx.system.api.domain.bo.species.FootprintsRspBo;
import com.haste.yzx.system.api.domain.po.species.SpeciesInfo;
import com.haste.yzx.system.api.domain.po.user.CommentPo;
import com.haste.yzx.system.api.domain.po.user.GardenPo;
import com.haste.yzx.system.api.service.species.IGardenService;
import com.haste.yzx.system.api.service.user.IUserPageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/userpage")
@Tag(name = "User Homepage")
public class UserPageController extends BaseUserController {

    @Resource
    private IUserPageService userPageService;

    @Resource
    private IGardenService gardenService;

    /**
     * @param type
     * Browsing species, liking species, and commenting on species will generate footprints
     * @see com.haste.yzx.common.enums.UserActEnum
     */
    @Operation(summary = "Get Footprints List")
    @PostMapping("/footprints/list/{type}")
    public Response<FootprintsRspBo> getFootprintsList(@RequestBody Page page, @PathVariable Integer type) {
        String uid = super.getUserInfo().getUserId();
        if (StrUtil.isEmpty(uid)) {
            return Response.error(ResponseEnum.UNAUTHORIZED.code, ResponseEnum.UNAUTHORIZED.msg);
        }
        return Response.success(userPageService.getFootprintsList(page, uid, type));
    }

    @Operation(summary = "Get Photos List")
    @PostMapping("/photos/list")
    public Response<SpeciesInfo> getPhotosList(@RequestBody Page page) {
        String uid = super.getUserInfo().getUserId();
        if (StrUtil.isEmpty(uid)) {
            return Response.error(ResponseEnum.UNAUTHORIZED.code, ResponseEnum.UNAUTHORIZED.msg);
        }
        return Response.success(userPageService.getPhotosList(page, uid));
    }

    @Operation(summary = "Get Collection List")
    @PostMapping("/collect/list")
    public Response<SpeciesInfo> getCollectList(@RequestBody Page page) {
        String uid = super.getUserInfo().getUserId();
        if (StrUtil.isEmpty(uid)) {
            return Response.error(ResponseEnum.UNAUTHORIZED.code, ResponseEnum.UNAUTHORIZED.msg);
        }
        return Response.success(userPageService.getCollectPhotosList(page, uid));
    }

    @Operation(summary = "Received Likes/Comments Count")
    @PostMapping("/receive/cnt")
    public Response<CommentPo> getReceivedCount() {
        String uid = super.getUserInfo().getUserId();
        if (StrUtil.isEmpty(uid)) {
            return Response.error(ResponseEnum.UNAUTHORIZED.code, ResponseEnum.UNAUTHORIZED.msg);
        }
        return Response.success(userPageService.getUserStatisticNum(uid));
    }

    @Operation(summary = "Garden List")
    @PostMapping("/garden/list")
    public Response<GardenPo> getGardenList() {
        String uid = super.getUserInfo().getUserId();
        if (StrUtil.isEmpty(uid)) {
            return Response.error(ResponseEnum.UNAUTHORIZED.code, ResponseEnum.UNAUTHORIZED.msg);
        }
        return Response.success(gardenService.list(new QueryWrapper<GardenPo>().eq("create_by", uid).orderByAsc("sort")));
    }

    @Operation(summary = "Add Garden Plant")
    @PostMapping("/garden/add")
    public Response<List<GardenPo>> addGardenPlant(@RequestBody GardenPo gardenPo) {
        String uid = super.getUserInfo().getUserId();
        if (StrUtil.isEmpty(uid)) {
            return Response.error(ResponseEnum.UNAUTHORIZED.code, ResponseEnum.UNAUTHORIZED.msg);
        }
        gardenPo.commonSet(uid);

        gardenService.save(gardenPo);
        return Response.success(gardenService.list(new QueryWrapper<GardenPo>().eq("create_by", uid)));
    }

    @Operation(summary = "Update Garden Plant")
    @PostMapping("/garden/update")
    public Response<List<GardenPo>> updateGardenPlant(@RequestBody GardenPo gardenPo) {
        String uid = super.getUserInfo().getUserId();
        if (StrUtil.isEmpty(uid)) {
            return Response.error(ResponseEnum.UNAUTHORIZED.code, ResponseEnum.UNAUTHORIZED.msg);
        }
        gardenPo.commonSet(uid);

        gardenService.updateById(gardenPo);
        return Response.success(gardenService.list(new QueryWrapper<GardenPo>().eq("create_by", uid)));
    }

    @Operation(summary = "Delete Garden Plant")
    @PostMapping("/garden/delete")
    public Response<List<GardenPo>> deleteGardenPlant(@RequestBody GardenPo gardenPo) {
        String uid = super.getUserInfo().getUserId();
        if (StrUtil.isEmpty(uid)) {
            return Response.error(ResponseEnum.UNAUTHORIZED.code, ResponseEnum.UNAUTHORIZED.msg);
        }
        gardenPo.commonSet(uid);

        gardenService.removeById(gardenPo);
        return Response.success(gardenService.list(new QueryWrapper<GardenPo>().eq("create_by", uid)));
    }

    @Operation(summary = "Add Garden Plant List")
    @PostMapping("/garden/addList")
    public Response<List<GardenPo>> addGardenPlantList(@RequestBody List<GardenPo> gardenPoList) {
        String uid = super.getUserInfo().getUserId();
        if (StrUtil.isEmpty(uid)) {
            return Response.error(ResponseEnum.UNAUTHORIZED.code, ResponseEnum.UNAUTHORIZED.msg);
        }
        for (GardenPo gardenPo : gardenPoList) {
            gardenPo.commonSet(uid);
        }
        gardenService.saveBatch(gardenPoList);
        return Response.success(gardenService.list(new QueryWrapper<GardenPo>().eq("create_by", uid)));
    }
}