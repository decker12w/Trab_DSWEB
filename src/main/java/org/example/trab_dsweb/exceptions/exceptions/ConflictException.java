package org.example.trab_dsweb.exceptions.exceptions;

import org.example.trab_dsweb.exceptions.BaseException;
import org.springframework.http.HttpStatus;

public class ConflictException extends BaseException {
    public ConflictException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
