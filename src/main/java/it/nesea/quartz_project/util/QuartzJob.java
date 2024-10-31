package it.nesea.quartz_project.util;

import it.nesea.quartz_project.controller.JwtExpiriedService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class QuartzJob implements Job {

    private final JwtExpiriedService jwtExpiriedService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        jwtExpiriedService.deleteExpiredTokens();
    }
}

