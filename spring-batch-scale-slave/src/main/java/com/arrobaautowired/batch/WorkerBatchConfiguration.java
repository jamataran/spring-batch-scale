package com.arrobaautowired.batch;

import com.arrobaautowired.payment.Payment;
import com.arrobaautowired.processor.PaymentWriter;
import com.arrobaautowired.processor.RecordProcessor;
import com.arrobaautowired.record.Record;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.batch.core.step.item.ChunkProcessor;
import org.springframework.batch.core.step.item.SimpleChunkProcessor;
import org.springframework.batch.integration.chunk.ChunkProcessorChunkHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.jms.dsl.Jms;

@Configuration
@EnableIntegration
public class WorkerBatchConfiguration {

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        factory.setBrokerURL("tcp://localhost:61616");
        factory.setTrustAllPackages(Boolean.TRUE);
        return factory;
    }

    /*
     * Configure inbound flow (requests coming from the master)
     */
    @Bean
    public DirectChannel requests() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow inboundFlow(ActiveMQConnectionFactory connectionFactory) {
        return IntegrationFlows
                .from(Jms.messageDrivenChannelAdapter(connectionFactory).destination("requests"))
                .channel(requests())
                .get();
    }

    /*
     * Configure outbound flow (replies going to the master)
     */
    @Bean
    public DirectChannel replies() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow outboundFlow(ActiveMQConnectionFactory connectionFactory) {
        return IntegrationFlows
                .from(replies())
                .handle(Jms.outboundAdapter(connectionFactory).destination("replies"))
                .get();
    }

    /*
     * Configure the ChunkProcessorChunkHandler
     */
    @Bean
    @ServiceActivator(inputChannel = "requests", outputChannel = "replies")
    public ChunkProcessorChunkHandler<Record> chunkProcessorChunkHandler() {
        ChunkProcessor<Record> chunkProcessor = new SimpleChunkProcessor<Record, Payment>(itemProcessor(), itemWriter());

        ChunkProcessorChunkHandler<Record> chunkProcessorChunkHandler = new ChunkProcessorChunkHandler<>();
        chunkProcessorChunkHandler.setChunkProcessor(chunkProcessor);

        return chunkProcessorChunkHandler;
    }

    @Bean
    public RecordProcessor itemProcessor() {
        return new RecordProcessor();
    }

    public PaymentWriter itemWriter() {
        return new PaymentWriter();
    }

}
