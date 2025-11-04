package br.com.bthirtyeight.services;

import br.com.bthirtyeight.config.EmailConfig;
import br.com.bthirtyeight.data.dto.request.EmailRequestDTO;
import br.com.bthirtyeight.mail.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
