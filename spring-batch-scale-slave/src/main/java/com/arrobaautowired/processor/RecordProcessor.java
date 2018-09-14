package com.arrobaautowired.processor;

import com.arrobaautowired.payment.Payment;
import com.arrobaautowired.record.Record;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

/**
 * Procesador de Recors (genera pagos).
 * Simula el procesado de elementos record y casuisticas que pueden ocurrir
 * <ul>
 * <li>Tarda en responder</li>
 * <li>Se produce una excepcion</li>
 * <li>Inserta OK</li>
 * </ul>
 */
@Slf4j
public class RecordProcessor implements ItemProcessor<Record, Payment> {
    @Override
    public Payment process(Record record) throws Exception {
        log.debug("Processing {}", record);
        return Payment.builder().bic("BIC").build();
    }
}
