package br.com.bthirtyeight.controllers;

import br.com.bthirtyeight.controllers.docs.FileControllerDocs;
import br.com.bthirtyeight.data.dto.UploadFileResponseDTO;
import br.com.bthirtyeight.services.FileStorageService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/file/v1")
public class FileController implements FileControllerDocs {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileStorageService service;

    @PostMapping("/uploadFile")
    @Override
    public UploadFileResponseDTO uploadFIle(@RequestParam("file") MultipartFile file) {

        var fileName = service.storageFile(file);//o arquivo e passado para o serviço responsavel por fazer a gravação do arquivo em disco e retona o nome do arquivo já tratado

        var fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()//chamado para construir o basepath(vai variar dependendo do local da hospedagem)
                //define o path normal do arquivo
                .path("/api/file/v1/downloadFile/")//
                .path(fileName)
                //converte a uri para string
                .toUriString();

        //montando o retorno
        return new UploadFileResponseDTO(fileName, fileDownloadUri, file.getContentType(), file.getSize());
    }

    @PostMapping("/uploadMultiplesFiles")
    @Override
    public List<UploadFileResponseDTO> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {

        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFIle(file))
                .collect(Collectors.toList());
    }

    //path param pq se não passar da problema
    @GetMapping("downloadFile/{fileName:.+}")
    @Override
    public ResponseEntity<Resource> downlaodFIle(@PathVariable String fileName, HttpServletRequest request) {// a request e setado pelo proprio spring
        // Chama o serviço que carrega o arquivo do disco como um objeto Resource
        Resource resource = service.loadFileAsResource(fileName);

        // Variável para armazenar o tipo MIME (tipo do arquivo, ex: image/png, application/pdf)
        String contentType = null;


        try {
            // Tenta descobrir o tipo de conteúdo do arquivo com base na extensão
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (Exception e) {
//            Caso não consiga identificar o tipo do arquivo, apenas registra no log
            logger.error("could not determine file type!");
        }

        //setando contentType default(padrao)
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        // Monta a resposta HTTP que será enviada ao navegador do usuário
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))// Define o tipo de arquivo
                .header(HttpHeaders.CONTENT_DISPOSITION, "atachment; filename=\"" + resource.getFilename() + "\"")// Força o navegador a baixar (attachment)
                .body(resource); // Corpo da resposta: o arquivo em si
    }
}
