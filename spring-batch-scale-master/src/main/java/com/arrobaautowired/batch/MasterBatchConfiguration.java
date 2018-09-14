package com.arrobaautowired.batch;

import com.arrobaautowired.domain.Record;
import com.arrobaautowired.write.RecordItemWritter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@EnableBatchProcessing
public class MasterBatchConfiguration {

    private final static String MASTER_JOB_TEST = "JOB_MASTER";
    private final static String MATER_JOB_STEP = "STEP-1";
    private final static int CHUNK_SIZE = 100;

    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;
    private MultiResourceItemReader<Record> filesReader;
    private RecordItemWritter recordItemWritter;


    @Autowired
    public MasterBatchConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, MultiResourceItemReader<Record> filesReader, RecordItemWritter recordItemWritter) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.filesReader = filesReader;
        this.recordItemWritter = recordItemWritter;
    }

    @Bean
    public Job processRecordsJob(JobCompletionNotificationListener listener, Step step1) {
        return jobBuilderFactory
                .get(MASTER_JOB_TEST)
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get(MATER_JOB_STEP)
                .<Record, Record>chunk(CHUNK_SIZE)
                .reader(filesReader)
                .writer(recordItemWritter)
                .build();
    }


}
