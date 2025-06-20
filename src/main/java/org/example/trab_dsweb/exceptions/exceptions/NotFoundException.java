package org.example.trab_dsweb.exceptions.exceptions;

import org.example.trab_dsweb.exceptions.BaseException;
import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseException {
    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
