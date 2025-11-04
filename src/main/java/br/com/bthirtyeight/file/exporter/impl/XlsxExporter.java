package br.com.bthirtyeight.file.exporter.impl;

import br.com.bthirtyeight.data.dto.PersonDTO;
import br.com.bthirtyeight.file.exporter.contract.FileExporter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class XlsxExporter implements FileExporter {
    @Override
    public Resource exportFile(List<PersonDTO> people) throws Exception {

        try (Workbook workbook = new XSSFWorkbook()){//Cria um novo arquivo Excel em memória usando o XSSFWorkbook
            Sheet sheet = workbook.createSheet("People");//Cria uma nova aba (planilha) chamada "People" dentro do arquivo Excel

            //Cria a primeira linha (índice 0) da planilha, que servirá como cabeçalho.
            Row headerRow = sheet.createRow(0);

            String[] headers = {"ID", "First Name", "Last Name", "Address", "Gender", "Enabled"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);//Cria uma célula para cada coluna.

                cell.setCellValue(headers[i]);//Define o texto do cabeçalho.
                cell.setCellStyle(createHeaderStyle(workbook));//Aplica um estilo personalizado
             }

            int rowIndex =1;//Define o índice inicial das próximas linhas (1, pois a linha 0 é o cabeçalho).
            for (PersonDTO person : people) {

                Row row = sheet.createRow(rowIndex++);

//              Cria e preenche as células da linha com os dados da pessoa.
                row.createCell(0).setCellValue(person.getId());
                row.createCell(1).setCellValue(person.getFirstName());
                row.createCell(2).setCellValue(person.getLastName());
                row.createCell(3).setCellValue(person.getAddress());
                row.createCell(4).setCellValue(person.getGender());
                row.createCell(5).setCellValue(person.getEnabled() != null && person.getEnabled() ? "yes" :"No");
            }

            //Ajusta automaticamente a largura das colunas conforme o conteúdo,
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

//           Cria um fluxo de saída em memória e escreve o conteúdo do arquivo Excel nele.
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            //Converte o arquivo gerado em um array de bytes e o retorna
            return new ByteArrayResource(outputStream.toByteArray());
        }
    }

//    Cria um estilo:
//    Define a fonte como negrito;
//    Centraliza o texto horizontalmente.
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);

        return style;
    }
}
