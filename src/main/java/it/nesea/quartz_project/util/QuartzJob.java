package it.nesea.quartz_project.util;

import it.nesea.quartz_project.controller.JwtExpiriedService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuartzJob implements Job {

    private final JwtExpiriedService jwtExpiriedService;

    @Autowired
    public QuartzJob(JwtExpiriedService jwtExpiriedService) {
        this.jwtExpiriedService = jwtExpiriedService;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        if (jwtExpiriedService != null) {
            jwtExpiriedService.deleteExpiredTokens();
        } else {
            System.out.println("JwtExpiriedService non funziona");
        }
    }
}



