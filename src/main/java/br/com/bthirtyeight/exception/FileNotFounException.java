package br.com.bthirtyeight.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FileNotFounException extends RuntimeException{

    public FileNotFounException(String message) {
        super(message);
    }

    public FileNotFounException(String message, Throwable cause) {
        super(message, cause);
    }
}
