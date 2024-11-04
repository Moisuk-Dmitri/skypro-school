package ru.hogwarts.school.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "No provided faculties")
public class NoFacultiesException extends RuntimeException{

    public NoFacultiesException() {
        super("No provided faculties");
    }

    public NoFacultiesException(String message) {
        super(message);
    }
}
