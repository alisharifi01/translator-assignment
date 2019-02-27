package com.dorsa.translator.biz;

import com.dorsa.translator.model.FileColumnsHolder;
import com.dorsa.translator.model.FileWriterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;

@Component
public class OutputDataFileInitiator {

    @Autowired
    private FileWriterBuilder fileWriterBuilder;

    @Autowired
    private FileColumnsHolder fileColumnsHolder;

    private Logger LOGGER = LoggerFactory.getLogger(OutputDataFileInitiator.class);

    public void initiate(String fileName , String columnLine) {
        try {
            FileWriter fileWriter = fileWriterBuilder.getFileWriter(fileName);
            fileWriter.write(columnLine);
            fileWriter.flush();
        } catch (IOException e) {
            LOGGER.error("ERROR" ,e);
            throw new RuntimeException(e);
        }
    }

}
