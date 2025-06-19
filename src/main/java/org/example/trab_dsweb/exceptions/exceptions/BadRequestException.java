package org.example.trab_dsweb.exceptions.exceptions;

import org.example.trab_dsweb.exceptions.BaseException;
import org.springframework.http.HttpStatus;

public class BadRequestException extends BaseException {

    public BadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
