package br.com.bthirtyeight.exception.hadler;

//handler -> interceptiva a excecao e vai substituir pelo json que o record vai gerar
//           e pelo status code do UnsupportedMath...(tipo um controller das exceptions)


import br.com.bthirtyeight.exception.ExceptionResponse;
import br.com.bthirtyeight.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

//controllerAdivice -> tratamento global(caso niguem fornceca um tratamento mais adequado)
@ControllerAdvice//centralizar o tratamento de exceções para todos os controllers da aplicação(sem ele o tratamento seria espalhado nos Controllers)
@RestController
public class CustomEntityResponseHandler extends ResponseEntityExceptionHandler {//sempre extend dessa clase

    //ResponseEntity -> uma classe que representa uma resposta HTTP completa
    //WebRequest -> é uma interface que representa uma requisição web genérica
    @ExceptionHandler(Exception.class)//define qual tipo de erro esse metodo vai tratar
    public final ResponseEntity<ExceptionResponse> handleAllExceptions(Exception ex, WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(
                new Date(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)//define qual tipo de erro esse metodo vai tratar
    public final ResponseEntity<ExceptionResponse> handleNotFoundExceptions(Exception ex, WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(
                new Date(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


}
