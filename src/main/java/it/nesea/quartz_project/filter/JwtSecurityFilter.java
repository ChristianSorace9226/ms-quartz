package it.nesea.quartz_project.filter;

import it.nesea.quartz_project.response.CustomResponse;
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
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                // Chiamata per validare il token
                CustomResponse<Boolean> isValidCall = tokenValidationResource.isValidToken(authorizationHeader, request.getRequestURI());
                Boolean isValid = isValidCall.getResponse();
                String errorMessage = isValidCall.getErrorMessage(); // Messaggio di errore dalla classe CustomResponse
                int errorCode = isValidCall.getResult();

                if (Boolean.TRUE.equals(isValid)) {
                    chain.doFilter(request, response);
                } else {
                    response.setStatus(errorCode);
                    response.getWriter().write(errorMessage != null ? errorMessage : "Token non valido");
                }
            } catch (RuntimeException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write(e.getMessage());
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Header non presente o non valido");
        }
    }


}