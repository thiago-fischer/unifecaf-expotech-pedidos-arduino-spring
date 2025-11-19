package br.com.fecaf.arduino.exception;

import br.com.fecaf.arduino.model.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGeneric(Exception ex) {
        ExceptionResponse body = new ExceptionResponse(
                new Date(),
                "Internal Server Error.",
                ex.getMessage()
        );
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(GrafoErrorException.class)
    public ResponseEntity<ExceptionResponse> handleGrafoException(GrafoErrorException ex) {
        ExceptionResponse body = new ExceptionResponse(
                new Date(),
                "Invalid input for the route optimizer.",
                ex.getMessage()
        );
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
