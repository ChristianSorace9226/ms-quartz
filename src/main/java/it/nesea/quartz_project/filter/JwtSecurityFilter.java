package it.nesea.quartz_project.filter;

import it.nesea.quartz_project.service.resource.TokenValidationResource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
@Slf4j
public class JwtSecurityFilter extends OncePerRequestFilter {

    private final TokenValidationResource tokenValidationResource;


    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            Boolean isValid = tokenValidationResource.isValidToken(authorizationHeader, request.getRequestURI());
            if (Boolean.TRUE.equals(isValid)) {
                chain.doFilter(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token non valido");
            }
        } else {
            chain.doFilter(request, response);
        }
    }
}