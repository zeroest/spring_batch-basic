package me.zeroest.spring_batch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DeciderJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job deciderJob() {
        return jobBuilderFactory.get("deciderJob")
                .start(startStep())
                .next(decider()) // 홀수 짝수 구분
                .from(decider()) // decider에서 나온 숫자가
                    .on("ODD") // 홀수라면
                    .to(oddStep()) // oddStep으로 보낸다.
                .from(decider()) // 다시 decider에서 나온 숫자가
                    .on("EVEN") // 짝수라면
                    .to(evenStep()) // evenStep으로 보낸다.
                .end()
                .build();

    }

    @Bean
    public Step startStep() {
        return stepBuilderFactory.get("startStep")
                .tasklet(((contribution, chunkContext) -> {
                    log.info(">>>> start");
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

    @Bean
    public Step evenStep() {
        return stepBuilderFactory.get("evenStep")
                .tasklet(((contribution, chunkContext) -> {
                    log.info(">>>> 짝수");
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

    @Bean
    public Step oddStep() {
        return stepBuilderFactory.get("oddStep")
                .tasklet(((contribution, chunkContext) -> {
                    log.info(">>>> 홀수");
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

    @Bean
    public JobExecutionDecider decider() {
        return new OddDecider();
    }

    public static class OddDecider implements JobExecutionDecider {
        @Override
        public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
            Random rand = new Random();

            int randNum = rand.nextInt(50) + 1;
            log.info("Random Number: {}", randNum);

            if (randNum % 2 == 0) {
                return new FlowExecutionStatus("EVEN");
            } else {
                return new FlowExecutionStatus("ODD");
            }
        }
    }
}
