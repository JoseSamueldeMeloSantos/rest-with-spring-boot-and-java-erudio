package br.com.bthirtyeight.services;

import br.com.bthirtyeight.controllers.PersonController;
import br.com.bthirtyeight.data.dto.PersonDTO;
import br.com.bthirtyeight.exception.RequiredObjectIsNullException;
import br.com.bthirtyeight.exception.ResourceNotFoundException;
import br.com.bthirtyeight.model.Person;
import br.com.bthirtyeight.repository.PersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.util.List;
import java.util.logging.Logger;

//import para metodos estaticos(nao precisa ficar declarando o metodo)
import static br.com.bthirtyeight.mapper.ObjectMapper.parseListObjects;
import static br.com.bthirtyeight.mapper.ObjectMapper.parseObject;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service//para deixar a instancia da classe service a disposicao
public class PersonServices {

    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    @Autowired//para injetar o repository
    private PersonRepository repository;

    public Page<PersonDTO> findAll(Pageable pageable) {
        logger.info("find all people");

        var people = repository.findAll(pageable);

        var peopleWithLinks = people.map(person -> {
            var dto = parseObject(person,PersonDTO.class);
            addHateoasLinks(dto);
            return dto;
        });

        return peopleWithLinks;
    }

    public PersonDTO findById(Long id) {
        logger.info("Finding one Person!");

        var entity = repository.findById(id)
                        //retorna uma exception caso nao ache no database
                        .orElseThrow(() -> new ResourceNotFoundException(""));

        var dto = parseObject(entity,PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public PersonDTO create(PersonDTO person) {

        if(person == null) throw  new RequiredObjectIsNullException();


        logger.info("Creatin one Person");

        var entity = parseObject(person,Person.class);

        //ta salvando no banco usando o save apos isso converte novamente para DTO e retorna o DTO
        //     obs:o save retorna o obj que ele salvou
        var dto = parseObject(repository.save(entity),PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public PersonDTO update(PersonDTO person) {
        if(person == null) throw  new RequiredObjectIsNullException();

        logger.info("Updating One Person!");

        Person entity = repository.findById(person.getId())
                            .orElseThrow(() -> new ResourceNotFoundException(""));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        //ta salvando no banco usando o save apos isso converte novamente para DTO e retorna o DTO
        var dto =  parseObject(repository.save(entity),PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public void delete(Long id) {
        logger.info("Deleting one person");

        Person entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this id!"));

        repository.delete(entity);
    }

    @Transactional//garante que todas as operações dentro do método sejam tratadas como uma única transação ACID(necessario pq trata de um metodo que usa um query personalizado)
    public PersonDTO disablePerson(Long id) {
        logger.info("Disablin one Person!");

        repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this id!"));

        repository.disablePerson(id);

        var entity = repository.findById(id).get();//pega a pessoa que foi modificada
        var dto = parseObject(entity, PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    private static void addHateoasLinks(PersonDTO dto) {
        //dentro do methodoOn a gente vai referenciar o metodo endpoint do controler
        dto.add(linkTo(methodOn(PersonController.class).findById(dto.getId())).withSelfRel().withType("GET"));

        dto.add(linkTo(methodOn(PersonController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));

        dto.add(linkTo(methodOn(PersonController.class).findAll(1,12,"asc")).withRel("findAll").withType("GET"));

        dto.add(linkTo(methodOn(PersonController.class).create(dto)).withRel("create").withType("POST"));

        dto.add(linkTo(methodOn(PersonController.class).update(dto)).withRel("update").withType("UPDATE"));

        dto.add(linkTo(methodOn(PersonController.class).disablePerson(dto.getId())).withRel("patch").withType("PATCH"));
    }
}
