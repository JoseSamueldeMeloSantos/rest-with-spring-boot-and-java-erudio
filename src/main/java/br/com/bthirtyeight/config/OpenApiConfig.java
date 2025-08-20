package br.com.bthirtyeight.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration//-> diz para o spring carregar essa classe quando tive inicializando o contexto
public class OpenApiConfig {

    @Bean//Cada método marcado com @Bean cria uma instância do objeto e o Spring guarda no container e a partir dai eu posso ussar quando quiser
    OpenAPI customOpemAPI () {
        return new OpenAPI()
                .info(new Info()//instancia as config de informacao do swwager
                        .title("REST API's RESTful from 0 with Java, Spring Boot, Kubernetes and Docker")//define o nome
                        .version("v1")
                        .description("REST API's RESTful from 0 with Java, Spring Boot, Kubernetes and Docker")
                        .termsOfService("")//declara os termos de servico
                        .license(new License()//configurando a licenca
                                .name("Apache 2.0")
                                .url("")//url da licenca
                        )
                );
    }
}
