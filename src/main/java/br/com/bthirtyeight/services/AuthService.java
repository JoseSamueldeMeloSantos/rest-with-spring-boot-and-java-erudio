package br.com.bthirtyeight.services;

import br.com.bthirtyeight.data.dto.security.AccountCredentialsDTO;
import br.com.bthirtyeight.data.dto.security.TokenDTO;
import br.com.bthirtyeight.exception.RequiredObjectIsNullException;
import br.com.bthirtyeight.mapper.ObjectMapper;
import br.com.bthirtyeight.model.User;
import br.com.bthirtyeight.repository.UserRepository;
import br.com.bthirtyeight.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository repository;


    public ResponseEntity<TokenDTO> signIn(AccountCredentialsDTO credentials) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        credentials.getUsername(),
                        credentials.getPassword()
                )
        );

        var user = repository.findByUsername(credentials.getUsername());

        if (user == null) {
            throw new UsernameNotFoundException("Username " + credentials.getUsername() + "not found!");
        }

        var tokenReponse = tokenProvider.createAcessToken(
                credentials.getUsername(),
                user.getRoles()
        );

        return ResponseEntity.ok(tokenReponse);
    }

    public ResponseEntity<TokenDTO> refreshToken(String username, String refreshToken) {
        var user = repository.findByUsername(username);
        TokenDTO token;

        if (user != null) {
            token = tokenProvider.refreshToken(refreshToken);
        } else {
            throw  new UsernameNotFoundException("Username " + username + " not found!");
        }

        return  ResponseEntity.ok(token);

    }

    public AccountCredentialsDTO create(AccountCredentialsDTO user) {
        if (user == null) throw new RequiredObjectIsNullException();

        var entity = new User();

        entity.setFullName(user.getFullname());
        entity.setUserName(user.getUsername());
        entity.setPassword(generatedHashedPassword(user.getPassword()));
        entity.setAccountNonExpired(true);
        entity.setAccountNonLocked(true);
        entity.setCredentialsNonExpired(true);
        entity.setEnabled(true);

        var dto = repository.save(entity);
        return new AccountCredentialsDTO(dto.getUserName(),dto.getPassword(),dto.getFullName());
    }


    private String generatedHashedPassword(String password) {

        PasswordEncoder pbkdf2Encoder = new Pbkdf2PasswordEncoder(
                "", 8, 185000,// vazio para gerar automaticamente  / comprimento da chave /numero de vezes que o algoritimo vai ser aplicado
                Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256//algoritimo de hash(criptografia)
        );

        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("pbkdf2", pbkdf2Encoder);

        //nome do algoritmo q a gente vai utilizar / hash de algoritimo q a gente vai usar
        DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2",encoders);

        passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2Encoder);

        return passwordEncoder.encode(password);
    }
}
