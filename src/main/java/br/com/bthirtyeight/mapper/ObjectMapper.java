package br.com.bthirtyeight.mapper;

import com.github.dozermapper.core.DozerBeanMapper;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ObjectMapper {

    //esse cara vai mapaear Entidade para DTO ou DTO para Entidade
    private static Mapper mapper = DozerBeanMapperBuilder.buildDefault();//sempre importar de com.github

    //esse metodo vai se dedicar a pasar DTO para Entidade e Entidade para DTO
    public static <O,D> D parseObeject(O origin, Class<D> destination) {
        return mapper.map(origin, destination);//faz a conversao de obj
    }

    //esse metodo vai se dedicar a pasar uma lista de DTO para Entidade/Entidade para DTO
    public static <O,D> List<D> parseListObjects(List<O> origin, Class<D> destination) {

        List<D> destinationObjs = new ArrayList<>();

        for (Object o: origin) {
            destinationObjs.add(mapper.map(o,destination));
        }

        return destinationObjs;
    }
}
