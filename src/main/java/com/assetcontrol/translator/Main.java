package com.assetcontrol.translator;

import com.assetcontrol.translator.biz.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main
        implements CommandLineRunner {

    @Autowired
    private Application application;

    private static Logger LOGGER = LoggerFactory
            .getLogger(Main.class);

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) {
        try {
            application.start();
        } catch (Exception e) {
            LOGGER.error("ERROR", e);
            throw e;
        }
    }

}