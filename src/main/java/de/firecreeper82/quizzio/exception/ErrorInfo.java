package de.firecreeper82.quizzio.exception;

import org.springframework.http.HttpStatus;

public record ErrorInfo(String message, HttpStatus code) {
}
