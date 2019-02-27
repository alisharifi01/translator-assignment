package com.assetcontrol.translator.biz;

import com.assetcontrol.translator.model.ColumnDictionaryConfigHolder;
import com.assetcontrol.translator.model.RowDictionaryConfigHolder;
import com.assetcontrol.translator.biz.exception.ConfigFileReadingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

/**
 * Responsible for read , parse config files and put it in memory as models
 */
@Component
public class ConfigFilesParser {

    private Logger logger = LoggerFactory.getLogger(ConfigFilesParser.class);

    @Value("${file.path.config.row.mapping}")
    private String rowMappingConfigFilePath;

    @Value("${file.path.config.column.mapping}")
    private String columnMappingConfigFilePath;

    @Autowired
    private RowDictionaryConfigHolder rowDictionaryConfigHolder;

    @Autowired
    private ColumnDictionaryConfigHolder columnDictionaryConfigHolder;

    public void parseFiles() {
        parseRowMappingFile();
        parseColumnMappingFile();

    }

    private void parseRowConfigLine(String line) {
        String[] split = line.split("\\t");
        rowDictionaryConfigHolder.getRowDictionary().put(split[0], split[1]);
    }

    private void parseColumnConfigLine(String line) {
        String[] split = line.split("\\t");
        columnDictionaryConfigHolder.getColumnDictionary().put(split[0], split[1]);
    }

    private void parseColumnMappingFile() {
        try {
            logger.info("parsing config column mapping file has been started | path = " + columnMappingConfigFilePath);
            long start = new Date().getTime();
            Files.lines(Paths.get(columnMappingConfigFilePath)).forEach(this::parseColumnConfigLine);
            long end = new Date().getTime();
            logger.info("parsing config column mapping file has been done | spent time = " + (end -start));
        } catch (IOException e) {
            throw new ConfigFileReadingException(e);
        }
    }

    private void parseRowMappingFile() {
        try {
            logger.info("parsing config row mapping file has been started | path = "+ rowMappingConfigFilePath);
            long start = new Date().getTime();
            Files.lines(Paths.get(rowMappingConfigFilePath)).forEach(this::parseRowConfigLine);
            long end = new Date().getTime();
            logger.info("parsing config row mapping file has been done | spent time = " + (end -start));
        } catch (IOException e) {
            throw new ConfigFileReadingException(e);
        }
    }
}
