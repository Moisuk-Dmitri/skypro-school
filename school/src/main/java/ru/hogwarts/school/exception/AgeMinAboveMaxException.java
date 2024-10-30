package ru.hogwarts.school.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AgeMinAboveMaxException extends RuntimeException {

    public AgeMinAboveMaxException() {
        super("Min age must be less than max");
    }

    public AgeMinAboveMaxException(String message) {
        super(message);
    }
}
