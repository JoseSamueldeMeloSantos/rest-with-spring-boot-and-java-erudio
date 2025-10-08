package br.com.bthirtyeight.services;

import br.com.bthirtyeight.config.FileStorageConfig;
import br.com.bthirtyeight.exception.FileStorageException;


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

    private final Path fileStorageLocation;//define a variavel dizendo onde vai armazenar o arquivo

    public FileStorageService(FileStorageConfig fileStorageConfig) {
        Path path = Paths.get(fileStorageConfig.getUploadDir())//tá pegando o path defino como config no propreties.yaml
                .toAbsolutePath().normalize();//Converte esse caminho relativo Converte esse caminho relativo dps Remove partes redundantes do caminho


        this.fileStorageLocation = path;

        try {
            Files.createDirectories(this.fileStorageLocation);//cria o diretorio de armazenamento
        } catch (Exception e ) {
            throw new FileStorageException("Could not create de directory where files will be stored", e);
        }
    }

    public String storageFile(MultipartFile file) {//aqui tentea gravar e salvar o arquivo em disco

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());//limpa o nome do arquivo(remove algum caractece que não seja aceito

        try {
            if (fileName.contains("..")) {//verifica se tem ../ e se tiver ele lanca exception
                throw new FileStorageException("Sorry! Filename Contains an Ivalid Path Sequence " + fileName);
            }

            Path targetLocation = this.fileStorageLocation.resolve(fileName);//determina o path onde o arquvio vai ser salvo e o nome que o arquvi vai ter
            // Copia o arquivo enviado (InputStream) para o destino especificado (targetLocation),
            // substituindo o arquivo existente se já houver um com o mesmo nome(StandardCopyOption.REPLACE_EXISTING).
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (Exception e) {
            throw new FileStorageException("Could not store file" + fileName + ". Please try again!", e);
        }
    }
}
