package com.dorsa.translator.biz.threadpool;

import com.dorsa.translator.biz.DataLineBroker;
import com.dorsa.translator.biz.runnable.DataFileReader;
import com.dorsa.translator.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import java.io.File;
import java.util.concurrent.ExecutorService;

/**
 * Manage executor service for DataFileReaders
 */
@Component
public class DataFileReaderThreadPoolManager {

    @Value("${file.path.dir.input.data}")
    private String DATA_INPUT_DIRECTORY;

    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private ExecutorService dataFileParsersThreadPool;

    @Autowired
    private DataLineBroker dataLineBroker;

    @Autowired
    private ApplicationContext context;

    public void submitAll(File[] inputFiles) {
        for(File file : inputFiles) {
            DataFileReader dataFileReader = context.getBean(DataFileReader.class);
            dataFileReader.init(file);
            dataFileReader.addDataFileReaderListener(dataLineBroker);
            dataFileParsersThreadPool.submit(dataFileReader);
        }
    }

    public void shutdown() {
        dataFileParsersThreadPool.shutdown();
    }

}
