package ru.hogwarts.school.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WrongAgeException extends RuntimeException {

    public WrongAgeException() {
        System.out.println("Wrong age");
    }

    public WrongAgeException(String e) {
        System.out.println(e);
    }
}
