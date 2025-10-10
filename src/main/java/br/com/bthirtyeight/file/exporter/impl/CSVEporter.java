package br.com.bthirtyeight.file.exporter.impl;

import br.com.bthirtyeight.data.dto.PersonDTO;
import br.com.bthirtyeight.file.exporter.contract.FileExporter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.core.io.ByteArrayResource;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class CSVEporter implements FileExporter {

    @Override
    public Resource exportFile(List<PersonDTO> people) throws Exception {
        //Cria um fluxo de saída em memória, ou seja, um “arquivo” que fica armazenado como bytes em memória RAM.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        //Cria um escritor que converte caracteres em bytes e grava no outputStream.
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);

//        Usa o Apache Commons CSV para configurar o formato do arquivo CSV.
        CSVFormat csvFormat = CSVFormat.Builder.create()
                .setHeader( "ID", "First Name", "Last Name", "Address", "Gender","Enabled")//define o cabeçalho
                .setSkipHeaderRecord(false)//indica que o cabeçalho não deve ser ignorado
                .build();//finaliza


        //preenche o arquivo
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat)){

            for (PersonDTO person: people) {
                csvPrinter.printRecord(
                        person.getId(),
                        person.getFirstName(),
                        person.getLastName(),
                        person.getAddress(),
                        person.getGender(),
                        person.getEnabled()
                );
            }
        }

        return new ByteArrayResource(outputStream.toByteArray());
    }
}
