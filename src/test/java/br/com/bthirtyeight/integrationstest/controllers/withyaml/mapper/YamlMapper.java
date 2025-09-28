package br.com.bthirtyeight.integrationstest.controllers.withyaml.mapper;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.restassured.mapper.ObjectMapper;
import io.restassured.mapper.ObjectMapperDeserializationContext;
import io.restassured.mapper.ObjectMapperSerializationContext;

//como nao existe um mapper para yaml e necessario criar um do zero
public class YamlMapper implements ObjectMapper {

    private com.fasterxml.jackson.databind.ObjectMapper objectMapper;
    protected TypeFactory typeFactory;

    public YamlMapper() {
        objectMapper = new com.fasterxml.jackson.databind.ObjectMapper(new YAMLFactory())//instancia o ObjMapper definindo que vai ser um yaml
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);//seleciona a propriedade de serializacao

        typeFactory = TypeFactory.defaultInstance();
    }

    @Override//Transforma YAML → Objeto Java.
    public Object deserialize(ObjectMapperDeserializationContext context) {

        var content = context.getDataToDeserialize().asString();
        Class type = (Class) context.getType();//pega o tipo da classe do context

        try {
            return objectMapper.readValue(content , typeFactory.constructType(type));//Esse comando lê o YAML recebido (como String) e cria automaticamente um objeto Java do tipo esperado.
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error deserializing YAML content", e);
        }
    }

    @Override//Transforma Objeto Java → YAML.
    public Object serialize(ObjectMapperSerializationContext context) {
        try {
            return objectMapper.writeValueAsString(context.getObjectToSerialize());//Esse comando transforma qualquer objeto Java em texto YAML
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error deserializing YAML content", e);
        }
    }
}
