package me.zeroest.spring_batch.job.alphabet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

@Slf4j
public class AlphabetWriter implements ItemWriter<String> {
    @Override
    public void write(List<? extends String> items) throws Exception {
        for (String item : items) {
            log.info("write - {}", item);
        }
    }
}
