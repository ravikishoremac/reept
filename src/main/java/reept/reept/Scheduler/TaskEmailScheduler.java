package reept.reept.Scheduler;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import reept.reept.Service.ReminderService;

@Component
public class TaskEmailScheduler {

    @Autowired
    private ReminderService reminderService;

    // Runs every day at 10 AM
    @Scheduled(cron = "0 0 10 * * ?", zone = "Asia/Kolkata")
    public void runDailyTaskReminder() {
        reminderService.sendDailyTaskEmailsToAllEmployees();
    }
}

