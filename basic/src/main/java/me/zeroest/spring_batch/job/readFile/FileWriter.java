package me.zeroest.spring_batch.job.readFile;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
public class FileWriter implements ItemWriter<String> {
    @Override
    public void write(List<? extends String> paths) throws Exception {
        for (String filePath : paths) {
            log.info("filePath = {}", filePath);

            try (Stream<String> lines = Files.lines(Paths.get(filePath))) {

                lines.forEach(System.out::println);

            } catch (IOException e) {
                throw e;
            }
        }
    }
}
