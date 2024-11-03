package it.nesea.quartz_project.util;

import lombok.NonNull;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.stereotype.Component;

@Component
public class PersonalJobFactory extends SpringBeanJobFactory {

    private final ApplicationContext applicationContext;


    public PersonalJobFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        setApplicationContext(applicationContext);
    }

    @NonNull
    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        return applicationContext.getAutowireCapableBeanFactory().createBean(bundle.getJobDetail().getJobClass());
    }
}


