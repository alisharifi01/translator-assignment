package com.assetcontrol.translator.biz;

import com.assetcontrol.translator.model.ColumnDictionaryConfigHolder;
import com.assetcontrol.translator.model.RowDictionaryConfigHolder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@TestPropertySource("classpath:application.properties")
@SpringBootTest(classes = {
        ConfigFilesParser.class,
        RowDictionaryConfigHolder.class,
        ColumnDictionaryConfigHolder.class
})
public class ConfigFileParser_Test {

    @Value("${file.path.config.row.mapping}")
    private String ROW_MAPPING_FILE_PATH;

    @Value("${file.path.config.column.mapping}")
    private String COLUMN_MAPPING_FILE_PATH;

    @Autowired
    private ConfigFilesParser configFilesParser;

    @Autowired
    private RowDictionaryConfigHolder rowDictionaryConfigHolder;

    @Autowired
    private ColumnDictionaryConfigHolder columnDictionaryConfigHolder;

    @Before
    public void writeSampleConfigFiles() throws IOException {

        String sampleRowMappingFile
                = "ID1\tOURID1\n" +
                "ID3\tOURID3";
        FileWriter fileWriter = new FileWriter(new File(ROW_MAPPING_FILE_PATH));
        fileWriter.write(sampleRowMappingFile);
        fileWriter.flush();

        String sampleColumnMappingFile
                = "COL1\tOURID\n" +
                "COL3\tOURCOL3";
        fileWriter = new FileWriter(new File(COLUMN_MAPPING_FILE_PATH));
        fileWriter.write(sampleColumnMappingFile);
        fileWriter.flush();

    }

    @Test
    public void happyScenario() {
        configFilesParser.parseFiles();

        HashMap<String, String> expectedRowDictionary = new HashMap<>();
        expectedRowDictionary.put("ID1" , "OURID1");
        expectedRowDictionary.put("ID3" , "OURID3");

        assertEquals(expectedRowDictionary , rowDictionaryConfigHolder.getRowDictionary());


        HashMap<String, String> expectedColumnDictionary = new HashMap<>();
        expectedColumnDictionary.put("COL1" , "OURID");
        expectedColumnDictionary.put("COL3" , "OURCOL3");

        assertEquals(expectedColumnDictionary, columnDictionaryConfigHolder.getColumnDictionary());
    }
}
