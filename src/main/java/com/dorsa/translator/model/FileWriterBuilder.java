package com.dorsa.translator.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class FileWriterBuilder {

    @Value("${file.path.dir.output.data}")
    private String OUTPUT_DATA_DIRECTORY_PATH;

    @Value("${out.extension}")
    private String OUT_EXTENSION;

    private final String DOT = ".";

    private Map<String, FileWriter> fileWritersByFileName = new ConcurrentHashMap<>();

    public FileWriter getFileWriter(String fileName) {
        try {
            FileWriter fileWriter = fileWritersByFileName.get(fileName);
            if (fileWriter == null) {
                fileWriter
                        = new FileWriter(
                        OUTPUT_DATA_DIRECTORY_PATH.concat(fileName).concat(DOT).concat(OUT_EXTENSION),
                        true
                );
                fileWritersByFileName.put(fileName, fileWriter);
            }
            return fileWriter;
        } catch (IOException e) {
            //TODO log
            throw new RuntimeException(e);
        }
    }
}
