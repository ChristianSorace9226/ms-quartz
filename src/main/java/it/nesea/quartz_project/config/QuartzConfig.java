package it.nesea.quartz_project.config;

import it.nesea.quartz_project.util.PersonalJobFactory;
import it.nesea.quartz_project.util.QuartzJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class QuartzConfig {

    private final PersonalJobFactory jobFactory;

    public QuartzConfig(PersonalJobFactory jobFactory) {
        this.jobFactory = jobFactory;
    }

    // Definisco i bean per JobKey e TriggerKey
    @Bean
    public JobKey jobKey() {
        return JobKey.jobKey("expiredTokenCleanupJob");
    }

    @Bean
    public TriggerKey triggerKey() {
        return TriggerKey.triggerKey("expiredTokenCleanupTrigger");
    }

    @Lazy
    @Bean
    public Scheduler scheduler() throws Exception {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        scheduler.setJobFactory(jobFactory);
        return scheduler;
    }

    public JobDetail expiredTokenCleanupJobDetail() {
        return JobBuilder.newJob(QuartzJob.class)
                .withIdentity("expiredTokenCleanupJob")
                .storeDurably()
                .build();
    }

    public Trigger expiredTokenCleanupJobTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(expiredTokenCleanupJobDetail())
                .withIdentity("expiredTokenCleanupTrigger")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInMinutes(1) // Ogni min
                        .repeatForever())
                .build();
    }
}







