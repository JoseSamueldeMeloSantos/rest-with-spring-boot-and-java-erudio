package br.com.bthirtyeight.file.exporter.impl;

import br.com.bthirtyeight.data.dto.PersonDTO;
import br.com.bthirtyeight.file.exporter.contract.FileExporter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CSVEporter implements FileExporter {
    @Override
    public List<PersonDTO> exportFile(List<PersonDTO> people) throws Exception {
        return List.of();
    }
}
