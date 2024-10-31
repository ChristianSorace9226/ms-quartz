package it.nesea.quartz_project.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jobs")
@AllArgsConstructor
@Slf4j
public class JobController {

    private final Scheduler scheduler;


    @PostMapping("/start")
    public String startJob() throws SchedulerException {
        try {
            if (scheduler.isShutdown()) {
                // Creare un nuovo scheduler
                SchedulerFactory schedulerFactory = new StdSchedulerFactory();
                Scheduler newScheduler = schedulerFactory.getScheduler();
                newScheduler.start();
                return "JOB ON!!";
            }
            // Logica per avviare il job
        } catch (SchedulerException e) {
            // Gestione delle eccezioni
            log.error("Error starting job: ", e);
            return "JOB OFF!!";
        }
        return "JOB gia avviato";
    }

    @PostMapping("/stop")
    public String stopJob() throws SchedulerException {
        scheduler.shutdown();
        return "Job OFF!";
    }

    @GetMapping("/status")
    public String getJobStatus() throws SchedulerException {
        return scheduler.isStarted() ? "Job ON" : "Job OFF";
    }
}

