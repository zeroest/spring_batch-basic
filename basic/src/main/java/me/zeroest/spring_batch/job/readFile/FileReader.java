package me.zeroest.spring_batch.job.readFile;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

@Slf4j
public class FileReader implements ItemReader<String> {

    private int count = 0;

    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if(count < ReadFileJobConfig.FILES.length){
            String filePath = ReadFileJobConfig.FILES[count++];
            log.info("##### fileReader  filePath - {}", filePath);
            return filePath;
        }else{
            count = 0;
        }
        return null;
    }
}
