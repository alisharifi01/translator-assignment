package com.dorsa.translator.biz;

import com.dorsa.translator.biz.exception.InvalidFileNameException;
import com.dorsa.translator.biz.exception.InvalidLineFormatException;
import com.dorsa.translator.model.ColumnDictionaryConfigHolder;
import com.dorsa.translator.model.FileColumnsHolder;
import com.dorsa.translator.model.RowDictionaryConfigHolder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = {DataLineTranslator.class}
        )
public class DataLineTranslator_translateColumnLine_Test {

    @Autowired
    private DataLineTranslator dataLineTranslator;

    @MockBean
    private RowDictionaryConfigHolder rowDictionaryConfigHolder;

    @MockBean
    private ColumnDictionaryConfigHolder columnDictionaryConfigHolder;

    @MockBean
    private FileColumnsHolder fileColumnsHolder;

    private Map<String , String> getMockColumnDictionary() {
        ConcurrentHashMap<String , String> columnDictionary = new ConcurrentHashMap<>();
        columnDictionary.put("COL1" , "OURID");
        columnDictionary.put("COL3" , "OURCOL3");
        return columnDictionary;
    }

    private Map<String , List<String>> getColumnsByFileName() {
        ConcurrentHashMap<String , List<String>> columnsByFileName = new ConcurrentHashMap<>();
        columnsByFileName.put("file1" , Arrays.asList("COL1" , "COL2" , "COL3" , "COL4"));
        return columnsByFileName;
    }

    @Before
    public void mockDependencies() {
        when(columnDictionaryConfigHolder.getColumnDictionary()).thenReturn(getMockColumnDictionary());
        when(fileColumnsHolder.getColumnsByFileName()).thenReturn(getColumnsByFileName());
    }

    @Test
    public void whenCorrectLine_thenTranslate() throws InvalidLineFormatException, InvalidFileNameException {
        String line = "COL1\tCOL2\tCOL3\tCOL4";
        String translatedLine = dataLineTranslator.translateColumnLine(line, "file1");
        String expectedTranslatedLine = "OURID\tOURCOL3";
        assertEquals(translatedLine , expectedTranslatedLine);
    }

    @Test(expected = InvalidLineFormatException.class)
    public void whenInvalidColSize_thenTrowsException() throws InvalidLineFormatException, InvalidFileNameException {
        String line = "COL1\tCOL2\tCOL3";
        String translatedLine = dataLineTranslator.translateColumnLine(line, "file1");
    }

    @Test(expected = InvalidLineFormatException.class)
    public void whenInvalidColSize_thenTrowsException2() throws InvalidLineFormatException, InvalidFileNameException {
        String line = "COL1";
        String translatedLine = dataLineTranslator.translateColumnLine(line, "file1");
    }

    @Test(expected = InvalidLineFormatException.class)
    public void whenEmptyLine_thenTrowsException2() throws InvalidLineFormatException, InvalidFileNameException {
        String line = "";
        String translatedLine = dataLineTranslator.translateColumnLine(line, "file1");
    }

    @Test(expected = InvalidFileNameException.class)
    public void whenInvalidFileName_thenTrowsInvalidFileName() throws InvalidLineFormatException, InvalidFileNameException {
        String line = "COL1\tCOL2\tCOL3";
        String translatedLine = dataLineTranslator.translateColumnLine(line, "file2");
    }


}
