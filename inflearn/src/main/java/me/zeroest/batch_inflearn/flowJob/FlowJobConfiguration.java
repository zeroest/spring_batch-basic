package me.zeroest.batch_inflearn.flowJob;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class FlowJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job flowJob() {
        return jobBuilderFactory.get("flowJob")
                .start(fjStep1())
                .on("COMPLETED")
                    .to(fjStep3())
                .from(fjStep1())
                .on("FAILED")
                    .to(fjStep2())
                .end()
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step fjStep1() {
        return stepBuilderFactory.get("fjStep1")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("FlowJobConfiguration.fjStep1");
                    throw new RuntimeException("test");
//                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step fjStep2() {
        return stepBuilderFactory.get("fjStep2")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("FlowJobConfiguration.fjStep2");
                        contribution.setExitStatus(ExitStatus.FAILED);
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }

    @Bean
    public Step fjStep3() {
        return stepBuilderFactory.get("fjStep3")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("FlowJobConfiguration.fjStep3");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }

}
