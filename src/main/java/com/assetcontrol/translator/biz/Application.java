package com.assetcontrol.translator.biz;

import com.assetcontrol.translator.biz.queue.LineBlockingQueue;
import com.assetcontrol.translator.biz.threadpool.DataFileLineConsumerThreadPoolManager;
import com.assetcontrol.translator.biz.threadpool.DataFileReaderThreadPoolManager;
import com.assetcontrol.translator.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class Application {

    private Logger LOGGER = LoggerFactory.getLogger(Application.class);

    @Value("${pool.consumer.count}")
    private int dataFileConsumerPoolCount;

    @Value("${file.path.dir.output.data}")
    private String OUTPUT_DIRECTORY;

    @Autowired
    private ConfigFilesParser configFilesParser;

    @Autowired
    private DataFileReaderThreadPoolManager dataFileReaderThreadPoolManager;

    @Autowired
    private DataFileLineConsumerThreadPoolManager dataFileLineConsumerThreadPoolManager;

    @Value("${pool.data.file.parser}")
    private int dataFileParserPoolCount;

    @Autowired
    private LineBlockingQueue lineBlockingQueue;

    @Autowired
    private FileUtil fileUtil;

    public void start() {
        LOGGER.info("START");
        long start = new Date().getTime();
        cleanOutput();
        File[] inputFiles = fileUtil.getInputFiles();
        initQueue(inputFiles);
        configFilesParser.parseFiles();
        dataFileReaderThreadPoolManager.submitAll(inputFiles);
        dataFileLineConsumerThreadPoolManager.submitAll();
        dataFileLineConsumerThreadPoolManager.waitForFinish();
        dataFileReaderThreadPoolManager.shutdown();
        dataFileLineConsumerThreadPoolManager.shutdown();
        long end = new Date().getTime();
        LOGGER.info("END | time = " + (end - start));
    }

    private void initQueue(File[] inputFiles) {
        lineBlockingQueue.init(Arrays.stream(inputFiles).map(File::getName).collect(Collectors.toList()));
    }

    public void cleanOutput(){
        File folder = new File(OUTPUT_DIRECTORY);
        for(File file : folder.listFiles()) {
            file.delete();
        }
    }

}
