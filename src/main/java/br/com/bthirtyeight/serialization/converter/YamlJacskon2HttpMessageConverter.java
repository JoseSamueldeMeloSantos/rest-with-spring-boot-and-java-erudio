package br.com.bthirtyeight.serialization.converter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;

//nenhuma classe pode herdar uma classe final
public final class YamlJacskon2HttpMessageConverter extends AbstractJackson2HttpMessageConverter {


    protected YamlJacskon2HttpMessageConverter() {
        super(new YAMLMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL),
                MediaType.parseMediaType("application/yaml")
        );
    }
}
