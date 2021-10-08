package me.zeroest.spring_batch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class ScopeJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job scopeJob() {
        return jobBuilderFactory.get("scopeJob")
                .start(scopeStep1(null)) // null할당 이는 Job Parameter의 할당이 어플리케이션 실행시에 하지 않기 때문
                .next(scopeStep2())
                .build();
    }

    @Bean
    @JobScope
    public Step scopeStep1(@Value("#{jobParameters[nowDateTime]}") String nowDateTime) {
        return stepBuilderFactory.get("scopeStep1")
                .tasklet(((contribution, chunkContext) -> {
                    log.info(">>>>> This is Step1");
                    log.error(">>>>> nowDateTime = {}", nowDateTime);
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

    private final ErrorJobTasklet errorJobTasklet;

    @Bean
    public Step scopeStep2() {
        return stepBuilderFactory.get("scopeStep2")
                .tasklet(errorJobTasklet) // null할당 이는 Job Parameter의 할당이 어플리케이션 실행시에 하지 않기 때문
                .build();
    }

    @Bean
    @StepScope
    public Tasklet scopeStep2Tasklet(@Value("#{jobParameters[requestDate]}") String requestDate){
        return (contribution, chunkContext) -> {
            log.info(">>>> This is scopeStep2");
            log.info(">>>> requestDate = {}", requestDate);
            return RepeatStatus.FINISHED;
        };
    }

}

@Slf4j
@Component
@StepScope
class ErrorJobTasklet implements Tasklet {

    @Value("#{jobParameters[requestDate]}")
    private String requestDate;

    public ErrorJobTasklet() {
        log.info(">>>> ErrorJobTasklet 생성");
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info(">>>> ErrorJobTasklet");
        log.info(">>>> requestDate = {}", requestDate);
        return RepeatStatus.FINISHED;
    }
}