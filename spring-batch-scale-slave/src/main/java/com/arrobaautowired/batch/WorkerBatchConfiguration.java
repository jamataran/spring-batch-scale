package com.arrobaautowired.batch;

import com.arrobaautowired.payment.Payment;
import com.arrobaautowired.processor.PaymentWriter;
import com.arrobaautowired.processor.RecordProcessor;
import com.arrobaautowired.record.Record;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.batch.core.step.item.ChunkProcessor;
import org.springframework.batch.core.step.item.SimpleChunkProcessor;
import org.springframework.batch.integration.chunk.ChunkProcessorChunkHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.AggregatorFactoryBean;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.jms.dsl.Jms;


@Configuration
@IntegrationComponentScan
@EnableIntegration
public class WorkerBatchConfiguration {

    @Bean
    public org.apache.activemq.ActiveMQConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        factory.setBrokerURL("tcp://localhost:61616");
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
                .from(Jms.messageDrivenChannelAdapter(connectionFactory())
                        .configureListenerContainer(c -> c.subscriptionDurable(false))
                        .destination("QUEUE_RECORDS"))
                .channel(requests())
                .get();
    }

    @Bean
    public IntegrationFlow outgoingReplies() {
        return IntegrationFlows.from("requests")
                .handle(Jms
                        .outboundGateway(connectionFactory())
                        .requestDestination("replies"))
                .get();
    }

    @Bean
    @ServiceActivator(inputChannel = "QUEUE_RECORDS")
    public AggregatorFactoryBean serviceActivator() throws Exception {
        AggregatorFactoryBean aggregatorFactoryBean = new AggregatorFactoryBean();
        aggregatorFactoryBean.setProcessorBean(chunkProcessorChunkHandler());
        aggregatorFactoryBean.setOutputChannel(replies());

        return aggregatorFactoryBean;
    }

    @Bean
    public ChunkProcessorChunkHandler chunkProcessorChunkHandler() {
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
