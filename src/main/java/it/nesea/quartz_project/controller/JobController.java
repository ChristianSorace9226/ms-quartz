package it.nesea.quartz_project.controller;

import it.nesea.quartz_project.util.QuartzJob;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jobs")
@AllArgsConstructor
@Slf4j
public class JobController {

//    private final Scheduler scheduler;
//
//    @PostMapping("/start")
//    public String startJob(@RequestHeader ("Authorization") String token) throws SchedulerException {
//        try {
//            if (scheduler.isShutdown()) {
//                // Creare un nuovo scheduler
//                SchedulerFactory schedulerFactory = new StdSchedulerFactory();
//                Scheduler newScheduler = schedulerFactory.getScheduler();
//                newScheduler.start();
//                return "JOB ON!!";
//            }
//            // Logica per avviare il job
//        } catch (SchedulerException e) {
//            // Gestione delle eccezioni
//            log.error("Error starting job: ", e);
//            return "JOB OFF!!";
//        }
//        return "JOB gia avviato";
//    }
//
//    @PostMapping("/stop")
//    public String stopJob() throws SchedulerException {
//        scheduler.shutdown();
//        return "Job OFF!";
//    }
//
//    @GetMapping("/status")
//    public String getJobStatus() throws SchedulerException {
//        if (scheduler.isShutdown()) {
//            return "Scheduler OFF";
//        } else if (scheduler.isInStandbyMode()) {
//            return "Scheduler in Standby";
//        } else {
//            return "Scheduler ON";
//        }
//    }

    private final Scheduler scheduler;

    @PostMapping("/start")
    public String startJob(@RequestHeader("Authorization") String token) throws SchedulerException {
        try {
            JobKey jobKey = JobKey.jobKey("quartzJob", "group1");
            if (scheduler.checkExists(jobKey)) {
                return "JOB già avviato!";
            }

            JobDetail jobDetail = JobBuilder.newJob(QuartzJob.class)
                    .withIdentity(jobKey)
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .forJob(jobDetail)
                    .withIdentity("quartzJob", "group1")
                    .startNow()
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                            .withIntervalInSeconds(10)
                            .repeatForever())
                    .build();

            scheduler.scheduleJob(jobDetail, trigger);
            return "Job avviato con successo!";
        } catch (SchedulerException e) {
            log.error("Error starting job: ", e);
            return "Errore nell'avvio del job!";
        }
    }

    @PostMapping("/stop")
    public String stopJob() throws SchedulerException {
        JobKey jobKey = JobKey.jobKey("quartzJob", "group1");

        if (scheduler.checkExists(jobKey)) {
            scheduler.deleteJob(jobKey);
            return "Job fermato con successo!";
        } else {
            return "Job non trovato!";
        }
    }

    @GetMapping("/status")
    public String getJobStatus() throws SchedulerException {
        JobKey jobKey = JobKey.jobKey("quartzJob", "group1");

        if (scheduler.isShutdown()) {
            return "Scheduler OFF";
        } else if (scheduler.isInStandbyMode()) {
            return "Scheduler in Standby";
        } else if (scheduler.checkExists(jobKey)) {
            return "Job è attivo!";
        } else {
            return "Job non trovato!";
        }
    }
}

