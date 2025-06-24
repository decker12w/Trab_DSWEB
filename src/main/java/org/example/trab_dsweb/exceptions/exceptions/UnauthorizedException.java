package org.example.trab_dsweb.exceptions.exceptions;

import org.example.trab_dsweb.exceptions.BaseException;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends BaseException {
  public UnauthorizedException(String message) {
    super(HttpStatus.UNAUTHORIZED, message);
  }
}
