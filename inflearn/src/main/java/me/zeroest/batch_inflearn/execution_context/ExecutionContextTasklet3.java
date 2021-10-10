package me.zeroest.batch_inflearn.execution_context;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class ExecutionContextTasklet3 implements Tasklet {
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        System.out.println("ExecutionContextTasklet3.execute");

        final ExecutionContext jobExecutionContext = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();

        final Object name = jobExecutionContext.get("name");

        if (name == null) {
            jobExecutionContext.put("name", "user1");
            throw new RuntimeException("step3 was failed");
        }

        return RepeatStatus.FINISHED;
    }
}
