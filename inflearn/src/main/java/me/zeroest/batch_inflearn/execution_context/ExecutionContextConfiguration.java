package me.zeroest.batch_inflearn.execution_context;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@RequiredArgsConstructor
public class ExecutionContextConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final ExecutionContextTasklet1 executionContextTasklet1;
    private final ExecutionContextTasklet2 executionContextTasklet2;
    private final ExecutionContextTasklet3 executionContextTasklet3;
    private final ExecutionContextTasklet4 executionContextTasklet4;

    /*
    * Program arguments: --spring.batch.job.names=executionContextJob
    * */
    @Bean
    public Job executionContextJob() {
        return jobBuilderFactory.get("executionContextJob")
                .start(step1())
                .next(step2())
                .next(step3())
                .next(step4())
                .build();
    }

    @Bean
    @JobScope
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet(executionContextTasklet1)
                .build();
    }

    @Bean
    @JobScope
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet(executionContextTasklet2)
                .build();
    }

    @Bean
    @JobScope
    public Step step3() {
        return stepBuilderFactory.get("step3")
                .tasklet(executionContextTasklet3)
                .build();
    }

    @Bean
    @JobScope
    public Step step4() {
        return stepBuilderFactory.get("step4")
                .tasklet(executionContextTasklet4)
                .build();
    }

}
