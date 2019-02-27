package com.dorsa.translator.biz.threadpool;

import com.dorsa.translator.biz.runnable.DataLineConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Manage executor service for DataFIleLineConsumers
 */
@Component
public class DataFileLineConsumerThreadPoolManager {

    @Value("${pool.consumer}")
    private int CONSUMER_COUNT;

    private Logger LOGGER = LoggerFactory.getLogger(DataFileLineConsumerThreadPoolManager.class);
    private List<Future> futures = new ArrayList<>();
    private ExecutorService dataFileConsumersThreadPool;
    private ApplicationContext context;

    @Autowired
    public DataFileLineConsumerThreadPoolManager(ExecutorService dataFileConsumersThreadPool, ApplicationContext context) {
        this.dataFileConsumersThreadPool = dataFileConsumersThreadPool;
        this.context = context;
    }

    public void submitAll() {
        for(int i = 0; i < CONSUMER_COUNT ; i++) {
            DataLineConsumer dataLineConsumer
                    = context.getBean(DataLineConsumer.class);
            futures.add(dataFileConsumersThreadPool.submit(dataLineConsumer));
        }
    }

    /**
     * wait for all task done
     */
    public void waitForFinish() {
        for(Future future : futures){
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                LOGGER.error("ERROR",e);
            }
        }
    }

    public void shutdown(){
        dataFileConsumersThreadPool.shutdown();
    }
}
