package br.com.bthirtyeight.file.exporter.contract;

import br.com.bthirtyeight.data.dto.PersonDTO;

import java.util.List;

public interface FileExporter {

    List<PersonDTO> exportFile(List<PersonDTO> people) throws Exception;
}
