package br.com.bthirtyeight.services;

import br.com.bthirtyeight.data.dto.PersonDTO;
import br.com.bthirtyeight.model.Person;
import br.com.bthirtyeight.repository.PersonRepository;
import br.com.bthirtyeight.unitetests.mapper.mocks.MockPerson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/*
Configurar o mock: Você usa o when para dizer ao Mockito o que o repository.save deve fazer quando for chamado.

Executar a ação: Você chama service.create(dto).
 */

@TestInstance(TestInstance.Lifecycle.PER_CLASS)//define que os objs instaciados nesse classe so serao vagos nessa classe
@ExtendWith(MockitoExtension.class)//diz ao JUnit 5 para inicializar automaticamente todos os seus objetos de simulação (@Mock) e injetá-los onde forem necessários (@InjectMocks), poupando você de escrever código extra.
class PersonServicesTest {



    MockPerson input;

    //---mock externo
    @InjectMocks//para injetar(injecao falsa) os mocks na classe que a gente ta testando
    private PersonServices service;

    @Mock//versao do autowired para a classe Test
    PersonRepository repository;
    //----

    @BeforeEach//esse metodo sera executado antes de cada metodo @test
    void setUp() {
        input = new MockPerson();
        MockitoAnnotations.openMocks(this);//abri os mocks do mockito(necessario para os dois atributos acima funcionar)
    }

    @AfterEach//esse metodo sera executado depois do termino de cada metodo @test
    void tearDown() {
    }

    @Test
    void findById() {
        //mock interno
        Person person = input.mockEntity(1);//simula um acesso ao banco
        person.setId(1L);
        //e optional pois o findbyid da jpa retorna um optional
        when(repository.findById(1L)).thenReturn(Optional.of(person));

        var result = service.findById(1L);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertNotNull(result.getLinks().stream().anyMatch(
                link -> (link.getRel().value().equals("self")) &&
                        (link.getHref().endsWith("/api/person/v1/1")) &&
                        (link.getType().equals("GET"))
            )
        );

        assertNotNull(result.getLinks().stream().anyMatch(
                link -> (link.getRel().value().equals("findAll")) &&
                        (link.getHref().endsWith("/api/person/v1")) &&
                        (link.getType().equals("GET"))
        ));

        assertNotNull(result.getLinks().stream().anyMatch(
                link -> (link.getRel().value().equals("create")) &&
                        (link.getHref().endsWith("/api/person/v1")) &&
                        (link.getType().equals("POST"))
        ));

        assertNotNull(result.getLinks().stream().anyMatch(
                link -> (link.getRel().value().equals("update")) &&
                        (link.getHref().endsWith("/api/person/v1")) &&
                        (link.getType().equals("PUT"))
        ));

        assertNotNull(result.getLinks().stream().anyMatch(
                link -> (link.getRel().value().equals("delete")) &&
                        (link.getHref().endsWith("/api/person/v1/1")) &&
                        (link.getType().equals("DELETE"))
        ));

        assertEquals("Address Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
    }

    @Test
    void create() {
        //mock interno
        Person person = input.mockEntity(1);//simula um acesso ao banco
        Person persisted = person;//apenas para clareza semantica(pois aponta para o mesmo obj)
        person.setId(1L);

        PersonDTO dto = input.mockDTO(1);//simula um cadastro no banco pela classe mock

        when(repository.save(person)).thenReturn(persisted);
        var result = service.create(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertNotNull(result.getLinks().stream().anyMatch(
                        link -> (link.getRel().value().equals("self")) &&
                                (link.getHref().endsWith("/api/person/v1/1")) &&
                                (link.getType().equals("GET"))
                )
        );

        assertNotNull(result.getLinks().stream().anyMatch(
                link -> (link.getRel().value().equals("findAll")) &&
                        (link.getHref().endsWith("/api/person/v1")) &&
                        (link.getType().equals("GET"))
        ));

        assertNotNull(result.getLinks().stream().anyMatch(
                link -> (link.getRel().value().equals("create")) &&
                        (link.getHref().endsWith("/api/person/v1")) &&
                        (link.getType().equals("POST"))
        ));

        assertNotNull(result.getLinks().stream().anyMatch(
                link -> (link.getRel().value().equals("update")) &&
                        (link.getHref().endsWith("/api/person/v1")) &&
                        (link.getType().equals("PUT"))
        ));

        assertNotNull(result.getLinks().stream().anyMatch(
                link -> (link.getRel().value().equals("delete")) &&
                        (link.getHref().endsWith("/api/person/v1/1")) &&
                        (link.getType().equals("DELETE"))
        ));

        assertEquals("Address Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
    }

    @Test
    void update() {
        //mock interno
        Person person = input.mockEntity(1);//simula um acesso ao banco
        Person persisted = person;//apenas para clareza semantica(pois aponta para o mesmo obj)
        person.setId(1L);

        PersonDTO dto = input.mockDTO(1);//simula um cadastro no banco pela classe mock

        //faz a a config de save e find,pois o update ler e escreve
        when(repository.findById(1L)).thenReturn(Optional.of(person));
        when(repository.save(person)).thenReturn(persisted);//Usado para fazer a operacao e retorna retorna uma instancia para teste(isola o teste)

        var result = service.update(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertNotNull(result.getLinks().stream().anyMatch(
                        link -> (link.getRel().value().equals("self")) &&
                                (link.getHref().endsWith("/api/person/v1/1")) &&
                                (link.getType().equals("GET"))
                )
        );

        assertNotNull(result.getLinks().stream().anyMatch(
                link -> (link.getRel().value().equals("findAll")) &&
                        (link.getHref().endsWith("/api/person/v1")) &&
                        (link.getType().equals("GET"))
        ));

        assertNotNull(result.getLinks().stream().anyMatch(
                link -> (link.getRel().value().equals("create")) &&
                        (link.getHref().endsWith("/api/person/v1")) &&
                        (link.getType().equals("POST"))
        ));

        assertNotNull(result.getLinks().stream().anyMatch(
                link -> (link.getRel().value().equals("update")) &&
                        (link.getHref().endsWith("/api/person/v1")) &&
                        (link.getType().equals("PUT"))
        ));

        assertNotNull(result.getLinks().stream().anyMatch(
                link -> (link.getRel().value().equals("delete")) &&
                        (link.getHref().endsWith("/api/person/v1/1")) &&
                        (link.getType().equals("DELETE"))
        ));

        assertEquals("Address Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
    }

    @Test
    void delete() {
        Person person = input.mockEntity(1);//simula um acesso ao banco
        person.setId(1L);
        //precisa configurar o findById pq o dele faz um get antes de apagar
        when(repository.findById(1L)).thenReturn(Optional.of(person));

        service.delete(1L);

        verify(repository, times(1)).findById(anyLong());//verifica se foi chamado e que se foi chamado apena uma vez(para qualquer id)
        verify(repository, times(1)).delete(any(Person.class));//verifica se foi chamado e que se foi apagado apenas uma vez(para qualquer person)
        verifyNoInteractions(repository);//verifica se nao teve mais interações
    }

    @Test
    void findAll() {
    }
}