package me.zeroest.batch_inflearn.execution_context;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class ExecutionContextTasklet1 implements Tasklet {
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        System.out.println("ExecutionContextTasklet1.execute");

        final ExecutionContext jobExecutionContext = contribution.getStepExecution().getJobExecution().getExecutionContext();
        final ExecutionContext stepExecutionContext = contribution.getStepExecution().getExecutionContext();

        final String jobName = chunkContext.getStepContext().getStepExecution().getJobExecution().getJobInstance().getJobName();
        final String stepName = chunkContext.getStepContext().getStepExecution().getStepName();

        if(jobExecutionContext.get("jobName") == null){
            jobExecutionContext.put("jobName", jobName);
        }
        if(stepExecutionContext.get("stepName") == null){
            stepExecutionContext.put("stepName", stepName);
        }

        System.out.println("jobName: " + jobExecutionContext.get("jobName"));
        System.out.println("stepName: " + stepExecutionContext.get("stepName"));

        return RepeatStatus.FINISHED;
    }
}
