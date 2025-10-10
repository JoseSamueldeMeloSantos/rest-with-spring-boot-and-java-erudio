package br.com.bthirtyeight.file.exporter.impl;

import br.com.bthirtyeight.data.dto.PersonDTO;
import br.com.bthirtyeight.file.exporter.contract.FileExporter;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class XlsxExporter implements FileExporter {
    @Override
    public Resource exportFile(List<PersonDTO> people) throws Exception {
        return List.of();
    }
}
