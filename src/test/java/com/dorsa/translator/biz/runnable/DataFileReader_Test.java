package com.dorsa.translator.biz.runnable;

import com.dorsa.translator.biz.DataFileReaderListener;
import org.junit.After;
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

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DataFileReader.class)
@TestPropertySource("classpath:application.properties")
public class DataFileReader_Test {

    private static int lineReceivedMethodCallCount = 0;
    private static int produceDoneCallCount = 0;

    @Value("${file.path.dir.input.data}")
    private String INPUT_DIRECTORY;

    @Autowired
    private DataFileReader dataFileReader;

    @Before
    public void createSampleInput() throws IOException {
        String sampleLines
                = "line1\n" +
                 "line2\n" +
                 "line3\n";
        File file = new File(INPUT_DIRECTORY + "temp_input.txt");
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(sampleLines);
        fileWriter.flush();
    }

    @Test
    public void happyScenario() {
        dataFileReader.addDataFileReaderListener(new DataFileReaderListener() {

            @Override
            public void lineReceived(String line, String fileName, boolean isFirstLine) {
                lineReceivedMethodCallCount++;
            }

            @Override
            public void produceDone(String fileName) {
                produceDoneCallCount++;
            }
        });

        dataFileReader.init(new File(INPUT_DIRECTORY + "temp_input.txt"));

        dataFileReader.run();

        assert lineReceivedMethodCallCount == 3;
        assert produceDoneCallCount == 1;
    }

    @After
    public void clean() {
        new File(INPUT_DIRECTORY + "temp_input.txt").delete();
    }
}
