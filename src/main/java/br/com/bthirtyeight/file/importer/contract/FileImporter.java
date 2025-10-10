package br.com.bthirtyeight.file.importer.contract;

import br.com.bthirtyeight.data.dto.PersonDTO;

import java.io.InputStream;
import java.util.List;

public interface FileImporter {

    List<PersonDTO> importFile(InputStream inputStream)  throws Exception;
}
