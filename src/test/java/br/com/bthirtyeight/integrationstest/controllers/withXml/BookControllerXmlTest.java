package br.com.bthirtyeight.integrationstest.controllers.withXml;

import br.com.bthirtyeight.config.TestConfig;
import br.com.bthirtyeight.integrationstest.dto.BookDTO;
import br.com.bthirtyeight.integrationstest.dto.PersonDTO;
import br.com.bthirtyeight.integrationstest.dto.wrappers.json.WrapperBookDTO;
import br.com.bthirtyeight.integrationstest.dto.wrappers.xml.PagedModelBook;
import br.com.bthirtyeight.integrationstest.testcontainers.AbstractIntegrationTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.restassured.RestAssured.*;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.testcontainers.shaded.org.bouncycastle.asn1.dvcs.Data;

import java.util.Date;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookControllerXmlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static XmlMapper objectMapper;

    private static BookDTO book;


    @BeforeAll
    static void setUp() {
        objectMapper = new XmlMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        book = new BookDTO();
    }

    @Test
    @Order(1)
    void createdTest() throws JsonProcessingException {
        mockBook();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_ERUDIO)
                .setBasePath("/api/book/v1")
                .setPort(TestConfig.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .body(book)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        BookDTO createdBook = objectMapper.readValue(content, BookDTO.class);
        book = createdBook;

        assertNotNull(createdBook.getId());
        assertNotNull(createdBook.getAuthor());
        assertNotNull(createdBook.getLaunchDate());
        assertNotNull(createdBook.getPrice());
        assertNotNull(createdBook.getTitle());

        assertTrue(createdBook.getId() > 0);

        assertEquals("J. K. Rowling",createdBook.getAuthor());
        assertEquals("Harry Potter",createdBook.getTitle());
        assertEquals(70.0, createdBook.getPrice());
    }

    @Test
    @Order(3)
    void findByIdTest() throws JsonProcessingException {

        var content =
                given(specification)
                        .contentType(MediaType.APPLICATION_XML_VALUE)
                        .accept(MediaType.APPLICATION_XML_VALUE)
                        .pathParam("id",book.getId())
                        .when()
                        .get("{id}")
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .asString();

        BookDTO createdBook = objectMapper.readValue(content, BookDTO.class);
        book = createdBook;

        assertNotNull(createdBook.getId());
        assertNotNull(createdBook.getAuthor());
        assertNotNull(createdBook.getLaunchDate());
        assertNotNull(createdBook.getPrice());
        assertNotNull(createdBook.getTitle());

        assertTrue(createdBook.getId() > 0);

        assertEquals("J. K. Rowling",createdBook.getAuthor());
        assertEquals("Animais Fantásticos e Onde Habitam",createdBook.getTitle());
        assertEquals(70.0, createdBook.getPrice());


    }

    @Test
    @Order(2)
    void updateTest() throws JsonProcessingException {
        book.setTitle("Animais Fantásticos e Onde Habitam");

        var content =
                given(specification)
                        .contentType(MediaType.APPLICATION_XML_VALUE)
                        .accept(MediaType.APPLICATION_XML_VALUE)
                        .body(book)
                        .when()
                        .put()
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .asString();

        BookDTO createdBook = objectMapper.readValue(content, BookDTO.class);
        book = createdBook;

        assertNotNull(createdBook.getId());
        assertNotNull(createdBook.getAuthor());
        assertNotNull(createdBook.getLaunchDate());
        assertNotNull(createdBook.getPrice());
        assertNotNull(createdBook.getTitle());

        assertTrue(createdBook.getId() > 0);

        assertEquals("J. K. Rowling",createdBook.getAuthor());
        assertEquals("Animais Fantásticos e Onde Habitam",createdBook.getTitle());
        assertEquals(70.0, createdBook.getPrice());
    }

    @Test
    @Order(4)
    void deleteTest() {
        given(specification)
                .pathParam("id",book.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(5)
    void findAllTest() throws JsonProcessingException {

        var content =
                given(specification)
                        .accept(MediaType.APPLICATION_XML_VALUE)
                        .queryParam("page", 0, "size",12,"direction","asc")
                        .when()
                        .get()
                        .then()
                        .statusCode(200)
                        .contentType(MediaType.APPLICATION_XML_VALUE)
                        .extract()
                        .body()
                        .asString();

        PagedModelBook wrapper = objectMapper.readValue(content, PagedModelBook.class);
        List<BookDTO> books = wrapper.getContent();

        BookDTO bookOne = books.get(0);

        assertNotNull( bookOne.getId());
        assertNotNull( bookOne.getAuthor());
        assertNotNull( bookOne.getLaunchDate());
        assertNotNull( bookOne.getPrice());
        assertNotNull( bookOne.getTitle());

        assertTrue( bookOne.getId() > 0);

        assertEquals("Viktor Mayer-Schonberger e Kenneth Kukier", bookOne.getAuthor());
        assertEquals("Big Data: como extrair volume, variedade, velocidade e valor da avalanche de informação cotidiana", bookOne.getTitle());
        assertEquals(54.0,  bookOne.getPrice());


        BookDTO bookFour = books.get(4);

        assertNotNull( bookFour.getId());
        assertNotNull( bookFour.getAuthor());
        assertNotNull( bookFour.getLaunchDate());
        assertNotNull( bookFour.getPrice());
        assertNotNull( bookFour.getTitle());

        assertTrue( bookFour.getId() > 0);

        assertEquals("Eric Evans", bookFour.getAuthor());
        assertEquals("Domain Driven Design", bookFour.getTitle());
        assertEquals(92.0,  bookFour.getPrice());
    }

    private void mockBook() {
        book.setAuthor("J. K. Rowling");
        book.setTitle("Harry Potter");
        book.setLaunchDate(new Date());
        book.setPrice(70.0);
    }
}

