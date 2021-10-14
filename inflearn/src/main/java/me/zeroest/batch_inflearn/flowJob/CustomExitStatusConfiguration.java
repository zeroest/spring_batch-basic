package me.zeroest.batch_inflearn.flowJob;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class CustomExitStatusConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job customExitStatusJob() {
        return jobBuilderFactory.get("customExitStatusJob")
                .start(ceStep1())
                    .on("FAILED")
                    .to(ceStep2())
                    .on("PASS")
                    .stop()
                .end()
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step ceStep1() {
        return stepBuilderFactory.get("ceStep1")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("CustomExitStatusConfiguration.ceStep1");
                    contribution.getStepExecution().setExitStatus(ExitStatus.FAILED);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step ceStep2() {
        return stepBuilderFactory.get("ceStep2")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("CustomExitStatusConfiguration.ceStep2");
                    return RepeatStatus.FINISHED;
                })
                .listener(new PassCheckingListener())
                .build();
    }

    private static class PassCheckingListener implements StepExecutionListener {

        @Override
        public void beforeStep(StepExecution stepExecution) {

        }

        @Override
        public ExitStatus afterStep(StepExecution stepExecution) {

            final String exitCode = stepExecution.getExitStatus().getExitCode();

            if(!exitCode.equals(ExitStatus.FAILED.getExitCode())){
                return new ExitStatus("PASS");
            }

            return null;

        }
    }

}
