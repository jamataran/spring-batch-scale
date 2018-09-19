package com.arrobaautowired.writer;

import com.arrobaautowired.payment.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

@Slf4j
public class PaymentWriter implements ItemWriter<Payment> {
    @Override
    public void write(List<? extends Payment> list) throws Exception {
        log.debug("\n\nFINALIZADO CHUNK  ============================\n\n");
        list.parallelStream().forEach(this::writePayment);
        log.debug("\n\n==============================================\n\n");
    }

    private void writePayment(Payment payment) {
        log.debug("\t\tPAYMENT: {}", payment);
    }
}
