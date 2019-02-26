package com.dorsa.translator.biz;

import com.dorsa.translator.biz.exception.InvalidFileNameException;
import com.dorsa.translator.biz.exception.InvalidLineFormatException;
import com.dorsa.translator.model.FileColumnsHolder;
import com.dorsa.translator.biz.queue.LineBlockingQueue;
import com.dorsa.translator.biz.queue.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;

/**
 * When a line has been read by dataReader notify DataFileParse to parse
 * When dataReader has finished with reading notify DataFileParse update lineBlockingQueue
 * Initiate Output file (parsing and writing on file) for the first line
 * Put in queue for the other lines
 */
@Component
public class DataLineBroker implements DataFileReaderListener {

    private Logger LOGGER = LoggerFactory.getLogger(DataLineBroker.class);

    @Autowired
    private DataLineTranslator dataLineTranslator;

    @Autowired
    private OutputDataFileInitiator outputDataFileInitiator;

    @Autowired
    private LineBlockingQueue lineBlockingQueue;

    @Autowired
    private FileColumnsHolder fileColumnsHolder;

    @Override
    public void lineReceived(String line, String fileName, boolean isFirstLine) {
        if (isFirstLine) {
            parseColumnLine(line, fileName);
        } else {
            putInQueue(line, fileName);
        }
    }

    @Override
    public void produceDone(String fileName) {
        lineBlockingQueue.setProduceDone(fileName);
    }

    private void putInQueue(String line, String fileName) {
        try {
            lineBlockingQueue.get().put(new Message(line, fileName));
        } catch (InterruptedException e) {
            LOGGER.error("ERROR", e);
            throw new RuntimeException(e);
        }
    }

    private void parseColumnLine(String line, String fileName) {
        try {
            extractColumns(line, fileName);
            String translatedColumnLine = dataLineTranslator.translateColumnLine(line, fileName);
            outputDataFileInitiator.initiate(fileName, translatedColumnLine + "\n");
        } catch (InvalidLineFormatException | InvalidFileNameException e) {
            throw new RuntimeException(e);
        }
    }

    private void extractColumns(String columnLine, String fileName) {
        fileColumnsHolder.getColumnsByFileName().putIfAbsent(fileName, new ArrayList<>());
        for (String column : columnLine.split("\\t")) {
            fileColumnsHolder.getColumnsByFileName().get(fileName).add(column);
        }
    }
}
