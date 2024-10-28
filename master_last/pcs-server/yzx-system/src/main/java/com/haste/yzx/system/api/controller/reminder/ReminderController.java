package com.haste.yzx.system.api.controller.reminder;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.haste.yzx.common.enums.ResponseEnum;
import com.haste.yzx.common.exception.BadRequestException;
import com.haste.yzx.common.response.Response;
import com.haste.yzx.system.api.controller.user.BaseUserController;
import com.haste.yzx.system.api.dao.reminder.ReminderDoingDao;
import com.haste.yzx.system.api.domain.po.reminder.ReminderDoingPo;
import com.haste.yzx.system.api.domain.po.reminder.ReminderPo;
import com.haste.yzx.system.api.service.reminder.IReminderService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/reminder")
public class ReminderController extends BaseUserController {

    @Resource
    private IReminderService reminderService;

    @Resource
    ReminderDoingDao reminderDoingDao;

    @Operation(summary = "Create Reminder")
    @PostMapping("/create")
    public Response create(@RequestBody ReminderPo reminderPo) {
        if (reminderPo.getTime() != null) {
            reminderPo.setTime(DateTime.of(reminderPo.getTime()).setField(DateField.SECOND, 0));
        }
        return Response.success(reminderService.create(reminderPo));
    }

    @Operation(summary = "Update Reminder")
    @PostMapping("/update")
    public Response update(@RequestBody ReminderPo reminderPo) {
        if (reminderPo.getTime() != null) {
            reminderPo.setTime(DateTime.of(reminderPo.getTime()).setField(DateField.SECOND, 0));
        }
        return Response.success(reminderService.updateById(reminderPo));
    }

    @Operation(summary = "Get Reminder List")
    @PostMapping("/list")
    public Response getReminderList() {
        String uid = this.getUserInfo().getUserId();
        if (uid == null) {
            throw new BadRequestException(ResponseEnum.UNAUTHORIZED.code, ResponseEnum.UNAUTHORIZED.msg);
        }
        return Response.success(reminderService.getReminderList(uid));
    }

    @Operation(summary = "Get Upcoming Events")
    @PostMapping("/list/being")
    public Response getBeingReminderList() {
        String uid = this.getUserInfo().getUserId();
        if (StrUtil.isEmpty(uid)) {
            return Response.error(ResponseEnum.UNAUTHORIZED.code, ResponseEnum.UNAUTHORIZED.msg);
        }
        return Response.success(reminderService.getReminderListDoing(uid));
    }

    @Operation(summary = "Confirm Upcoming Event")
    @PostMapping("/being/update/{id}")
    public Response dealBeingRemind(@PathVariable Long id) {
        String uid = this.getUserInfo().getUserId();
        if (StrUtil.isEmpty(uid)) {
            return Response.error(ResponseEnum.UNAUTHORIZED.code, ResponseEnum.UNAUTHORIZED.msg);
        }

        UpdateWrapper<ReminderDoingPo> wrapper = new UpdateWrapper<>();
        wrapper.eq("create_by", uid);
        wrapper.eq("id", id);
        wrapper.set("status", 1);
        return Response.success(reminderDoingDao.update(wrapper));
    }

    @Operation(summary = "One-Click Confirm Upcoming Events")
    @PostMapping("/being/updateAll")
    public Response dealBeingRemindAll() {
        String uid = this.getUserInfo().getUserId();
        if (StrUtil.isEmpty(uid)) {
            return Response.error(ResponseEnum.UNAUTHORIZED.code, ResponseEnum.UNAUTHORIZED.msg);
        }

        UpdateWrapper<ReminderDoingPo> wrapper = new UpdateWrapper<>();
        wrapper.eq("create_by", uid);
        wrapper.set("status", 1);
        return Response.success(reminderDoingDao.update(wrapper));
    }

    @Operation(summary = "Delete Reminder")
    @PostMapping("/delete/{id}")
    public Response delete(@PathVariable Long id) {
        return Response.success(reminderService.delete(id));
    }

    @Operation(summary = "Create Event")
    @PostMapping("/create/event")
    public void createRemindEvent() {
        System.out.println(new Date());
        reminderService.createRemindEvent();
    }
}