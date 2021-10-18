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
public class SimpleFlow2Configuration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /*
* 문의 사항

flow1에서 failed 시점에 flow3으로 흐르도록 했을 때
flow3의 모든 스탭이 성공하여서 제 생각으로는 JobExecution 기록에 EXIT_CODE가 COMPLETED로 기록될 줄 알았는데
JobExecution 기록에 EXIT_CODE 가 FAILED로 기록되어 있습니다.

JobExecution의 EXIT_CODE가 마지막 스탭이나 플로우의 상태값을 반영되는 것으로 인지하고 있었는데 위와 같은 경우와 같이
Job의 과정중 하나의 스탭이라도 실패하게 된다면 EXIT_CODE에는 FAILED로 기록이 되는건가요?
    * */
    @Bean
    public Job simpleFlow2Job() {
        return jobBuilderFactory.get("simpleFlow2Job")
                .start(sf2Flow1())
                    .on("COMPLETED")
                    .to(sf2Flow2())
                .from(sf2Flow1())
                    .on("FAILED")
                    .to(sf2Flow3())
                .end()
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Flow sf2Flow1() {

        final FlowBuilder<Flow> builder = new FlowBuilder<>("sf2Flow1");

        builder.start(sf2Step1())
                .next(sf2Step2())
                .end();

        return builder.build();

    }

    @Bean
    public Flow sf2Flow2() {

        final FlowBuilder<Flow> builder = new FlowBuilder<>("sf2Flow2");

        builder.start(sf2Flow3())
                .next(sf2Step5())
                .next(sf2Step6())
                .end();

        return builder.build();

    }

    @Bean
    public Flow sf2Flow3() {

        final FlowBuilder<Flow> builder = new FlowBuilder<>("sf2Flow3");

        builder.start(sf2Step3())
                .next(sf2Step4())
                .end();

        return builder.build();

    }

    @Bean
    public Step sf2Step1() {
        return stepBuilderFactory.get("sf2Step1")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("SimpleFlowConfiguration.sfStep1");
//                    contribution.setExitStatus(new ExitStatus("A"));
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step sf2Step2() {
        return stepBuilderFactory.get("sf2Step2")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("SimpleFlowConfiguration.sfStep2");
//                    throw new RuntimeException("test");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step sf2Step3() {
        return stepBuilderFactory.get("sf2Step3")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("SimpleFlowConfiguration.sfStep3");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step sf2Step4() {
        return stepBuilderFactory.get("sf2Step4")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("SimpleFlowConfiguration.sfStep4");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step sf2Step5() {
        return stepBuilderFactory.get("sf2Step5")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("SimpleFlowConfiguration.sfStep5");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step sf2Step6() {
        return stepBuilderFactory.get("sf2Step6")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("SimpleFlowConfiguration.sfStep6");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

}
