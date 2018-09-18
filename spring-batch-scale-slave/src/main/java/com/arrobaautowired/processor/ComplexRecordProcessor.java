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
public class ComplexRecordProcessor implements ItemProcessor<Record, Payment> {

    public static String getRandomBic() {
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

        Random randomizador = new Random();

        int comportamientoAleatorio = randomizador.nextInt(3);
        log.debug("Comportamiento de {}: {}", record, comportamientoAleatorio);

        switch (comportamientoAleatorio) {
            // Lentitud.
            case 0:
                log.debug("\t\tSimulando delay de procesamiento...");
                int delay = randomizador.nextInt(60000);
                log.debug("\t\t\tDelay generado: {}", delay);
                log.debug("\t\t\tDurmiendo...");
                Thread.sleep(60000);
                log.debug("\t\t\tDespertando...");
                break;

            // Excepción
            case 1:
                log.debug("\t\tSe produjo una excepción en el procesamiento...");
//                if (record.getIncome().compareTo(BigDecimal.valueOf(900L)) > 0)
//                    throw new RuntimeException("Error Gravísimo en Record ".concat(record.toString()));

                // Funcionamiento esperado.
            case 2:
                log.debug("\t\tTodo bien...");
                break;
            default:
                log.error("Comportamiento no contemplado");
                break;
        }

        Payment payment = Payment
                .builder()
                .fullName(record.getName())
                .amount(record.getIncome())
                .bic(getRandomBic())
                .currency('€')
                .build();
        log.debug("\tSe ha conseguido generar el pago {} a partir del record {}", payment, record);
        return payment;
    }
}
