package br.com.bthirtyeight.controllers;

import br.com.bthirtyeight.controllers.docs.EmailControllerDocs;
import br.com.bthirtyeight.data.dto.request.EmailRequestDTO;
import br.com.bthirtyeight.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/email/v1")
public class EmailController implements EmailControllerDocs {

    @Autowired
    private EmailService service;


    //para email sem anexo
    @Override
    @PostMapping
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequestDTO emailRequestDTO) {
        service.sendSimpleEmail(emailRequestDTO);
        return new ResponseEntity<>("e-Mail sent with success", HttpStatus.OK);
    }

    @Override
    @PostMapping("/withAttachment")
    public ResponseEntity<String> sendEmail(
            @RequestParam("emailRequest") String emailRequestJson,
            @RequestParam("attachment") MultipartFile multipartFile) {
        service.sendEmailWithAttachment(emailRequestJson, multipartFile);
        return new ResponseEntity<>("e-Mail with attachment sent successfully!",HttpStatus.OK);
    }
}
