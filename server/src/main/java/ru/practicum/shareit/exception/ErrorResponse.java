package ru.practicum.shareit.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorResponse {
    String error;
    HttpStatus status;

    public ErrorResponse(String error, HttpStatus status) {
        this.error = error;
        this.status = status;
    }

}
