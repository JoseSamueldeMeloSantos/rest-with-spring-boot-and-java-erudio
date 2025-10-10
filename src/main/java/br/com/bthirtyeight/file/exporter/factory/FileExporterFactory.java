package br.com.bthirtyeight.file.exporter.factory;

import br.com.bthirtyeight.exception.BadRequestException;
import br.com.bthirtyeight.file.exporter.MediaTypes;
import br.com.bthirtyeight.file.exporter.contract.FileExporter;
import br.com.bthirtyeight.file.exporter.impl.CSVEporter;
import br.com.bthirtyeight.file.exporter.impl.XlsxExporter;
import br.com.bthirtyeight.file.importer.contract.FileImporter;
import br.com.bthirtyeight.file.importer.factory.FileImporterFactory;
import br.com.bthirtyeight.file.importer.impl.CSVImporter;
import br.com.bthirtyeight.file.importer.impl.XlsxImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class FileExporterFactory {

     /*
    magine que XlsxImporter ou CSVImporter usam outras classes com @Autowired.
Se você usasse new XlsxImporter(), o Spring não injetaria essas dependências — daria NullPointerException
     */

    private Logger logger = LoggerFactory.getLogger(FileExporterFactory.class);

    @Autowired
    private ApplicationContext context;

    public FileExporter getExporter(String acceptHeader) throws Exception {

        if (acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_XLSX_VALUE)) {
            //exerce mesma funcao do new
            return context.getBean(XlsxExporter.class);//Pede ao Spring para te entregar o bean configurado
//            return new XlsxImporter();
        } else if (acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_CSV_VALUE)){
            return context.getBean(CSVEporter.class);
//            return new CSVImporter();
        } else {
            throw new BadRequestException();
        }
    }
}
