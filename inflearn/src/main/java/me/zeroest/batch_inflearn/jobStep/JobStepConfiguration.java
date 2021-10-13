package me.zeroest.batch_inflearn.jobStep;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.step.job.DefaultJobParametersExtractor;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class JobStepConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job parentJob() {
        return jobBuilderFactory.get("parentJob")
                .start(jobStep(null))
                .next(jsStep2())
                .build();
    }

    @Bean
    public Step jobStep(JobLauncher jobLauncher) {
        return stepBuilderFactory.get("jobStep")
                .job(childJob())
                .launcher(jobLauncher)
                .parametersExtractor(jobParametersExtractor())
                .listener(new StepExecutionListener() {
                    @Override
                    public void beforeStep(StepExecution stepExecution) {
                        stepExecution.getExecutionContext().putString("name", "user1");
                    }

                    @Override
                    public ExitStatus afterStep(StepExecution stepExecution) {
                        return null;
                    }
                })
                .build();
    }

    @Bean
    public Job childJob() {
        return jobBuilderFactory.get("childJob")
                .start(jsStep1())
                .build();
    }

    @Bean
    public Step jsStep1() {
        return stepBuilderFactory.get("jsStep1")
                .tasklet(((contribution, chunkContext) -> {
                    throw new RuntimeException("step1 was failed");
//                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

    @Bean
    public Step jsStep2() {
        return stepBuilderFactory.get("jsStep2")
                .tasklet(((contribution, chunkContext) -> RepeatStatus.FINISHED))
                .build();
    }

    private DefaultJobParametersExtractor jobParametersExtractor() {
        final DefaultJobParametersExtractor extractor = new DefaultJobParametersExtractor();
        extractor.setKeys(new String[]{"name"});
        return extractor;
    }

}
