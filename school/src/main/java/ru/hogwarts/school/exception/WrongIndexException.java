package ru.hogwarts.school.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WrongIndexException extends RuntimeException {

    public WrongIndexException() {
        super("Wrong index");
    }

    public WrongIndexException(String message) {
        super(message);
    }
}
