package me.zeroest.batch_inflearn;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@SpringBootApplication
public class BatchInflearnApplication {

    public static void main(String[] args) {
        SpringApplication.run(BatchInflearnApplication.class, args);
    }

}
