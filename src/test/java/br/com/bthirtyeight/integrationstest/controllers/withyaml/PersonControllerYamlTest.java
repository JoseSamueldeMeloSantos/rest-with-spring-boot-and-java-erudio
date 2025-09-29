package br.com.bthirtyeight.integrationstest.controllers.withyaml;


import br.com.bthirtyeight.config.TestConfig;
import br.com.bthirtyeight.integrationstest.controllers.withyaml.mapper.YamlMapper;
import br.com.bthirtyeight.integrationstest.dto.PersonDTO;
import br.com.bthirtyeight.integrationstest.dto.wrappers.xml.PagedModelPerson;
import br.com.bthirtyeight.integrationstest.testcontainers.AbstractIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)//precisameos usar pq vamos executar operações do tipo crud(para armazenar o estado do obj entre os difentes testes)
class PersonControllerYamlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;//a specification sera passada de um teste para outro pois ela é static
    private static YamlMapper objectMapper;

    private static PersonDTO person;

    @BeforeAll
    static void setUp() {
        objectMapper = new YamlMapper();
        person = new PersonDTO();
    }

    @Test
    @Order(1)//define a ordem de excucao entre os testes
    void createTest() throws JsonProcessingException {
        mockPerson();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_ERUDIO)
                .setBasePath("/person/v1")
                .setPort(TestConfig.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var createdPerson =
                given().config(RestAssuredConfig.config()
                                .encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
                )
                        .spec(specification)
                        .contentType(MediaType.APPLICATION_YAML_VALUE)
                        .accept(MediaType.APPLICATION_YAML_VALUE)
                        .body(person, objectMapper)//necessario passar o mapper no body
                        .when()
                        .post()
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .as(PersonDTO.class, objectMapper);


        person = createdPerson;


        assertNotNull(createdPerson.getId());
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getAddress());
        assertNotNull(createdPerson.getGender());

        assertTrue(createdPerson.getId() > 0);

        assertEquals("Linus", createdPerson.getFirstName());
        assertEquals("Torvalds", createdPerson.getLastName());
        assertEquals("Helsinki - Finland", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertTrue(person.getEnabled());
    }

    @Test
    @Order(2)
    void findByIdTest() throws JsonProcessingException {
        mockPerson();

        var createdPerson = given().config(RestAssuredConfig.config()
                        .encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
                )
                .spec(specification)//Esse código cria uma requisição RestAssured onde qualquer corpo enviado com yaml sera tratado como texto puro
                        .contentType(MediaType.APPLICATION_YAML_VALUE)
                        .accept(MediaType.APPLICATION_YAML_VALUE)
                        .pathParam("id", person.getId())//pega do getMapping no controller(pode ser qualquer nome caso que esteja la tbm
                        .when()
                        .get("{id}")
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .as(PersonDTO.class, objectMapper);//ja para obj


        person = createdPerson;

        assertNotNull(createdPerson.getId());
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getAddress());
        assertNotNull(createdPerson.getGender());

        assertTrue(createdPerson.getId() > 0);

        assertEquals("Linus", createdPerson.getFirstName());
        assertEquals("Torvalds", createdPerson.getLastName());
        assertEquals("Helsinki - Finland", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());
    }

    @Test
    @Order(4)//define a ordem de excucao entre os testes
    void updateTest() throws JsonProcessingException {
        person.setLastName("Benedict Torvalds");

        var createdPerson = given().config(RestAssuredConfig.config()
                        .encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
                )
                .spec(specification)
                        .contentType(MediaType.APPLICATION_YAML_VALUE)
                        .accept(MediaType.APPLICATION_YAML_VALUE)
                        .body(person, objectMapper)
                        .when()
                        .put()
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .as(PersonDTO.class, objectMapper);


        person = createdPerson;


        assertNotNull(createdPerson.getId());
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getAddress());
        assertNotNull(createdPerson.getGender());

        assertTrue(createdPerson.getId() > 0);

        assertEquals("Linus", createdPerson.getFirstName());
        assertEquals("Benedict Torvalds", createdPerson.getLastName());
        assertEquals("Helsinki - Finland", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertTrue(!person.getEnabled());
    }

    @Test
    @Order(3)//define a ordem de excucao entre os testes
    void disableTest() throws JsonProcessingException {

        var createdPerson = given().config(RestAssuredConfig.config()
                        .encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
                )
                .spec(specification)
                        .contentType(MediaType.APPLICATION_YAML_VALUE)
                        .accept(MediaType.APPLICATION_YAML_VALUE)
                        .pathParam("id",person.getId())
                        .body(person, objectMapper)
                        .when()
                        .patch("{id}")
                        .then()
                        .statusCode(200)
                        .contentType(MediaType.APPLICATION_YAML_VALUE)//para verificar se retorna um json
                        .extract()
                        .body()
                        .as(PersonDTO.class, objectMapper);


        person = createdPerson;


        assertNotNull(createdPerson.getId());
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getAddress());
        assertNotNull(createdPerson.getGender());

        assertTrue(createdPerson.getId() > 0);

        assertEquals("Linus", createdPerson.getFirstName());
        assertEquals("Torvalds", createdPerson.getLastName());
        assertEquals("Helsinki - Finland", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertFalse(person.getEnabled());
    }

    @Test
    @Order(5)//define a ordem de excucao entre os testes
    void deleteTest() throws JsonProcessingException {
        given(specification)
                //.contentType(MediaType.APPLICATION_JSON_VALUE)//não precissa de pois esse não recebe e nem retorna estrutura
                .pathParam("id", person.getId())//pega do getMapping no controller(pode ser qualquer nome caso que esteja la tbm
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);


    }

    @Test
    @Order(6)
    void findAllTest() throws JsonProcessingException {
        mockPerson();

        var response =
                given(specification)
                        .accept(MediaType.APPLICATION_YAML_VALUE)
                        .queryParam("page", 3,"size",12,"direction","asc")
                        .when()
                        .get()
                        .then()
                        .statusCode(200)
                        .contentType(MediaType.APPLICATION_YAML_VALUE)//mediaType de retorno sera json
                        .extract()
                        .body()
                        .as(PagedModelPerson.class, objectMapper);;

        //WrapperPersonDTO wrapper = objectMapper.readValue(content,WrapperPersonDTO.class);
        List<PersonDTO> people = response.getContent();

        //fazemos isso para nao ter problema com o restassure(converter obj -> str -> obj
        PersonDTO personOne = people.get(0);

        assertNotNull(personOne.getId());
        assertTrue(personOne.getId() > 0);

        assertEquals("Allin", personOne.getFirstName());
        assertEquals("Otridge", personOne.getLastName());
        assertEquals("09846 Independence Center", personOne.getAddress());
        assertEquals("Male", personOne.getGender());
        assertFalse(personOne.getEnabled());

        PersonDTO personFour = people.get(4);

        assertNotNull(personFour.getId());
        assertTrue(personFour.getId() > 0);

        assertEquals("Alonso", personFour.getFirstName());
        assertEquals("Luchelli", personFour.getLastName());
        assertEquals("9 Doe Crossing Avenue", personFour.getAddress());
        assertEquals("Male", personFour.getGender());
        assertFalse(personFour.getEnabled());

    }



    private void mockPerson() {
        person.setFirstName("Linus");
        person.setLastName("Torvalds");
        person.setAddress("Helsinki - Finland");
        person.setGender("Male");
        person.setEnabled(true);
    }
}