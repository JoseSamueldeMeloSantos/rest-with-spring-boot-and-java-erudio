package br.com.bthirtyeight.data.dto.request;

import java.util.Objects;

// Classe DTO (Data Transfer Object) usada para transportar os dados da requisição de envio de e-mail.
// "Request" indica que os dados vêm do corpo da requisição HTTP.
// Esse tipo de DTO é usado apenas para receber informações — ele chega ao controller, mas não é retornado na resposta.
// Está no pacote "data.dto.request" para manter a organização entre objetos de entrada (request) e saída (response).
public class EmailRequestDTO {

    private String to;
    private String subject;
    private String body;

    public EmailRequestDTO(String to, String subject, String body) {
        this.to = to;
        this.subject = subject;
        this.body = body;
    }

    public EmailRequestDTO() {
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        EmailRequestDTO that = (EmailRequestDTO) o;
        return Objects.equals(to, that.to) && Objects.equals(subject, that.subject) && Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(to, subject, body);
    }
}
