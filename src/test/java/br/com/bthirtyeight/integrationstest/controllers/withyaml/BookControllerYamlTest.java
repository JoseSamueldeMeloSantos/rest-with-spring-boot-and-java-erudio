package br.com.bthirtyeight.integrationstest.controllers.withyaml;

import br.com.bthirtyeight.config.TestConfig;
import br.com.bthirtyeight.integrationstest.controllers.withyaml.mapper.YamlMapper;
import br.com.bthirtyeight.integrationstest.dto.BookDTO;
import br.com.bthirtyeight.integrationstest.dto.wrappers.xml.PagedModelBook;
import br.com.bthirtyeight.integrationstest.testcontainers.AbstractIntegrationTest;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.Date;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookControllerYamlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static YamlMapper objectMapper;

    private static BookDTO book;

    @BeforeAll
    static void setUp() {
        objectMapper = new YamlMapper();
        book = new BookDTO();
    }

    @Test
    @Order(1)
    void createTest() {
        mockBook();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_ERUDIO)
                .setBasePath("/api/book/v1")
                .setPort(TestConfig.SERVER_PORT)
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var createdBook =
                given().config(RestAssuredConfig.config()
                                .encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT)))
                        .spec(specification)
                        .contentType(MediaType.APPLICATION_YAML_VALUE)
                        .body(book, objectMapper)
                        .when()
                        .post()
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .as(BookDTO.class, objectMapper);

        book = createdBook;

        assertNotNull(createdBook.getId());
        assertNotNull(createdBook.getAuthor());
        assertNotNull(createdBook.getLaunchDate());
        assertNotNull(createdBook.getPrice());
        assertNotNull(createdBook.getTitle());

        assertTrue(createdBook.getId() > 0);

        assertEquals("J. K. Rowling", createdBook.getAuthor());
        assertEquals("Harry Potter", createdBook.getTitle());
        assertEquals(70.0, createdBook.getPrice());

    }

    @Test
    @Order(2)
    void findByIdTest() {
        mockBook();

        var createdBook = given().config(RestAssuredConfig.config()
                        .encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT)))
                .spec(specification)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .pathParam("id", book.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(BookDTO.class, objectMapper);

        book = createdBook;

        assertNotNull(createdBook.getId());
        assertNotNull(createdBook.getAuthor());
        assertNotNull(createdBook.getLaunchDate());
        assertNotNull(createdBook.getPrice());
        assertNotNull(createdBook.getTitle());

        assertTrue(createdBook.getId() > 0);

        assertEquals("J. K. Rowling", createdBook.getAuthor());
        assertEquals("Harry Potter", createdBook.getTitle());
        assertEquals(70.0, createdBook.getPrice());
    }

    @Test
    @Order(4)
    void updateTest() {
        book.setTitle("Animais Fantásticos e Onde Habitam");

        var createdBook = given().config(RestAssuredConfig.config()
                        .encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT)))
                .spec(specification)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .body(book, objectMapper)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(BookDTO.class, objectMapper);


        book = createdBook;

        assertNotNull(createdBook.getId());
        assertNotNull(createdBook.getAuthor());
        assertNotNull(createdBook.getLaunchDate());
        assertNotNull(createdBook.getPrice());
        assertNotNull(createdBook.getTitle());

        assertTrue(createdBook.getId() > 0);

        assertEquals("J. K. Rowling", createdBook.getAuthor());
        assertEquals("Animais Fantásticos e Onde Habitam", createdBook.getTitle());
        assertEquals(70.0, createdBook.getPrice());
    }

    @Test
    @Order(5)
    void deleTest() {
        given(specification)
                .pathParam("id", book.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }


    @Test
    @Order(6)
    void findAllTest() {
        mockBook();

        var response =
                given(specification)
                        .accept(MediaType.APPLICATION_YAML_VALUE)
                        .queryParam("page", 0, "size", 12, "direction", "asc")
                        .when()
                        .get()
                        .then()
                        .statusCode(200)
                        .contentType(MediaType.APPLICATION_YAML_VALUE)
                        .extract()
                        .body()
                        .as(PagedModelBook.class, objectMapper);

        List<BookDTO> books = response.getContent();

        BookDTO bookOne = books.get(0);

        assertNotNull(bookOne.getId());
        assertNotNull(bookOne.getAuthor());
        assertNotNull(bookOne.getLaunchDate());
        assertNotNull(bookOne.getPrice());
        assertNotNull(bookOne.getTitle());

        assertTrue(bookOne.getId() > 0);

        assertEquals("Viktor Mayer-Schonberger e Kenneth Kukier", bookOne.getAuthor());
        assertEquals("Big Data: como extrair volume, variedade, velocidade e valor da avalanche de informação cotidiana", bookOne.getTitle());
        assertEquals(54.0, bookOne.getPrice());


        BookDTO bookFour = books.get(4);

        assertNotNull(bookFour.getId());
        assertNotNull(bookFour.getAuthor());
        assertNotNull(bookFour.getLaunchDate());
        assertNotNull(bookFour.getPrice());
        assertNotNull(bookFour.getTitle());

        assertTrue(bookFour.getId() > 0);

        assertEquals("Eric Evans", bookFour.getAuthor());
        assertEquals("Domain Driven Design", bookFour.getTitle());
        assertEquals(92.0, bookFour.getPrice());
    }


    private void mockBook() {
        book.setAuthor("J. K. Rowling");
        book.setTitle("Harry Potter");
        book.setLaunchDate(new Date());
        book.setPrice(70.0);
    }
}
