package br.com.bthirtyeight.mail;

import br.com.bthirtyeight.config.EmailConfig;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.StringTokenizer;

@Component
public class EmailSender implements Serializable {

    Logger logger = LoggerFactory.getLogger(EmailSender.class);

    private final JavaMailSender mailSender;//para garantir q sua referencia nao seja alterada
    private String to;
    private String subject;
    private String body;//menssagem
    private ArrayList<InternetAddress> recipients = new ArrayList<>();//emails
    private File attachment;//caso tenha um anexo de algum arquivo

    public EmailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public EmailSender to(String to) {
        this.to = to;
        this.recipients = getRecipients(to);
        return this;
    }

    public EmailSender withSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public EmailSender withMessage(String body) {
        this.body = body;
        return this;
    }

    public EmailSender attach(String fileDir) {
        this.attachment = new File(fileDir);
        return this;
    }

    public void send(EmailConfig config) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);//multipart é para permitir envio de arquivo(anexo)
            helper.setFrom(config.getUsername());
            helper.setTo(recipients.toArray(new InternetAddress[0]));
            helper.setSubject(subject);
            helper.setText(body, true);// true -> pra dizer q o tex e do tipo html

            //adiciona o anexo
            if (attachment != null) {
                helper.addAttachment(attachment.getName(), attachment);
            }

            mailSender.send(message);
            logger.info("Email sent to  %s with the subject %s %n");

            //resetando dados para nao reenviar o mesmo email
            reset();
        } catch (MessagingException e) {
            throw new RuntimeException("Error sending the email", e);
        }

    }

    private void reset() {
        this.to = null;
        this.subject = null;
        this.body = null;
        this.recipients = null;
        this.attachment = null;
    }

    //email@gmail.com email@gmail.com email@gmail.com
    public ArrayList<InternetAddress> getRecipients(String to) {
        String toWithoutSpaces = to.trim();
        //agr vamos tokenizar a variavel e separar onde tem ;
        StringTokenizer tok = new StringTokenizer(toWithoutSpaces, ";");
        ArrayList<InternetAddress> recipientsList = new ArrayList<>();

        while (tok.hasMoreElements()) {
            try {
                //convertendo a string em um InternetAddress
                recipientsList.add(new InternetAddress(tok.nextElement().toString()));
            } catch (AddressException e) {
                throw new RuntimeException(e);
            }
        }
        return recipientsList;
    }
}
