package br.com.bthirtyeight.exception;

import java.util.Date;

/*
O response serve para:
Informar se a requisição foi bem-sucedida ou não (status HTTP).
Entregar os dados solicitados (JSON, texto, arquivo, etc.).
Fornecer metadados (cabeçalhos, cookies, tipo de conteúdo, etc.).
 */

public record ExceptionResponse(Date timestamp, String message, String details) {}
