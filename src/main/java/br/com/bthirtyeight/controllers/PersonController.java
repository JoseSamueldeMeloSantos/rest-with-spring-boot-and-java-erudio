package br.com.bthirtyeight.controllers;

import br.com.bthirtyeight.data.dto.PersonDTO;
import br.com.bthirtyeight.model.Person;
import br.com.bthirtyeight.services.PersonServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/person/v1")
public class PersonController {

    @Autowired//injeta a instancia do service(da um new)
    private PersonServices service;

    @GetMapping(value = "/{id}",
                produces = MediaType.APPLICATION_JSON_VALUE)
    public PersonDTO findById(@PathVariable("id") Long id) {
           return service.findById(id);
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PersonDTO> findAll() {
        return service.findAll();
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,//Diz que o tipo de valor que ele vai consumir e um json(nao e necessario mas e bom)
            produces = MediaType.APPLICATION_JSON_VALUE//Diz que o tipo de resposta gerado por esse endpoint será JSON
    )
    public PersonDTO create(@RequestBody PersonDTO person) {//RequestBody -> usado para url do tipo body
        return service.create(person);
    }


    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public PersonDTO update(@RequestBody PersonDTO person) {
        return service.update(person);
    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();//vai retornar o status code
    }
}
