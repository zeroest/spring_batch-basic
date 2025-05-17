package me.zeroest.batch_inflearn;

import me.zeroest.batch_inflearn.listener.JobRepositoryListener;
import org.junit.After;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBatchTest
@SpringBootTest(classes = {TestBatchConfig.class, HelloJobConfiguration.class, JobRepositoryListener.class})
class HelloJobConfigurationTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void helloJobTest() throws Exception {
        // given
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("name", "user1")
                .addDate("date", new Date())
                .toJobParameters();

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        // then
        assertEquals(jobExecution.getStatus(), BatchStatus.COMPLETED);
        assertEquals(jobExecution.getExitStatus(), ExitStatus.COMPLETED);
    }

    @Test
    void helloStep1Test() {
        // given
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("name", "step1")
                .addDouble("age", 30.0)
                .toJobParameters();

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchStep("helloStep1", jobParameters);
        StepExecution stepExecution = ((List<StepExecution>) jobExecution.getStepExecutions()).get(0);

        assertEquals(1, stepExecution.getCommitCount());
        assertEquals(0, stepExecution.getReadCount());
        assertEquals(0, stepExecution.getWriteCount());
    }

    @After
    void clear() {
        System.out.println("Clear test data");
//        jdbcTemplate.execute("delete from customer");
    }

}