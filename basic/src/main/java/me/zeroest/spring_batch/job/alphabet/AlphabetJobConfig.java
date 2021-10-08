package me.zeroest.spring_batch.job.alphabet;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class AlphabetJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean(name = "alphabetJob")
    public Job alphabetJob() {
        return jobBuilderFactory.get("alphabetJob")
                .incrementer(new RunIdIncrementer())
                .start(alphabetStep())
                .build();
    }

    @Bean
    public Step alphabetStep() {
        return stepBuilderFactory.get("alphabetStep")
                .<String, String> chunk(5)
                .reader(new AlphabetReader())
                .processor(new UpperCaseProcessor())
                .writer(new AlphabetWriter())
                .build();
    }

}
