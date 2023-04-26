package com.intraviologistica.intravio.service.exceptions;

public class AuthenticateErrorException extends RuntimeException {
    public AuthenticateErrorException(String msg) {
        super(msg);
    }
}
