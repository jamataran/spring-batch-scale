package com.arrobaautowired.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.debug("\n======================================================================================================================================================================================================================================================\n\n\n\n\nFinalizó la ejecución del proceso.\nJobExecution: {}\n\n\n\n\n======================================================================================================================================================================================================================================================", jobExecution);
        }
    }
}
