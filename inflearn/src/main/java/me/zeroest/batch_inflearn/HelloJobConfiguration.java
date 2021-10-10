package me.zeroest.batch_inflearn;

import lombok.RequiredArgsConstructor;
import me.zeroest.batch_inflearn.validator.CustomJobParametersValidator;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Date;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class HelloJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final JobExecutionListener jobExecutionListener;

    /*
     * Program arguments: --spring.batch.job.names=executionContextJob
     * */
    @Primary
    @Bean
    public Job helloJob() {
        return jobBuilderFactory.get("helloJob")
                .start(helloStep1())
                .next(helloStep2())
//                .listener(jobExecutionListener)
//                .validator(new CustomJobParametersValidator())
                .validator(new DefaultJobParametersValidator(
                        new String[]{"name", "date"},
                        new String[]{"count"}
                ))
                .build();
    }
/*
    @Primary
    @Bean
    public Job helloJob() {
        return jobBuilderFactory.get("helloJob")
                .start(flow())
                .next(helloStep5())
                .end()
                .build();
    }
*/

    @Bean
    public Step helloStep1() {
        return stepBuilderFactory.get("helloStep1")
                .tasklet((contribution, chunkContext) -> {

                    final JobParameters jobParameters = contribution.getStepExecution().getJobExecution().getJobParameters();
                    final String name = jobParameters.getString("name");
                    System.out.println("name = " + name);
                    final Long seq = jobParameters.getLong("seq");
                    System.out.println("seq = " + seq);
                    final Date date = jobParameters.getDate("date");
                    System.out.println("date = " + date);
                    final Double age = jobParameters.getDouble("age");
                    System.out.println("age = " + age);


                    System.out.println("Hello Spring Batch 1");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step helloStep2() {
        return stepBuilderFactory.get("helloStep2")
                .tasklet((contribution, chunkContext) -> {

                    final Map<String, Object> jobParameters = chunkContext.getStepContext().getJobParameters();

                    Thread.sleep(5000L);

                    System.out.println("Hello Spring Batch 2");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Flow flow() {
        return new FlowBuilder<Flow>("flow")
                .start(helloStep3())
                .next(helloStep4())
                .end();
    }

    @Bean
    @JobScope
    public Step helloStep3() {
        return stepBuilderFactory.get("step3")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("HelloJobConfiguration.step3");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    @JobScope
    public Step helloStep4() {
        return stepBuilderFactory.get("step4")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("HelloJobConfiguration.step4");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    @JobScope
    public Step helloStep5() {
        return stepBuilderFactory.get("step5")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("HelloJobConfiguration.step5");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

}
