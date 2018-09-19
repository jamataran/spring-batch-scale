package com.arrobaautowired.read;

import com.arrobaautowired.record.Record;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ReaderConfig {

    @Value("${masterapp.inroot}")
    private String inRoot;

    @Value("${masterapp.infolder: classpath:input/*.xml}")
    private String inFolder;

    /**
     * El bean lector principal leerá varios ficheros (sobre la carpeta configurada). Estos ficheros seran procesados por el bean altasReader() que procesará los ficheros xml y creará
     * elmentos de tipo {@link Record}
     *
     * @return Elemento {@link MultiResourceItemReader}
     */
    @Bean
    @SuppressWarnings("unchecked")
    public MultiResourceItemReader<Record> filesReader() throws Exception {
        log.debug("Iniciando MultiResourceItemReader...");

        // Creo una instancia del lector multirecurso.
        MultiResourceItemReader<Record> multiResourceItemReader = new MultiResourceItemReader();

        // Configuro los origenes (Para este ejemplo los leo del classpath)
        ClassLoader cLoader = this.getClass().getClassLoader();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cLoader);
        multiResourceItemReader.setResources(resolver.getResources(inFolder));
        log.debug("Configurada instancia ResourcePatternResolver {} con carpeta {}", resolver, inFolder);

        // Delego la lectura de cada fichero.
        multiResourceItemReader.setDelegate(altasReader());

        log.debug("Inyectando al contexto instancia de MultiResourceItemReader: {}", multiResourceItemReader);
        return multiResourceItemReader;
    }

    /**
     * Bean que lee datos de xml y los mapea
     *
     * @return Instancia de {@link StaxEventItemReader<Record>} configurada
     */
    @Bean
    public StaxEventItemReader<Record> altasReader() throws Exception {
        log.debug("Inicando StaxEventItemReader...");

        // Creo el objeto.
        StaxEventItemReader<Record> staxEventItemReader = new StaxEventItemReader<>();

        // Para leer los ficheros necesito una instancia de marshaller (ver abajo)
        staxEventItemReader.setUnmarshaller(marshaller());
        staxEventItemReader.setFragmentRootElementNames(new String[]{
                "record",
        });


        // Devuelvo el lector.
        log.debug("Inyectando al contexto el StaxEventItemReader bean: {}", staxEventItemReader);
        return staxEventItemReader;
    }

    /**
     * Bean para Marshalling xml->java
     *
     * @return {@link Jaxb2Marshaller} instance.
     */
    @Bean
    public Jaxb2Marshaller marshaller() throws Exception {
        log.debug("Iniciando Jaxb2Marshaller...");

        // Instancio el objeto marshaller.
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(Record.class);
        marshaller.afterPropertiesSet();

        // Devuelvo el marshaller.
        log.debug("Inyectando al contexto instancia de Jaxb2Marshaller: {}", marshaller);
        return marshaller;
    }

}
