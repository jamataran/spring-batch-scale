package com.arrobaautowired.processor;

import com.arrobaautowired.payment.Payment;
import com.arrobaautowired.record.Record;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import java.math.BigDecimal;
import java.util.Random;

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

    private static String getRandomBic() {
        String start = "ES";
        Random value = new Random();

        //Generate two values to append to 'BE'
        int r1 = value.nextInt(10);
        int r2 = value.nextInt(10);
        start += Integer.toString(r1) + Integer.toString(r2) + " ";

        int count = 0;
        int n = 0;
        for (int i = 0; i < 20; i++) {
            if (count == 4) {
                start += " ";
                count = 0;
            } else
                n = value.nextInt(10);
            start += Integer.toString(n);
            count++;

        }

        return start;
    }

    @Override
    public Payment process(Record record) throws Exception {
        log.debug("\tComenzando el procesado de: {}", record);

        log.debug("\t\tSimulando delay de 20s para el procesado {}", record);
        Thread.sleep(20000);

        Payment payment = Payment
                .builder()
                .fullName(record.getName())
                .amount(record.getIncome())
                .bic(getRandomBic())
                .currency('€')
                .build();
        log.debug("\n\tNUEVO PAGO OBTENIDO\n\t\t{}\t==>\t{}\n", record, payment);
        return payment;
    }
}
