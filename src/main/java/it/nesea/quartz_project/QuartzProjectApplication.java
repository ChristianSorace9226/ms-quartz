package it.nesea.quartz_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class QuartzProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuartzProjectApplication.class, args);
    }

}
