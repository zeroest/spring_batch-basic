package me.zeroest.spring_batch.exit_code;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;

public class SkipCheckingListener extends StepExecutionListenerSupport {
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        String exitCode = stepExecution.getExitStatus().getExitCode();
        if (
                !exitCode.equals(ExitStatus.FAILED.getExitCode()) // Step이 성공적으로 수행되었는지 확인한다.
                && stepExecution.getSkipCount() > 0 // StepExecution의 횟수가 0보다 클경우
        ) {
            return new ExitStatus("COMPLETE WITH SKIPS"); // 커스텀 ExitStatus를 반환한다.
        } else {
            return null;
        }
    }
}
