package org.example.trab_dsweb.exception.exceptions;

import org.example.trab_dsweb.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictException extends BaseException {
    public ConflictException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
