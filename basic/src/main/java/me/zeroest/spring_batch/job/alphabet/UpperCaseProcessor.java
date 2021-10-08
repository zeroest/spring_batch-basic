package me.zeroest.spring_batch.job.alphabet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class UpperCaseProcessor implements ItemProcessor<String, String> {
    @Override
    public String process(String item) throws Exception {
        String result = item.toUpperCase();
        log.info("process - {}", result);
        return result;
    }
}
