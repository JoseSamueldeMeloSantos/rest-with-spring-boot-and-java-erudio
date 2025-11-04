package br.com.bthirtyeight.services;

import br.com.bthirtyeight.config.EmailConfig;
import br.com.bthirtyeight.data.dto.request.EmailRequestDTO;
import br.com.bthirtyeight.mail.EmailSender;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class EmailService {

    @Autowired
    private EmailSender sender;
    @Autowired
    private EmailConfig config;

    public  void sendSimpleEmail(EmailRequestDTO dto) {

        sender.to(dto.getTo())
                .withSubject(dto.getSubject())
                .withMessage(dto.getBody())
                .send(config);
    }

    //precissamos desserializar(transformar um dado em texto (como JSON, XML, etc.) de volta em um objeto Java.) o email com anexo
    public void sendEmailWithAttachment(String emailRequestJson, MultipartFile attachment) {

        File tempFile = null;
        try {
            EmailRequestDTO emailRequest = new ObjectMapper().readValue(emailRequestJson, EmailRequestDTO.class);//convertendo json em obj
            tempFile = File.createTempFile("attachement", attachment.getOriginalFilename());//Cria um arquivo temporário no disco
            attachment.transferTo(tempFile);//Copia o conteúdo do arquivo recebido (do tipo MultipartFile, vindo da requisição HTTP) para o arquivo temporário recém-criado.

            //envia o email
            sender.to(emailRequest.getTo())
                    .withSubject(emailRequest.getSubject())
                    .withMessage(emailRequest.getBody())
                    .attach(tempFile.getAbsolutePath())//anexa o arquivo
                    .send(config);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("error parsing email request JSON", e);
        } catch (IOException e) {
            throw new RuntimeException("Error processing the attachment", e);
        } finally {
            if (tempFile != null && tempFile.exists())
                tempFile.delete();
        }
    }
}
