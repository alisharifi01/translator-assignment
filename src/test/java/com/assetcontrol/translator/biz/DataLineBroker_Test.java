package com.assetcontrol.translator.biz;

import com.assetcontrol.translator.biz.exception.InvalidFileNameException;
import com.assetcontrol.translator.biz.exception.InvalidLineFormatException;
import com.assetcontrol.translator.biz.queue.LineBlockingQueue;
import com.assetcontrol.translator.biz.queue.Message;
import com.assetcontrol.translator.model.FileColumnsHolder;
import com.assetcontrol.translator.model.FileWriterBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@TestPropertySource("classpath:application.properties")
@SpringBootTest(classes = {
        DataLineBroker.class,
        LineBlockingQueue.class,
        FileColumnsHolder.class
})
public class DataLineBroker_Test {

    private String SAMPLE_TRANSLATED_COLUMN_LINE = "OURID\tOURCOL2";

    @Autowired
    private DataLineBroker dataLineBroker;

    @Autowired
    private LineBlockingQueue lineBlockingQueue;

    @Autowired
    private FileColumnsHolder fileColumnsHolder;

    @MockBean
    private OutputDataFileInitiator outputDataFileInitiator;

    @MockBean
    private FileWriterBuilder fileWriterBuilder;

    @MockBean
    private DataLineTranslator dataLineTranslator;

    @Before
    public void mockDependencies() throws InvalidFileNameException, InvalidLineFormatException, IOException {
        when(dataLineTranslator.translateColumnLine(any(), any())).thenReturn(SAMPLE_TRANSLATED_COLUMN_LINE);
        when(fileWriterBuilder.getFileWriter(any())).thenReturn(null);
    }

    @Test
    public void whenReceivedRowLine_thenMessageInQueue() {
        dataLineBroker.lineReceived("sample_row_line", "file1", false);
        Message message = lineBlockingQueue.get().poll();
        assert message.getLine().equals("sample_row_line");
    }

    @Test
    public void whenReceiveFirstLine_thenMessageInQueue() {
        dataLineBroker.lineReceived("sample_column_line", "file1", true);
        Map<String , List<String>> expectedColumnByFileName = new HashMap<>();
        expectedColumnByFileName.put("file1" , Arrays.asList("sample_column_line"));
        assertEquals(expectedColumnByFileName , fileColumnsHolder.getColumnsByFileName());
    }
}
