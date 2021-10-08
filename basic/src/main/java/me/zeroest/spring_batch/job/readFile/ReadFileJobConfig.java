package me.zeroest.spring_batch.job.readFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class ReadFileJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public static final String[] FILES = {"/tmp/test/file_read1.txt", "/tmp/test/file_read2.txt", "/tmp/test/file_read3.txt", "/tmp/test/file_read4.txt", };

    @Bean
    public Job readFileJob() {
        return jobBuilderFactory.get("readFileJob")
                .incrementer(new RunIdIncrementer())
                .start(readFileStep1())
                .next(readFileStep2())
                .build();
    }

    @Bean
    public Step readFileStep1() {
        return stepBuilderFactory.get("readFileStep1")
                .<String, String> chunk(2)
                .reader(new FileReader())
                .writer(new FileWriter())
                .build();
    }

    @Bean
    public Step readFileStep2() {
        return stepBuilderFactory.get("readFileStep2")
                .tasklet(((contribution, chunkContext) -> {
                    try {
                        for (String filePath : FILES) {
                            File file = new File(filePath);
                            if (file.delete()) {
                                log.info("############ readFileStep2 : delete success {}", file.getName());
                            } else {
                                log.info("############ readFileStep2 : delete fail {}", file.getName());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

}
