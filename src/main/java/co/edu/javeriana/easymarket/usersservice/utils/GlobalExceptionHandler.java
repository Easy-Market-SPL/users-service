package co.edu.javeriana.easymarket.usersservice.utils;

import co.edu.javeriana.easymarket.usersservice.dtos.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(OperationException.class)
    public ResponseEntity<Response> handleOperationException(OperationException e) {
        return ResponseEntity.status(e.getCode()).body(new Response(e.getCode(), e.getMessage()));
    }
}
