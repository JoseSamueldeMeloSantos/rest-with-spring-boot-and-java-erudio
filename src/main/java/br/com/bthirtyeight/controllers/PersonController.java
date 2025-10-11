package br.com.bthirtyeight.controllers;

import br.com.bthirtyeight.controllers.docs.PersonControllerDocs;
import br.com.bthirtyeight.data.dto.PersonDTO;
import br.com.bthirtyeight.file.exporter.MediaTypes;
import br.com.bthirtyeight.model.Person;
import br.com.bthirtyeight.services.PersonServices;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/*fazer cors a nivel de controller
@CrossOrigin(origins = "http://localhost:8080")//para definir quem pode consumir a api(caso vc nao defina o origins,geral pode usar so com o @crossOrigon)
 */
@RestController
@RequestMapping("/person/v1")
@Tag(name = "People", description = "Endpoints for Managing people")//agrupa endpoints em seções.
public class PersonController implements PersonControllerDocs {

    @Autowired//injeta a instancia do service(da um new)
    private PersonServices service;

    //@CrossOrigin(origins = "http://localhost:8080")//habilitando cors idividualmente em cada endpoint
    @GetMapping(value = "/{id}",
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE})
    @Override
    public PersonDTO findById(@PathVariable("id") Long id) {
           return service.findById(id);
    }


    @GetMapping(produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_YAML_VALUE})
    @Override
    public ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "firstName"));//o nome do atributo que ser exatamente como esta no dto
        return ResponseEntity.ok(service.findAll(pageable));
    }


    @GetMapping(
            value = "/exportPage",
            produces = {
                    MediaTypes.APPLICATION_CSV_VALUE,
                    MediaTypes.APPLICATION_XLSX_VALUE
            })
    @Override
    public ResponseEntity<Resource> exportPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            HttpServletRequest request
    ) {
        //Converte o parâmetro direction em um objeto Sort.Direction
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
//        Cria um objeto Pageable do Spring Data, que define(page,size,campo de ordenacao)
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "firstName"));//o nome do atributo que ser exatamente como esta no dto

//Lê o cabeçalho Accept da requisição HTTP, que indica o tipo de arquivo que o cliente deseja receber (CSV ou XLSX).
        String acceptHeader = request.getHeader(HttpHeaders.ACCEPT);

        Resource file = service.exportPage(pageable, acceptHeader);

        //Define o tipo MIME do arquivo a ser enviado:
        var contentType = acceptHeader != null ? acceptHeader : "application/octet-stream";
        //Define a extensão do arquivo:
        var fileExtension = MediaTypes.APPLICATION_XLSX_VALUE.equalsIgnoreCase(acceptHeader) ? ".xlsx" : ".csv";
//      Monta o nome final do arquivo que será enviado para o navegador.
        var filename = "people_exported" + fileExtension;

        // Monta a resposta HTTP que será enviada ao navegador do usuário
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))// Define o tipo de arquivo
                .header(HttpHeaders.CONTENT_DISPOSITION, "atachment; filename=\"" + filename + "\"")//adiciona o cabeçalho Content-Disposition para forçar download com o nome definido.
                .body(file); // insere o arquivo real no corpo da resposta.
    }

    @PostMapping(
            value = "/massCreation",
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE}//Diz que o tipo de resposta gerado por esse endpoint será JSON
    )
    @Override
    public List<PersonDTO> massCreation(@RequestParam("file") MultipartFile file) {
        return service.massCreation(file);
    }

    @GetMapping(
            value = "/findPeopleByName/{firstName}",
            produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_YAML_VALUE})
    @Override
    public ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findByName(
            @PathVariable(value = "firstName") String firstName,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "firstName"));//o nome do atributo que ser exatamente como esta no dto
        return ResponseEntity.ok(service.findByName(firstName,pageable));
    }



    //@CrossOrigin(origins = {"http://localhost:8080","http://www.erudio.com.br"})
    @PostMapping(
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE},//Diz que o tipo de valor que ele vai consumir e um json(nao e necessario mas e bom)
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE}//Diz que o tipo de resposta gerado por esse endpoint será JSON
    )
    @Override
    public PersonDTO create(@RequestBody PersonDTO person) {//RequestBody -> usado para url do tipo body
        return service.create(person);
    }


    @PutMapping(
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE},
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    public PersonDTO update(@RequestBody PersonDTO person) {
        return service.update(person);
    }

    @PatchMapping(value = "/{id}",
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE})
    @Override
    public PersonDTO disablePerson(@PathVariable("id") Long id) {
        return service.disablePerson(id);
    }

    @DeleteMapping(value = "/{id}")
    @Override
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();//vai retornar o status code
    }
}
