package br.com.bthirtyeight;

import jakarta.persistence.Embeddable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class Startup {

	public static void main(String[] args) {
		SpringApplication.run(Startup.class, args);

//        generatedHashedPassword();
	}

    private static void generatedHashedPassword() {

        PasswordEncoder pbkdf2Encoder = new Pbkdf2PasswordEncoder(
                "", 8, 185000,// vazio para gerar automaticamente  / comprimento da chave /numero de vezes que o algoritimo vai ser aplicado
                Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256//algoritimo de hash(criptografia)
        );

        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("pbkdf2", pbkdf2Encoder);

        //nome do algoritmo q a gente vai utilizar / hash de algoritimo q a gente vai usar
        DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2",encoders);

        passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2Encoder);

        var pass1 = passwordEncoder.encode("admin123");
        var pass2 = passwordEncoder.encode("admin321");

        System.out.println(pass1);
        System.out.println(pass2);

    }
}
