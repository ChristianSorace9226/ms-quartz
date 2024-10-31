package it.nesea.quartz_project.config;


import it.nesea.quartz_project.controller.JwtExpiriedService;
import it.nesea.quartz_project.util.QuartzJob;
import lombok.AllArgsConstructor;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

@Configuration

public class QuartzConfig {

    private final Scheduler scheduler;
//    private final JwtExpiriedService jwtExpiriedService;

    public QuartzConfig(@Lazy Scheduler scheduler) {
        this.scheduler = scheduler;
//        this.jwtExpiriedService = jwtExpiriedService;, JwtExpiriedService jwtExpiriedService
    }

    @Lazy
    @Bean
    public Scheduler scheduler() throws Exception {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        scheduler.start();
        return scheduler;
    }


//    @Bean
//    public JobDetail expiredTokenCleanupJobDetail() {
//        return JobBuilder.newJob(QuartzJob.class)
//                .withIdentity("expiredTokenCleanupJob")
//                .storeDurably()
//                .build();
//    }
//
//    @Lazy
//    @Bean
//    public Trigger expiredTokenCleanupJobTrigger() {
//        return TriggerBuilder.newTrigger()
//                .forJob(expiredTokenCleanupJobDetail())
//                .withIdentity("expiredTokenCleanupTrigger")
//                .withSchedule(simpleSchedule()
//                        .withIntervalInMinutes(1) // Ogni min
//                        .repeatForever())
//                .build();
//    }
}


