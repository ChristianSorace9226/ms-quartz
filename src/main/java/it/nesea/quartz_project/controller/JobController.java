package it.nesea.quartz_project.controller;

import it.nesea.quartz_project.config.QuartzConfig;
import it.nesea.quartz_project.response.CustomResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/jobs")
@AllArgsConstructor
@Slf4j
public class JobController {

    private final Scheduler scheduler;
    private final QuartzConfig quartzConfig;
    private final JobKey jobKey;
    private final TriggerKey triggerKey;


    @PostMapping("/start")
    public ResponseEntity<CustomResponse<String>> startJob(@RequestHeader("Authorization") String token) throws SchedulerException {
        try {
            if (scheduler.checkExists(jobKey)) {
                scheduler.resumeJob(jobKey);
                return ResponseEntity.ok(CustomResponse.success("Job ripreso con successo."));
            }

            JobDetail jobDetail = quartzConfig.expiredTokenCleanupJobDetail();
            Trigger trigger = quartzConfig.expiredTokenCleanupJobTrigger();

            scheduler.scheduleJob(jobDetail, trigger);
            scheduler.start();

            return ResponseEntity.ok(CustomResponse.success("Job avviato con successo!"));
        } catch (SchedulerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CustomResponse.error(HttpStatus.NOT_FOUND.value(),
                    e.getMessage()));
        }
    }

    @PostMapping("/stop")
    public ResponseEntity<CustomResponse<String>> stopJob(@RequestHeader("Authorization") String token) throws SchedulerException {
        try {
            scheduler.checkExists(jobKey);
            scheduler.pauseJob(jobKey);
            return ResponseEntity.ok(CustomResponse.success("Job fermato con successo!"));
        } catch (SchedulerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CustomResponse.error(HttpStatus.NOT_FOUND.value(),
                    e.getMessage()));
        }
    }

    @GetMapping("/status")
    public ResponseEntity<CustomResponse<String>> getJobStatus(@RequestHeader("Authorization") String token) throws SchedulerException {
        try {
            scheduler.checkExists(jobKey);
            Trigger.TriggerState triggerState = scheduler.getTriggerState(triggerKey);
            return ResponseEntity.ok(CustomResponse.success("Job è " + triggerState.name()));
        } catch (SchedulerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CustomResponse.error(HttpStatus.NOT_FOUND.value(), "Job non trovato!"));
        }
    }
}



