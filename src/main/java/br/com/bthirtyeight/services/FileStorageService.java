package br.com.bthirtyeight.services;

import br.com.bthirtyeight.config.FileStorageConfig;
import br.com.bthirtyeight.controllers.FileController;
import br.com.bthirtyeight.exception.FileNotFounException;
import br.com.bthirtyeight.exception.FileStorageException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);

    private final Path fileStorageLocation;//define a variavel dizendo onde vai armazenar o arquivo

    @Autowired
    public FileStorageService(FileStorageConfig fileStorageConfig) {
        Path path = Paths.get(fileStorageConfig.getUploadDir())//tá pegando o path defino como config no propreties.yaml
                .toAbsolutePath().normalize();//Converte esse caminho relativo Converte esse caminho relativo dps Remove partes redundantes do caminho


        this.fileStorageLocation = path;

        try {
            logger.info("creating directories");
            Files.createDirectories(this.fileStorageLocation);//cria o diretorio de armazenamento
        } catch (Exception e ) {
            logger.error("Could not create de directory where files will be stored");
            throw new FileStorageException("Could not create de directory where files will be stored", e);
        }
    }


    public String storageFile(MultipartFile file) {//aqui tentea gravar e salvar o arquivo em disco

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());//limpa o nome do arquivo(remove algum caractece que não seja aceito

        try {
            if (fileName.contains("..")) {//verifica se tem ../ e se tiver ele lanca exception
                throw new FileStorageException("Sorry! Filename Contains an Ivalid Path Sequence " + fileName);
            }

            logger.info("Saving file in disk");

            Path targetLocation = this.fileStorageLocation.resolve(fileName);//determina o path onde o arquvio vai ser salvo e o nome que o arquvi vai ter
            // Copia o arquivo enviado (InputStream) para o destino especificado (targetLocation),
            // substituindo o arquivo existente se já houver um com o mesmo nome(StandardCopyOption.REPLACE_EXISTING).
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (Exception e) {
            logger.error("Could not store file" + fileName + ". Please try again!");
            throw new FileStorageException("Could not store file" + fileName + ". Please try again!", e);
        }
    }

    public Resource loadFileAsResource(String fileName) {

        try {
            // Cria um caminho absoluto do arquivo que será carregado:
            Path filePath = this.fileStorageLocation//é o diretório onde os arquivos estão salvo
                    .resolve(fileName)//→ adiciona o nome do arquivo ao caminho base.
                    .normalize();//remove possíveis “..” e corrige o caminho (evita diretórios inválidos).

            // Cria um objeto Resource que representa o arquivo no sistema.
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return  resource;
            } else {
                throw new FileNotFounException("File not found " + fileName);
            }
        } catch (Exception e) {
            logger.error("File not found " + fileName);
            throw new FileNotFounException("File not found " + fileName, e);
        }
    }
}
