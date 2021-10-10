package me.zeroest.batch_inflearn.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JobRepositoryListener implements JobExecutionListener {

    private final JobRepository jobRepository;

    @Override
    public void beforeJob(JobExecution jobExecution) {

    }

    @Override
    public void afterJob(JobExecution jobExecution) {

        final String jobName = jobExecution.getJobInstance().getJobName();
        System.out.println("jobName = " + jobName);
        final JobParameters jobParameters = new JobParametersBuilder()
                .addString("requestDate", "202110101708")
                .toJobParameters();

        final JobExecution lastJobExecution = jobRepository.getLastJobExecution(jobName, jobParameters);
        if (lastJobExecution != null) {
            for (StepExecution execution : lastJobExecution.getStepExecutions()){
                final BatchStatus status = execution.getStatus();
                System.out.println("status = " + status);
                final ExitStatus exitStatus = execution.getExitStatus();
                System.out.println("exitStatus = " + exitStatus);
                final String stepName = execution.getStepName();
                System.out.println("stepName = " + stepName);
            }
        }

    }
}
