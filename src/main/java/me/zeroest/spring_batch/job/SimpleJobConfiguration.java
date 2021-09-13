package me.zeroest.spring_batch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SimpleJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /*
    * schema- 검색후 사용하는 DBMS에 맞춰 테이블을 생성하자
    * */

    @Bean
    public Job simpleJob() {
        return jobBuilderFactory.get("simpleJob")
                .start(simpleStep1(null))
                .next(simpleStep2(null))
                .build();
    }

    @Bean
    @JobScope
    public Step simpleStep1(@Value("#{jobParameters[nowDateTime]}") String nowDateTime) {
        return stepBuilderFactory.get("simpleStep1")
                .tasklet(((contribution, chunkContext) -> {
                    throw new IllegalArgumentException("step1에서 실패");
                }))
                .build();
    }

    @Bean
    @JobScope
    public Step simpleStep2(@Value("#{jobParameters[nowDateTime]}") String nowDateTime) {
        return stepBuilderFactory.get("simpleStep2")
                .tasklet(((contribution, chunkContext) -> {
                    log.info(">>>>> This is Step2");
                    log.error(">>>>> nowDateTime = {}", nowDateTime);
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

}
