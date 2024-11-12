package it.nesea.quartz_project.service.resource;

import it.nesea.quartz_project.response.CustomResponse;
import jakarta.annotation.Nonnull;

public interface TokenValidationResource {
    public CustomResponse<Boolean> isValidToken(@Nonnull String authorizationHeader, @Nonnull String requestURI);
}
