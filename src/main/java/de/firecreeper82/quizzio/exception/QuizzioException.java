package de.firecreeper82.quizzio.exception;

import org.springframework.http.HttpStatus;

public class QuizzioException extends Exception{

    private final String message;
    private final HttpStatus statusCode;

    public QuizzioException(String message, HttpStatus statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }
}
