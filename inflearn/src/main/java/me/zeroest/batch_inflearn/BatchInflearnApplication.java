package me.zeroest.batch_inflearn;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@EnableBatchProcessing
@SpringBootApplication
public class BatchInflearnApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(BatchInflearnApplication.class, args);
        int exitCode = SpringApplication.exit(context);
        System.exit(exitCode);
    }

}
