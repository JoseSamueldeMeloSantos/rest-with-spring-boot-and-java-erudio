package br.com.bthirtyeight.integrationstest.controllers.withXml;


import br.com.bthirtyeight.config.TestConfig;
import br.com.bthirtyeight.integrationstest.dto.PersonDTO;
import br.com.bthirtyeight.integrationstest.dto.wrappers.json.WrapperPersonDTO;
import br.com.bthirtyeight.integrationstest.dto.wrappers.xml.PagedModelPerson;
import br.com.bthirtyeight.integrationstest.testcontainers.AbstractIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)//precisameos usar pq vamos executar operações do tipo crud(para armazenar o estado do obj entre os difentes testes)
class PersonControllerXmlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;//a specification sera passada de um teste para outro pois ela é static
    private static XmlMapper objectMapper;

    private static PersonDTO person;

    @BeforeAll
    static void setUp() {
        objectMapper = new XmlMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);//para ignorar parametros nao registrados no json

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

        var content =
                given(specification)
                        .contentType(MediaType.APPLICATION_XML_VALUE)
                        .accept(MediaType.APPLICATION_XML_VALUE)
                        .body(person)
                        .when()
                        .post()
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .asString();

        //fazemos isso para nao ter problema com o restassure(converter obj -> str -> obj
        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
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

        var content =
                given(specification)
                        .contentType(MediaType.APPLICATION_XML_VALUE)
                        .accept(MediaType.APPLICATION_XML_VALUE)
                        .pathParam("id", person.getId())//pega do getMapping no controller(pode ser qualquer nome caso que esteja la tbm
                        .when()
                        .get("{id}")
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .asString();

        //fazemos isso para nao ter problema com o restassure(converter obj -> str -> obj
        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
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

        var content =
                given(specification)
                        .contentType(MediaType.APPLICATION_XML_VALUE)
                        .accept(MediaType.APPLICATION_XML_VALUE)
                        .body(person)
                        .when()
                        .put()
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .asString();

        //fazemos isso para nao ter problema com o restassure(converter obj -> str -> obj
        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
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

        var content =
                given(specification)
                        .contentType(MediaType.APPLICATION_XML_VALUE)
                        .accept(MediaType.APPLICATION_XML_VALUE)
                        .pathParam("id",person.getId())
                        .body(person)
                        .when()
                        .patch("{id}")
                        .then()
                        .statusCode(200)
                        .contentType(MediaType.APPLICATION_XML_VALUE)//para verificar se retorna um json
                        .extract()
                        .body()
                        .asString();

        //fazemos isso para nao ter problema com o restassure(converter obj -> str -> obj
        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
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

        var content =
                given(specification)
                        .accept(MediaType.APPLICATION_XML_VALUE)
                        .queryParam("page", 3,"size",12,"direction","asc")
                        .when()
                        .get()
                        .then()
                        .statusCode(200)
                        .contentType(MediaType.APPLICATION_XML_VALUE)//mediaType de retorno sera json
                        .extract()
                        .body()
                        .asString();

        PagedModelPerson wrapper = objectMapper.readValue(content, PagedModelPerson.class);
        List<PersonDTO> people = wrapper.getContent();

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