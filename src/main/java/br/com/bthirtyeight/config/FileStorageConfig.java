package br.com.bthirtyeight.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "file")//o prefix é a propriedade que a gente inventou no arquivo propeties.yaml
public class FileStorageConfig {

    //o nome do atributo deve ser semelhante a propriedade presente no propeties.yaml
    private String uploadDir;

    public FileStorageConfig() {
    }

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}
