package br.com.bthirtyeight.file.importer.impl;

import br.com.bthirtyeight.data.dto.PersonDTO;
import br.com.bthirtyeight.file.importer.contract.FileImporter;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XlsxImporter implements FileImporter {

    @Override
    public List<PersonDTO> importFile(InputStream inputStream) throws Exception {

        /*
        O que você mostrou é um try-with-resources do Java. Ele é uma forma especial do try que garante que os recursos abertos
        (como arquivos, streams, conexões de banco de dados etc.) sejam fechados automaticamente ao final do bloco, mesmo que ocorra uma exceção
         */
        try (XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {//XSSFWorkbook representa todo o arquivo .xlsx.
            XSSFSheet sheet = workbook.getSheetAt(0);//pega a primeira planilha (sheet) do arquivo Excel.
            Iterator<Row> rowIterator = sheet.iterator();//Cria um iterador para percorrer todas as linhas da planilha.

            if (rowIterator.hasNext()) rowIterator.next();//Pula a primeira linha, geralmente usada como cabeçalho (nomes das colunas).

            return parseRowsToPersonDtoList(rowIterator);
        }
    }

    private List<PersonDTO> parseRowsToPersonDtoList(Iterator<Row> rowIterator) {

        List<PersonDTO> people = new ArrayList<>();

        while (rowIterator.hasNext()) {//Percorre todas as linhas restantes do Excel
            Row row = rowIterator.next();

            //Verifica se a linha é válida (não está em branco) e, se for, converte em PersonDTO e adiciona à lista.
            if (isRowValid(row)) {
                people.add(parseRowsToPersonDto(row));
            }
        }

        return people;
    }

    private PersonDTO parseRowsToPersonDto(Row row) {
        PersonDTO person = new PersonDTO();

        //Preenche o PersonDTO com os valores das células da linha
        person.setFirstName(row.getCell(0).getStringCellValue());
        person.setLastName(row.getCell(1).getStringCellValue());
        person.setAddress(row.getCell(2).getStringCellValue());
        person.setGender(row.getCell(3).getStringCellValue());
        person.setEnabled(true);

        return person;
    }

    private boolean isRowValid(Row row) {
        //Verifica se a primeira célula da linha não está vazia.
        //Isso serve para ignorar linhas em branco no Excel.
        return row.getCell(0) != null && row.getCell(0).getCellType() != CellType.BLANK;
    }
}
