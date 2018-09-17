package com.arrobaautowired.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StepListener implements StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.debug("beforeStep ==> {}", stepExecution);

    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.debug("afterStep ==> {}", stepExecution);
        return null;
    }
}
