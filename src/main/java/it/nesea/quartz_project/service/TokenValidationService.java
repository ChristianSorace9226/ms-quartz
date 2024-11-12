package it.nesea.quartz_project.service;

import it.nesea.quartz_project.common.AppValue;
import it.nesea.quartz_project.response.CustomResponse;
import it.nesea.quartz_project.service.resource.TokenValidationResource;
import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@AllArgsConstructor
public class TokenValidationService implements TokenValidationResource {

    private final AppValue appValue;
    private final RestTemplate restTemplate;

    @Override
    public CustomResponse<Boolean> isValidToken(@Nonnull String authorizationHeader, @Nonnull String requestURI) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authorizationHeader);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            String validationUrl = UriComponentsBuilder
                    .fromHttpUrl(appValue.getHasAccessUrl())
                    .queryParam("uri", requestURI)
                    .toUriString();

            ResponseEntity<CustomResponse<Boolean>> responseEntity = restTemplate.exchange(
                    validationUrl,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<>() { // N.b. " <> " stanno per <CustomResponse<Boolean>
                    }
            );

            return responseEntity.getBody();
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}


