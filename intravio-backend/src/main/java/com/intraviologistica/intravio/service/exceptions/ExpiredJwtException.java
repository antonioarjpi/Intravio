package com.intraviologistica.intravio.service.exceptions;

public class ExpiredJwtException extends RuntimeException {
    public ExpiredJwtException(String msg) {
        super(msg);
    }
}
