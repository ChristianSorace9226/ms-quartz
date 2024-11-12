package it.nesea.quartz_project.filter;

import it.nesea.quartz_project.exception.CustomResponseException;
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


//    @Override
//    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain) throws ServletException, IOException {
//        String authorizationHeader = request.getHeader("Authorization");
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            try {
//                CustomResponse<Boolean> isValidCall = tokenValidationResource.isValidToken(authorizationHeader, request.getRequestURI());
//
//                Boolean isValid = isValidCall.getResponse();
//                String errorMessage = isValidCall.getErrorMessage();
//
//                if (Boolean.TRUE.equals(isValid)) {
//                    chain.doFilter(request, response);
//                }
//
//            } catch (RuntimeException e) {
//
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                response.getWriter().write(e.getMessage());
//            }
//        }
//    }



    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                // Chiamata per validare il token
                CustomResponse<Boolean> isValidCall = tokenValidationResource.isValidToken(authorizationHeader, request.getRequestURI());
                Boolean isValid = isValidCall.getResponse();
                String errorMessage = isValidCall.getErrorMessage(); // Messaggio di errore dalla classe CustomResponse

                if (Boolean.TRUE.equals(isValid)) {
                    // Se il token è valido, continua la catena di filtri
                    chain.doFilter(request, response);
                } else {
                    // Se il token non è valido, restituisci errore con il messaggio dalla classe CustomResponse
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write(errorMessage != null ? errorMessage : "Token non valido");
                }
            } catch (RuntimeException e) {
                // Se c'è un errore durante la validazione, prendi l'errore da CustomResponse
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

                // Se isValidCall è stato settato e contiene errorMessage, usalo. Altrimenti, usa l'eccezione.
                String errorMessage = e instanceof CustomResponseException
                        ? ((CustomResponseException) e).getErrorMessage() // Qui puoi accedere a CustomResponseException, se definito
                        : e.getMessage(); // Messaggio dall'eccezione se non è un CustomResponseException.

                response.getWriter().write(errorMessage != null ? errorMessage : "L'autenticazione non è andata a buon fine");
            }
        } else {
            // Se l'Authorization header è assente o non valido
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Header non presente o non valido");
        }
    }


}