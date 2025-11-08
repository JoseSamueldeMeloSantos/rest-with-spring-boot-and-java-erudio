package br.com.bthirtyeight.controllers;

import br.com.bthirtyeight.data.dto.security.AccountCredentialsDTO;
import br.com.bthirtyeight.data.dto.security.TokenDTO;
import br.com.bthirtyeight.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication Endpoint")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService service;

    @Operation(summary = "Authenticates an User and Return a Token")
    @PostMapping("/signin")
    public ResponseEntity<?> signin(AccountCredentialsDTO credentials) {
        if (credentialsIsInvalid(credentials)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid Client Request!");
        }

        var token = service.signIn(credentials).getBody();

        if (token == null) ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid Client Request!");

        return ResponseEntity.ok().body(token);
    }

    private static boolean credentialsIsInvalid(AccountCredentialsDTO credentials) {
        return credentials == null ||
                StringUtils.isBlank(credentials.getPassword()) ||
                StringUtils.isBlank(credentials.getUsername());
    }
}
