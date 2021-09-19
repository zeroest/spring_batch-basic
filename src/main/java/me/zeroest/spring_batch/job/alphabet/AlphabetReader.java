package me.zeroest.spring_batch.job.alphabet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class AlphabetReader implements ItemReader<String> {

    private int idx;
    private List<String> alphabets;

    public AlphabetReader() {
        this.idx = 0;
        init();
    }

    private void init() {
        alphabets = new ArrayList<>();
        alphabets.add("a");
        alphabets.add("b");
        alphabets.add("c");
        alphabets.add("d");
        alphabets.add("e");
        alphabets.add("f");
        alphabets.add("g");
        alphabets.add("h");
        alphabets.add("i");
        alphabets.add("j");
    }

    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        String alphabet = null;
        if (idx < alphabets.size()) {
            alphabet = alphabets.get(idx++);
        }
        log.info("Read {} - {}", idx, alphabet);
        return alphabet;
    }
}
