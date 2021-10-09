package me.zeroest.batch_inflearn;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class HelloJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job helloJob() {
        return jobBuilderFactory.get("helloJob")
                .start(helloStep1())
                .next(helloStep2())
                .build();
    }

    @Bean
    public Step helloStep1() {
        return stepBuilderFactory.get("helloStep1")
                .tasklet((contribution, chunkContext) -> {

                    final JobParameters jobParameters = contribution.getStepExecution().getJobExecution().getJobParameters();
                    final String name = jobParameters.getString("name");
                    System.out.println("name = " + name);
                    final Long seq = jobParameters.getLong("seq");
                    System.out.println("seq = " + seq);
                    final Date date = jobParameters.getDate("date");
                    System.out.println("date = " + date);
                    final Double age = jobParameters.getDouble("age");
                    System.out.println("age = " + age);


                    System.out.println("Hello Spring Batch 1");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step helloStep2() {
        return stepBuilderFactory.get("helloStep2")
                .tasklet((contribution, chunkContext) -> {

                    final Map<String, Object> jobParameters = chunkContext.getStepContext().getJobParameters();

                    System.out.println("Hello Spring Batch 2");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

}
