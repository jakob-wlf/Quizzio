package de.firecreeper82.quizzio.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorInfo> handleErrors(Throwable exception) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);

        System.out.println(exception.getMessage());
        System.out.println(sw);

        String message = exception.getMessage() == null ? "An error occurred" : exception.getMessage();
        HttpStatus code = exception instanceof QuizzioException quizzioException ? quizzioException.getStatusCode() : HttpStatus.INTERNAL_SERVER_ERROR;


        return new ResponseEntity<>(new ErrorInfo(message, code), code);

    }

}
