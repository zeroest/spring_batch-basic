package me.zeroest.batch_inflearn.flowJob;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class SimpleFlowConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job simpleFlowJob() {
        return jobBuilderFactory.get("simpleFlowJob")
                .start(sfFlow())
                .next(sfStep3())
                .end()
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Flow sfFlow() {

        final FlowBuilder<Flow> builder = new FlowBuilder<>("sfFlow");

        builder.start(sfStep1())
                .next(sfStep2())
                .end();

        return builder.build();

    }

    @Bean
    public Step sfStep1() {
        return stepBuilderFactory.get("sfStep1")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("SimpleFlowConfiguration.sfStep1");
//                    contribution.setExitStatus(new ExitStatus("A"));
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step sfStep2() {
        return stepBuilderFactory.get("sfStep2")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("SimpleFlowConfiguration.sfStep2");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step sfStep3() {
        return stepBuilderFactory.get("sfStep3")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("SimpleFlowConfiguration.sfStep3");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

}
