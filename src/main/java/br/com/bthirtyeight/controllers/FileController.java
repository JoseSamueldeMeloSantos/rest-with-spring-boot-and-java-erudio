package br.com.bthirtyeight.controllers;

import br.com.bthirtyeight.controllers.docs.FileControllerDocs;
import br.com.bthirtyeight.data.dto.UploadFileResponseDTO;
import br.com.bthirtyeight.services.FileStorageService;
import jakarta.servlet.http.HttpServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
                .path("/api/file/v1/uploadFile")//
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

    @Override
    public ResponseEntity<ResponseEntity> downlaodFIle(String fileName, HttpServlet request) {
        return null;
    }
}
