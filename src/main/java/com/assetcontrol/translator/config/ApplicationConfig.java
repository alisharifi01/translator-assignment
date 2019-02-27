package com.assetcontrol.translator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@PropertySource({
        "classpath:application.properties"
})
public class ApplicationConfig {

    @Value("${pool.file.reader}")
    private int dataFileParserPoolCount;

    @Value("${pool.consumer}")
    private int dataFileConsumerPoolCount;

    @Bean("dataFileParsersThreadPool")
    public ExecutorService dataFileParsersThreadPool() {
        return Executors.newFixedThreadPool(dataFileParserPoolCount);
    }

    @Bean("dataFileConsumersThreadPool")
    public ExecutorService dataFileConsumersThreadPool() {
        return Executors.newFixedThreadPool(dataFileConsumerPoolCount);
    }

}
