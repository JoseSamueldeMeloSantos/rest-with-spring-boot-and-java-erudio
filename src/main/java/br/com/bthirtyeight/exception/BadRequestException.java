package br.com.bthirtyeight.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)//defini o código de status HTTP que será retornado  quando uma exceção for lançada(do response).
public class BadRequestException extends RuntimeException {

    public BadRequestException() {
        super("Unsupported file extension");
    }
}
