package me.zeroest.batch_inflearn.execution_context;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class ExecutionContextTasklet2 implements Tasklet {
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        System.out.println("ExecutionContextTasklet2.execute");

        final ExecutionContext jobExecutionContext = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
        final ExecutionContext stepExecutionContext = chunkContext.getStepContext().getStepExecution().getExecutionContext();

        System.out.println("jobName: " + jobExecutionContext.get("jobName"));
        System.out.println("stepName: " + stepExecutionContext.get("stepName"));

        if(stepExecutionContext.get("stepName") == null){
            stepExecutionContext.put("stepName", chunkContext.getStepContext().getStepExecution().getStepName());
        }

        return RepeatStatus.FINISHED;
    }
}
