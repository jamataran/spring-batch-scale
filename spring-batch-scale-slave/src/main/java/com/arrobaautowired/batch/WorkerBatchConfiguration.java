package com.arrobaautowired.batch;

import com.arrobaautowired.writer.PaymentWriter;
import com.arrobaautowired.processor.RecordProcessor;
import com.arrobaautowired.record.Record;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.batch.core.step.item.SimpleChunkProcessor;
import org.springframework.batch.integration.chunk.ChunkProcessorChunkHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.jms.dsl.Jms;

import java.util.UUID;


@Configuration
@IntegrationComponentScan
@EnableIntegration
@Slf4j
public class WorkerBatchConfiguration {

    @Value("${slave.broker-url}")
    private String brokerUrl;

    @Bean
    @SuppressWarnings("Duplicates")
    public ActiveMQConnectionFactory connectionFactory() {
        log.info("Creando conexión ActiveMQConnectionFactory con Broker URL {}", brokerUrl);
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        factory.setBrokerURL(brokerUrl);
        factory.setTrustAllPackages(Boolean.TRUE);
        return factory;
    }

    @Bean
    public DirectChannel requests() {
        return new DirectChannel();
    }

    @Bean
    public DirectChannel replies() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow jmsIn() {
        return IntegrationFlows
                .from(Jms
                        .messageDrivenChannelAdapter(connectionFactory())
                        .configureListenerContainer(c -> {
                            c.subscriptionDurable(false);
                            c.clientId(UUID.randomUUID().toString());
                        })
                        .destination("requests"))
                .channel(requests())
                .get();
    }

    @Bean
    public IntegrationFlow outgoingReplies() {
        return IntegrationFlows
                .from("replies")
                .handle(Jms.outboundAdapter(connectionFactory()).destination("replies"))
                .get();
    }

    @SuppressWarnings("unchecked")
    @Bean
    @ServiceActivator(inputChannel = "requests", outputChannel = "replies", sendTimeout = "10000")
    public ChunkProcessorChunkHandler<Record> chunkProcessorChunkHandler() {
        ChunkProcessorChunkHandler chunkProcessorChunkHandler = new ChunkProcessorChunkHandler();
        chunkProcessorChunkHandler.setChunkProcessor(new SimpleChunkProcessor(recordProcessor(), paymentWriter()));
        return chunkProcessorChunkHandler;
    }

    @Bean
    public RecordProcessor recordProcessor() {
        return new RecordProcessor();
    }

    @Bean
    public PaymentWriter paymentWriter() {
        return new PaymentWriter();
    }

}
