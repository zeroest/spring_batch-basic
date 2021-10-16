package me.zeroest.batch_inflearn.flowJob;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class JobExecutionDeciderConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job deciderJob() {
        return jobBuilderFactory.get("deciderJob")
                .start(dcStep1())
                .next(decider())
                .from(decider()).on("ODD").to(dcOddStep())
                .from(decider()).on("EVEN").to(dcEvenStep())
                .end()
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step dcStep1() {
        return stepBuilderFactory.get("dcStep1")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("JobExecutionDeciderConfiguration.dcStep1");
//                    throw new RuntimeException("test");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step dcOddStep() {
        return stepBuilderFactory.get("dcOddStep")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("JobExecutionDeciderConfiguration.dcOddStep");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step dcEvenStep() {
        return stepBuilderFactory.get("dcEvenStep")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("JobExecutionDeciderConfiguration.dcEvenStep");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public JobExecutionDecider decider() {
        return new OddDecider();
    }

    public static class OddDecider implements JobExecutionDecider {

        @Override
        public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {

            final Long id = jobExecution.getJobParameters().getLong("run.id");
            System.out.println("run.id = " + id);

            if(id % 2 == 0){
                return new FlowExecutionStatus("EVEN");
            }else {
                return new FlowExecutionStatus("ODD");
            }

        }

    }

}
