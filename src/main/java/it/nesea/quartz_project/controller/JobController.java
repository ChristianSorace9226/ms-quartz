package it.nesea.quartz_project.controller;

import it.nesea.quartz_project.config.QuartzConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jobs")
@AllArgsConstructor
@Slf4j
public class JobController {

    private final Scheduler scheduler;
    private final QuartzConfig quartzConfig;

    @PostMapping("/start")
    public String startJob(@RequestHeader("Authorization") String token) throws SchedulerException {
        try {
            JobKey jobKey = JobKey.jobKey("expiredTokenCleanupJob");

            if (scheduler.checkExists(jobKey)) {
                scheduler.resumeJob(jobKey);
                return "Job ripreso con successo.";
            }

            JobDetail jobDetail = quartzConfig.expiredTokenCleanupJobDetail();
            Trigger trigger = quartzConfig.expiredTokenCleanupJobTrigger();

            scheduler.scheduleJob(jobDetail, trigger);
            scheduler.start();

            return "Job avviato con successo!";
        } catch (SchedulerException e) {
           return "Errore nell'avvio del job!";
        }
    }

    @PostMapping("/stop")
    public String stopJob(@RequestHeader("Authorization") String token) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey("expiredTokenCleanupJob");
        if (scheduler.checkExists(jobKey)) {
            scheduler.pauseJob(jobKey);
            return "Job fermato con successo!";
        } else {
            return "Job non trovato!";
        }
    }

    @GetMapping("/status")
    public String getJobStatus(@RequestHeader("Authorization") String token) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey("expiredTokenCleanupJob");
        TriggerKey triggerKey = TriggerKey.triggerKey("expiredTokenCleanupTrigger");

        if (scheduler.checkExists(jobKey)) {
            Trigger.TriggerState triggerState = scheduler.getTriggerState(triggerKey);
            return "Job è " + triggerState.name();
        } else {
            return "Job non trovato!";
        }
    }
}



