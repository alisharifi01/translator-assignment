package com.dorsa.translator.biz.queue;

import com.dorsa.translator.util.FileNameUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * hold a blocking queue
 * hold status of producing per file
 */
@Component
public class LineBlockingQueue {

    @Value("${queue.size}")
    private int queueSize;

    private Map<String ,Boolean> isProduceDonePerFile = new ConcurrentHashMap<>();

    private boolean isProduceDone = false;

    private BlockingQueue<Message> queue;

    public void init(List<String> fileNames) {
        for(String fileName : fileNames) {
            isProduceDonePerFile.put(FileNameUtil.eliminateExtension(fileName) , false);
        }
    }

    public BlockingQueue<Message> get(){
        if(queue == null) {
            queue = new LinkedBlockingDeque<>(queueSize);
        }
        return queue;
    }

    public boolean isProduceDone() {
        return isProduceDone;
    }

    public void setProduceDone(String fileName) {
        isProduceDonePerFile.put(fileName , true);
        changeIsProduceDoneState();
    }

    private void changeIsProduceDoneState() {
        AtomicBoolean newStatusForProduceDone = new AtomicBoolean(true);
        isProduceDonePerFile.values().forEach(isProduceDone -> {
            if(!isProduceDone) {
                newStatusForProduceDone.set(false);
            }
        });
        isProduceDone = newStatusForProduceDone.get();
    }
}
