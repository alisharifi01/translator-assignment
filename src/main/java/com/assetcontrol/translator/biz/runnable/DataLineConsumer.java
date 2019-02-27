package com.assetcontrol.translator.biz.runnable;

import com.assetcontrol.translator.biz.DataLineTranslator;
import com.assetcontrol.translator.biz.exception.IdNotFoundException;
import com.assetcontrol.translator.model.FileWriterBuilder;
import com.assetcontrol.translator.biz.queue.LineBlockingQueue;
import com.assetcontrol.translator.biz.queue.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.io.FileWriter;
import java.util.concurrent.TimeUnit;

/**
 * Responsible for reading messages from queue, according to the fileName label fetch an appropriate fileWrite ,
 * after translation of line , write on the file
 */
@Component
@Scope("prototype")
public class DataLineConsumer implements Runnable {

    private Logger LOGGER = LoggerFactory.getLogger(DataLineConsumer.class);

    @Value("${out.extension}")
    private String OUT_EXTENSION;

    @Value("${file.path.dir.output.data}")
    private String OUTPUT_DATA_DIRECTORY_PATH;

    @Value("${queue.polling.timeout}")
    private int POLLING_TIMEOUT;

    @Autowired
    private LineBlockingQueue lineBlockingQueue;

    @Autowired
    private DataLineTranslator dataLineTranslator;

    @Autowired
    private FileWriterBuilder fileWriterBuilder;

    @Override
    public void run() {

        while (!lineBlockingQueue.isProduceDone() || !lineBlockingQueue.get().isEmpty()) {
            try {
                Message message = lineBlockingQueue.get().poll(POLLING_TIMEOUT , TimeUnit.MILLISECONDS);
                if (message == null) {
                    continue;
                }
                String translatedLine = dataLineTranslator.translateRowLine(message.getLine() , message.getFileName());
                FileWriter fileWriter = fileWriterBuilder.getFileWriter(message.getFileName());
                fileWriter.write(translatedLine.concat("\n"));
                fileWriter.flush();

            } catch (IdNotFoundException e){
                continue;
            } catch (Exception e) {
                LOGGER.error("ERROR" , e);
            }
        }

        LOGGER.info("consume done " + Thread.currentThread());
    }

}
