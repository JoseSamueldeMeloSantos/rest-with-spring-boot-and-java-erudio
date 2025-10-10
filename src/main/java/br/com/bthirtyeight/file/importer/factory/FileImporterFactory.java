package br.com.bthirtyeight.file.importer.factory;

import br.com.bthirtyeight.exception.BadRequestException;
import br.com.bthirtyeight.file.importer.contract.FileImporter;
import br.com.bthirtyeight.file.importer.impl.CSVImporter;
import br.com.bthirtyeight.file.importer.impl.XlsxImporter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class FileImporterFactory {
    /*
    magine que XlsxImporter ou CSVImporter usam outras classes com @Autowired.
Se você usasse new XlsxImporter(), o Spring não injetaria essas dependências — daria NullPointerException
     */

    private Logger logger = LoggerFactory.getLogger(FileImporterFactory.class);

    @Autowired
    private ApplicationContext context;

    public FileImporter getImporter(String fileName) throws Exception {

        if (fileName.endsWith(".xlsx")) {
            //exerce mesma funcao do new
            return context.getBean(XlsxImporter.class);//Pede ao Spring para te entregar o bean configurado
//            return new XlsxImporter();
        } else if (fileName.endsWith(".csv")){
            return context.getBean(CSVImporter.class);
//            return new CSVImporter();
        } else {
            throw new BadRequestException();
        }
    }
}
