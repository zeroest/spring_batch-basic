package me.zeroest.batch_inflearn.flowJob;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class TransitionJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /*
    * 1. 단계 1을 실행해서 "A"가 나오면 단계 2를 실행하라
    * 2. 단계 2가 성공하면 Step의 결과에 상관없이 작업을 중단하라
    * 3. 단계 1을 실행해서 "A"가 아니면 Step의 결과에 상관없이 단계 3을 실행하라
    * 4. 단계 3이 성공하면 단계 4를 실행하라
    * 5. 단계 4를 실행해서 "B"가 나오면 작업을 종료하라
    * */
    @Bean
    public Job transitionJob() {
        return jobBuilderFactory.get("transitionJob")
                .start(tsStep1())
                    .on("A")
                    .to(tsStep2())
                    .on("*")
                    .stop()
                .from(tsStep1())
                    .on("*")
                    .to(tsStep3())
                    .next(tsStep4())
                    .on("B")
                    .end()
                .end()
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step tsStep1() {
        return stepBuilderFactory.get("tsStep1")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("TransitionJobConfiguration.tsStep1");
//                    contribution.setExitStatus(new ExitStatus("A"));
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step tsStep2() {
        return stepBuilderFactory.get("tsStep2")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("TransitionJobConfiguration.tsStep2");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step tsStep3() {
        return stepBuilderFactory.get("tsStep3")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("TransitionJobConfiguration.tsStep3");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step tsStep4() {
        return stepBuilderFactory.get("tsStep4")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("TransitionJobConfiguration.tsStep4");
                    contribution.setExitStatus(new ExitStatus("B"));
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

}
