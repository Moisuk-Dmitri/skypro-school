package ru.hogwarts.school.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Min age must be less than max")
public class AgeMinAboveMaxException extends RuntimeException {

    public AgeMinAboveMaxException() {
        super("Min age must be less than max");
    }

    public AgeMinAboveMaxException(String message) {
        super(message);
    }
}
