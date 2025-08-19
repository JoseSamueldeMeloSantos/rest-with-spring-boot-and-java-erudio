package br.com.bthirtyeight.services;

import br.com.bthirtyeight.controllers.PersonController;
import br.com.bthirtyeight.data.dto.PersonDTO;
import br.com.bthirtyeight.exception.ResourceNotFoundException;
import br.com.bthirtyeight.model.Person;
import br.com.bthirtyeight.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

//import para metodos estaticos(nao precisa ficar declarando o metodo)
import static br.com.bthirtyeight.mapper.ObjectMapper.parseListObjects;
import static br.com.bthirtyeight.mapper.ObjectMapper.parseObeject;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service//para deixar a instancia da classe service a disposicao
public class PersonServices {

    private final AtomicLong counter = new AtomicLong();
    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    @Autowired//para injetar o repository
    private PersonRepository repository;

    public  List<PersonDTO> findAll() {
        logger.info("find all people");

        return parseListObjects(repository.findAll(),PersonDTO.class);
    }

    public PersonDTO findById(Long id) {
        logger.info("Finding one Person!");

        var entity = repository.findById(id)
                        //retorna uma exception caso nao ache no database
                        .orElseThrow(() -> new ResourceNotFoundException(""));

        var dto = parseObeject(entity,PersonDTO.class);
        addHateoasLinks(id,dto);
        return dto;
    }

    public PersonDTO create(PersonDTO person) {
        logger.info("Creatin one Person");

        var entity = parseObeject(person,Person.class);

        //ta salvando no banco usando o save apos isso converte novamente para DTO e retorna o DTO
        //     obs:o save retorna o obj que ele salvou
        return parseObeject(repository.save(entity),PersonDTO.class);
    }

    public PersonDTO update(PersonDTO person) {
        logger.info("Updating One Person!");

        Person entity = repository.findById(person.getId())
                            .orElseThrow(() -> new ResourceNotFoundException(""));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        //ta salvando no banco usando o save apos isso converte novamente para DTO e retorna o DTO
        return parseObeject(repository.save(entity),PersonDTO.class);
    }

    public void delete(Long id) {
        logger.info("Deleting one person");

        Person entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(""));

        repository.delete(entity);
    }

    private static void addHateoasLinks(Long id, PersonDTO dto) {
        //dentro do methodoOn a gente vai referenciar o metodo endpoint do controler
        dto.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel().withType("GET"));

        dto.add(linkTo(methodOn(PersonController.class).delete(id)).withRel("delete").withType("DELETE"));

        dto.add(linkTo(methodOn(PersonController.class).findAll()).withRel("findAll").withType("GET"));

        dto.add(linkTo(methodOn(PersonController.class).create(dto)).withRel("create").withType("POST"));

        dto.add(linkTo(methodOn(PersonController.class).update(dto)).withRel("update").withType("UPDATE"));

    }
}
