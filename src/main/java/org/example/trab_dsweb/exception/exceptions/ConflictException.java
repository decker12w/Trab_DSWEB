package org.example.trab_dsweb.exception.exceptions;

import org.example.trab_dsweb.exception.BaseException;
import org.springframework.http.HttpStatus;

public class ConflictException extends BaseException {
    public ConflictException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
