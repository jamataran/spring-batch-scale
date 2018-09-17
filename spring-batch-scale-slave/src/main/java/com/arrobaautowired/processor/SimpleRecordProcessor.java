package com.arrobaautowired.processor;

import com.arrobaautowired.payment.Payment;
import com.arrobaautowired.record.Record;
import org.springframework.batch.item.ItemProcessor;

import static com.arrobaautowired.processor.ComplexRecordProcessor.getRandomBic;

public class SimpleRecordProcessor implements ItemProcessor<Record, Payment> {
    @Override
    public Payment process(Record record) throws Exception {
        return Payment
                .builder()
                .fullName(record.getName())
                .amount(record.getIncome())
                .bic(getRandomBic())
                .currency('€')
                .build();
    }
}
