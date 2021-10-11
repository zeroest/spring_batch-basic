package me.zeroest.batch_inflearn.stepBuilder;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.*;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Configuration
@RequiredArgsConstructor
public class StepBuilderConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job stepBuilderJob() {
        return jobBuilderFactory.get("stepBuilderJob")
                .start(sbjStep1())
                .next(sbjStep2())
//                .next(sbjStep3())
//                .next(sbjStep4())
                .build();
    }

    @Bean
    public Step sbjStep1() {
        return stepBuilderFactory.get("sbjStep1")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("StepBuilderConfiguration.execute");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }

    @Bean
    public Step sbjStep2() {
        return stepBuilderFactory.get("sbjStep2")
                .<String, String> chunk(3)
/*
                .reader(new ItemReader<String>() {
                    @Override
                    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
                        return null;
                    }
                })
*/
                .reader(new ListItemReader<>(Arrays.asList("item1", "item2", "item3", "item4", "item5")))
                .processor(new ItemProcessor<String, String>() {
                    @Override
                    public String process(String item) throws Exception {
                        System.out.println("StepBuilderConfiguration.process");
                        return item.toUpperCase();
                    }
                })
                .writer(new ItemWriter<String>() {
                    @Override
                    public void write(List<? extends String> items) throws Exception {
                        System.out.println("StepBuilderConfiguration.write");
                        items.forEach(System.out::println);
                    }
                })
                .build();
    }

    @Bean
    public Step sbjStep3() {
        return stepBuilderFactory.get("sbjStep3")
                .partitioner(sbjStep1())
                .gridSize(2)
                .build();
    }

    @Bean
    public Step sbjStep4() {
        return stepBuilderFactory.get("sbjStep4")
                .job(sbjJob())
                .build();
    }

    @Bean
    public Job sbjJob() {
        return jobBuilderFactory.get("subJob")
                .start(sbjStep5())
                .build();
    }

    @Bean
    public Step sbjStep5() {
        return stepBuilderFactory.get("sbjStep5")
                .flow(sbjFlow())
                .build();
    }

    @Bean
    public Flow sbjFlow() {
        final FlowBuilder<Flow> sbjFlow = new FlowBuilder<>("sbjFlow");
        sbjFlow.start(sbjStep2())
                .end();
        return sbjFlow.build();
    }

}
