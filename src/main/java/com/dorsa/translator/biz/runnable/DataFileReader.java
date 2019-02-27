package com.dorsa.translator.biz.runnable;

import com.dorsa.translator.biz.*;
import com.dorsa.translator.util.FileNameUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Responsible for read the input file and notify listeners(DataLineBroker) when every line read
 * and notify when produce has been done
 */
@Component
@Scope("prototype")
public class DataFileReader implements Runnable {

    private static Logger LOGGER = LoggerFactory.getLogger(DataFileReader.class);
    private File file;
    private String fileName;
    private BufferedReader bufferedReader;
    private List<DataFileReaderListener> dataFileReaderListeners = new ArrayList<>();

    private void setFile(File file) {
        this.file = file;
    }

    public void addDataFileReaderListener(DataFileReaderListener dataFileReaderListener) {
        this.dataFileReaderListeners.add(dataFileReaderListener);
    }

    public void init(File file) {
        try {
            setFile(file);
            bufferedReader
                    = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            LOGGER.error("ERROR", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            fileName = FileNameUtil.eliminateExtension(file.getName());
            LOGGER.info("reading file has been started | fileName = " + fileName);
            readFirstLine();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                notifyDataLineListeners(line , fileName , false);
            }
            notifyDataLineListenersToProduceDone();
            LOGGER.info("read complete | fileName = " + file.getName());
        } catch (Exception e){
            LOGGER.error("ERROR", e);
            throw new RuntimeException(e);
        }
    }

    private void notifyDataLineListenersToProduceDone() {
        for (DataFileReaderListener dataFileReaderListener : dataFileReaderListeners) {
            dataFileReaderListener.produceDone(fileName);
        }
    }

    private void readFirstLine() {
        try {
            String line = bufferedReader.readLine();
            notifyDataLineListeners(line , fileName , true);
        } catch (IOException e) {
            LOGGER.error("ERROR" , e);
            throw new RuntimeException(e);
        }

    }

    private void notifyDataLineListeners(String line, String fileName, boolean isFirstLine) {
        dataFileReaderListeners.forEach(listener -> listener.lineReceived(line,fileName,isFirstLine));
    }
}
