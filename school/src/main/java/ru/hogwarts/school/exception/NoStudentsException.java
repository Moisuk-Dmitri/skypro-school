package ru.hogwarts.school.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "No provided students")
public class NoStudentsException extends RuntimeException {

    public NoStudentsException() {
        super("No provided students");
    }

    public NoStudentsException(String message) {
        super(message);
    }
}
