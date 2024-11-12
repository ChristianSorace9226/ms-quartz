package it.nesea.quartz_project.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomResponseException extends RuntimeException {
    private final String errorMessage;

    public CustomResponseException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

}
