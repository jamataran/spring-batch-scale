package com.arrobaautowired;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class SlaveApplication {

    public static void main(String[] args) {
        SpringApplication.run(SlaveApplication.class, args);
    }

}
