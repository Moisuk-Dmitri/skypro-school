package ru.hogwarts.school.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmptyColorException extends RuntimeException {

    public EmptyColorException() {
        super("No provided color");
    }

    public EmptyColorException(String message) {
        super(message);
    }
}
