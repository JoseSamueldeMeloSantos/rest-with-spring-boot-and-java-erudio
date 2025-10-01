package br.com.bthirtyeight.repository;

import br.com.bthirtyeight.integrationstest.testcontainers.AbstractIntegrationTest;
import br.com.bthirtyeight.model.Person;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)//instrega o spring Framework com o JUnit cinco(permite o use de componentes, bens e recursos no ambiente de test)
@DataJpaTest//configu o teste para trabalhar com JPA,ele carrega apenas os componentes relacionado a camada de persistencia
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)//define que o banco de dados real vai ser usado durante os teste(e nao o h2)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)//define a ordem de execucao dos testes
class PersonRepositoryTest extends AbstractIntegrationTest {//temos que extender pq estamos usando test container

    @Autowired
    PersonRepository repository;
    private static Person person;

    @BeforeAll
    static void setUp() {
        person = new Person();
    }

    @Test
    @Order(1)
    void findPeopleByName() {
        Pageable pageable =
                PageRequest.of(
                        0, 12,
                        Sort.by(Sort.Direction.ASC, "firstName")
                );

        person = repository.findPeopleByName("muel",pageable).getContent().get(0);

        assertNotNull(person);
        assertNotNull(person.getId());
        assertEquals("Santa Rita-Brazil",person.getAddress());
        assertEquals("Samuel",person.getFirstName());
        assertEquals("Melo",person.getLastName());
        assertEquals("Male",person.getGender());
        assertTrue(person.getEnabled());
    }

    @Test
    @Order(2)
    void disablePerson() {

        Long id = person.getId();
        repository.disablePerson(id);

        var result = repository.findById(id);
        person = result.get();

        assertNotNull(person);
        assertNotNull(person.getId());
        assertEquals("Santa Rita-Brazil",person.getAddress());
        assertEquals("Samuel",person.getFirstName());
        assertEquals("Melo",person.getLastName());
        assertEquals("Male",person.getGender());
        assertFalse(person.getEnabled());
    }
}