package br.com.bthirtyeight.file.importer.impl;

import br.com.bthirtyeight.data.dto.PersonDTO;
import br.com.bthirtyeight.file.importer.contract.FileImporter;

import java.io.InputStream;
import java.util.List;

public class XlsxImporter implements FileImporter {

    @Override
    public List<PersonDTO> importFile(InputStream inputStream) throws Exception {
        return List.of();
    }
}
