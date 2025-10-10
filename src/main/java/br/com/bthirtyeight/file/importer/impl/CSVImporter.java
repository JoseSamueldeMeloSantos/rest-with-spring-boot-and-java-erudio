package br.com.bthirtyeight.file.importer.impl;

import br.com.bthirtyeight.data.dto.PersonDTO;
import br.com.bthirtyeight.file.importer.contract.FileImporter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class CSVImporter implements FileImporter {

    @Override
    public List<PersonDTO> importFile(InputStream inputStream) throws Exception {

        CSVFormat format = CSVFormat.Builder.create()//cria um objeto CSVFormat
                .setHeader()//indica que o CSV possui cabeçalho (nomes das colunas).
                .setSkipHeaderRecord(true)//ignora a primeira linha do arquivo, que contém os nomes das colunas.
                .setIgnoreEmptyLines(true)//ignora linhas vazias no arquivo.
                .setTrim(true)//remove espaços em branco antes e depois dos valores das células.
                .build();//finaliza a construção do objeto CSVFormat.

        //lê o CSV de acordo com o formato definido e cria um iterable
        Iterable<CSVRecord> records = format.parse(new InputStreamReader(inputStream));
        /*
        format.parse(...): lê o CSV de acordo com o formato definido acima.
new     InputStreamReader(inputStream): converte o InputStream em um Reader, que o CSVFormat consegue ler.
         */
        return parseRecordsToPersonDTOs(records);
    }

    private List<PersonDTO> parseRecordsToPersonDTOs(Iterable<CSVRecord> records) {

        List<PersonDTO> people = new ArrayList<>();

        for (CSVRecord record: records) {
            PersonDTO person = new PersonDTO();
            //seta o atributo de acordo um a tabela escolhida do csv
            person.setFirstName(record.get("first_name"));
            person.setLastName(record.get("last_name"));
            person.setAddress(record.get("Address"));
            person.setGender(record.get("gender"));
            person.setEnabled(true);

            people.add(person);
        }

        return people;
    }
}
