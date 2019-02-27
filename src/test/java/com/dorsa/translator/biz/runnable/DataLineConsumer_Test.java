package com.dorsa.translator.biz.runnable;

import com.dorsa.translator.biz.DataLineTranslator;
import com.dorsa.translator.biz.exception.IdNotFoundException;
import com.dorsa.translator.biz.exception.InvalidFileNameException;
import com.dorsa.translator.biz.exception.InvalidLineFormatException;
import com.dorsa.translator.biz.queue.LineBlockingQueue;
import com.dorsa.translator.biz.queue.Message;
import com.dorsa.translator.model.FileWriterBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = {DataLineConsumer.class,
                LineBlockingQueue.class}
)
@TestPropertySource({
        "classpath:application.properties"
})
public class DataLineConsumer_Test {

    private FileWriter fileWriter;

    @Value("${file.path.dir.input.data}")
    private String INPUT_DIRECTORY;

    @Autowired
    private DataLineConsumer dataLineConsumer;

    @Autowired
    private LineBlockingQueue lineBlockingQueue;

    @MockBean
    private DataLineTranslator dataLineTranslator;

    @MockBean
    private FileWriterBuilder fileWriterBuilder;

    @Before
    public void setup() throws Exception {
        mockDependencies();
        setupLineBlockingQueue();
    }

    private void setupLineBlockingQueue() throws InterruptedException {
        lineBlockingQueue.init(Arrays.asList("file1"));

        for(int i = 0 ; i < 10 ; i++){
            lineBlockingQueue.get().put(new Message("fake_line" , "file1"));
        }
    }

    private void mockDependencies() throws InvalidLineFormatException, IdNotFoundException, InvalidFileNameException, IOException {
        //dataLIneTranslator
        when(dataLineTranslator.translateRowLine(any(), any())).thenReturn("OUR_LINE");
        //fileWriterBuilder
        File file = new File(INPUT_DIRECTORY + "temp_input.txt");
        fileWriter = new FileWriter(file);
        when(fileWriterBuilder.getFileWriter("file1")).thenReturn(fileWriter);
    }

    @Test
    public void happyScenario() throws Exception {
        new Thread(() -> {
            try {
                Thread.sleep(10000);
                lineBlockingQueue.setProduceDone("file1");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        dataLineConsumer.run();

        File file = new File(INPUT_DIRECTORY + "temp_input.txt");
        List<String> lines = Files.readAllLines(Paths.get(INPUT_DIRECTORY + "temp_input.txt"));
        assert lines.size() == 10;
    }

    @After
    public void clean(){
        new File(INPUT_DIRECTORY + "temp_input.txt").delete();
    }
}
