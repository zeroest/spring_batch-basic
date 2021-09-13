package me.zeroest.spring_batch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class StepNextJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job stepNextJob() {
        return jobBuilderFactory.get("stepNextConditionalJob")
                .start(conditionalJobStep1())
                    .on("FAILED") // 실패했을경우
                    .to(conditionalJobStep3()) // step3으로 이동
                    .on("*") // step3의 결과 관계 없이
                    .end() // flow가 종료된다.
                .from(conditionalJobStep1()) // 다시 step1에서 부터
                    .on("*") // FAILED 외의 모든 경우에
                    .to(conditionalJobStep2()) // step2로 이동
                    .next(conditionalJobStep3()) // step2가 정상 종료시 step3로 이동
                    .on("*") // step3 결과 상관없이
                    .end() // flow가 종료된다.
                .end() // Job이 종료된다.
                .build();
    }

    @Bean
    public Step conditionalJobStep1() {
        return stepBuilderFactory.get("step1")
                .tasklet(((contribution, chunkContext) -> {
                    log.info(">>>>> This is Step1");

                    /*
                    * ExitStatus를 FAILED로 지정한다.
                    * 해당 status를 보고 flow가 진행된다.
                    * */
//                    contribution.setExitStatus(ExitStatus.FAILED);

                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

    @Bean
    public Step conditionalJobStep2() {
        return stepBuilderFactory.get("step2")
                .tasklet(((contribution, chunkContext) -> {
                    log.info(">>>>> This is Step2");
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

    @Bean
    public Step conditionalJobStep3() {
        return stepBuilderFactory.get("step3")
                .tasklet(((contribution, chunkContext) -> {
                    log.info(">>>>> This is Step3");
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

}
