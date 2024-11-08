package ru.hogwarts.school.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "files operation couldn't be completed")
public class FilesOperationStoppedException extends RuntimeException {

    public FilesOperationStoppedException() {
    }

    public FilesOperationStoppedException(String message) {
        super(message);
    }
}
