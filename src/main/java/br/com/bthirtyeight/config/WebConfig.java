package br.com.bthirtyeight.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration//diz para o spring que essa classe contem aconfiguracoes e ela pode ter inicialização de beans
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {

        //Via EXTENSION. _.xml or _.JSON deprecated on spring boot 2.6
        // http://localhost:8080/person/v1/1.xml  http://localhost:8080/person/v1/1.json

        //Use Query Param http://localhost:8080/person/v1/1?mediaType=xml
        configurer.favorParameter(true)
                .parameterName("mediaType")//define o nome do parametro
                .ignoreAcceptHeader(true)//ignora os headers accept
                .useRegisteredExtensionsOnly(false)
                .defaultContentType(MediaType.APPLICATION_JSON)
                .mediaType("json", MediaType.APPLICATION_JSON)
                .mediaType("xml", MediaType.APPLICATION_XML);

        //agora e so adicionar os novos media types no comsumer e producer
    }
}
