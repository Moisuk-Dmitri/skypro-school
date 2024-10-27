package ru.hogwarts.school.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmptyDatabaseException extends RuntimeException {

    public EmptyDatabaseException() {
        System.out.println("Empty database");
    }

    public EmptyDatabaseException(String e) {
        System.out.println(e);
    }
}
