package it.nesea.quartz_project.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;


@FeignClient(name = "JwtExpiriedService", url = "http://localhost:8080/jwt")
@Component
public interface JwtExpiriedService {

    @DeleteMapping("/quartz/remove-token")
    void deleteExpiredTokens();
}
