package ru.hogwarts.school.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WrongAgeException extends RuntimeException {

    public WrongAgeException() {
        super("Wrong age");
    }

    public WrongAgeException(String message) {
        super(message);
    }
}
