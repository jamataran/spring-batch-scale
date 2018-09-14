package com.arrobaautowired.write;

import com.arrobaautowired.domain.Record;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class RecordItemWritter implements ItemWriter<Record> {

    /**
     * Implementación de la esritura de los redords procesados en un chunk
     *
     * @param list Lista de records procesados
     */
    @Override
    public void write(List<? extends Record> list) throws Exception {
        log.debug("Escribiendo records: {}", list);
        list.forEach(this::print);
    }

    /**
     * Imprime en el log (Punto final)
     */
    private void print(Record r) {
        log.trace("\t\tRECORD ==> {}", r);
    }
}
