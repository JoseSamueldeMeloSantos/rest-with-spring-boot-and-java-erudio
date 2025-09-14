package br.com.bthirtyeight.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration//diz para o spring que essa classe contem aconfiguracoes e ela pode ter inicialização de beans
public class WebConfig implements WebMvcConfigurer {

    private static final Logger log = LoggerFactory.getLogger(WebConfig.class);

    //                           :valor ->para definir um valor padrao caso nao esteja no yml
    @Value("${cors.originPatterns}")//o @value puxa um certo atributo do yml
    private String crosOriginPatterns = "";

    //metodo para configuracao do cors universal
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        var allowedOrigins = crosOriginPatterns.split(",");
        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods("*")
                //.allowedMethods("GET","POST","PUT","PATCH","DELETE","OPTIONS")
                .allowCredentials(true);
    }

    //configuração de mediatype
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {

        //Via EXTENSION. _.xml or _.JSON deprecated on spring boot 2.6
        // http://localhost:8080/person/v1/1.xml  http://localhost:8080/person/v1/1.json

        //Use Query Param http://localhost:8080/person/v1/1?mediaType=xml
        /**
        configurer.favorParameter(true)
                .parameterName("mediaType")//define o nome do parametro
                .ignoreAcceptHeader(true)//ignora os headers accept
                .useRegisteredExtensionsOnly(false)
                .defaultContentType(MediaType.APPLICATION_JSON)
                .mediaType("json", MediaType.APPLICATION_JSON)
                .mediaType("xml", MediaType.APPLICATION_XML);

        //agora e so adicionar os novos media types no comsumer e producer
         */


        //Via Header Param
        //nao tem o parametro,pois nao e um query param
        configurer.favorParameter(false)
                .ignoreAcceptHeader(false)//tem que ta falso,pois a requisicao e pero header
                .useRegisteredExtensionsOnly(false)
                .defaultContentType(MediaType.APPLICATION_JSON)
                .mediaType("json", MediaType.APPLICATION_JSON)
                .mediaType("xml", MediaType.APPLICATION_XML)
                .mediaType("yaml", MediaType.APPLICATION_YAML);

    }
}
