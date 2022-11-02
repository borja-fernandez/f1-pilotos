package es.f1.pilotos.command.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DuplicateObjectException extends RuntimeException{

    public DuplicateObjectException(String errorMessage) {
        super(errorMessage);
    }
}
