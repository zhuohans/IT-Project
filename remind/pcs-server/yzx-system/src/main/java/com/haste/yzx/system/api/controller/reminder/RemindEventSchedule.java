package com.haste.yzx.system.api.controller.reminder;

import com.haste.yzx.system.api.service.reminder.IReminderService;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 */

@Component
@EnableScheduling
public class RemindEventSchedule {

    @Resource
    IReminderService reminderService;

    @Scheduled(cron = "16 0/1 * * * ?")
    public void createRemindEvent() {
        System.out.println(new Date());
        reminderService.createRemindEvent();
    }
}
