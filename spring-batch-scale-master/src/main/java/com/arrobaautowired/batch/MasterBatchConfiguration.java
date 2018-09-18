package com.arrobaautowired.batch;

import com.arrobaautowired.payment.Payment;
import com.arrobaautowired.record.Record;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.integration.chunk.ChunkMessageChannelItemWriter;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.jms.dsl.Jms;

@Configuration
@Slf4j
@EnableBatchProcessing
public class MasterBatchConfiguration {

    private final static String MASTER_JOB_TEST = "JOB_MASTER";
    private final static String MATER_JOB_STEP = "STEP-1";
    private final static int CHUNK_SIZE = 5;

    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;
    private MultiResourceItemReader<Record> filesReader;
    private StepListener stepListener;


    @Autowired
    public MasterBatchConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, MultiResourceItemReader<Record> filesReader, StepListener stepListener) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.filesReader = filesReader;
        this.stepListener = stepListener;
    }

    @Bean
    public Job processRecordsJob(JobCompletionNotificationListener listener, Step step1) {
        return jobBuilderFactory
                .get(MASTER_JOB_TEST)
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get(MATER_JOB_STEP)
                .<Record, Payment>chunk(CHUNK_SIZE)
                .reader(filesReader)
                .writer(itemWriter())
                .listener(stepListener)
                .build();
    }

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        factory.setBrokerURL("tcp://localhost:61616");
        factory.setTrustAllPackages(Boolean.TRUE);
        return factory;
    }

    /*
     * Configure outbound flow (requests going to workers)
     */
    @Bean
    public DirectChannel requests() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow jmsOutboundFlow(ActiveMQConnectionFactory connectionFactory) {
        return IntegrationFlows
                .from("requests")
                .handle(Jms
                        .outboundAdapter(connectionFactory)
                        .destination("requests"))
                .get();
    }

    /*
     * Configure inbound flow (replies coming from workers)
     */
    @Bean
    public QueueChannel replies() {
        return new QueueChannel();
    }

    @Bean
    public IntegrationFlow inboundFlow(ActiveMQConnectionFactory connectionFactory) {
        return IntegrationFlows
                .from(Jms.messageDrivenChannelAdapter(connectionFactory).destination("replies"))
                .channel(replies())
                .get();
    }

    /*
     * Configure the ChunkMessageChannelItemWriter.
     * Se trata de un ItemWriter especial, {@link ChunkMessageChannelItemWriter}, que se encarga de enviar la información al pooleer (Middleware externo) y recogerla.
     */
    @Bean
    public ItemWriter<Payment> itemWriter() {
        MessagingTemplate messagingTemplate = new MessagingTemplate();
        messagingTemplate.setDefaultChannel(requests());
        messagingTemplate.setReceiveTimeout(2000);

        ChunkMessageChannelItemWriter<Payment> chunkMessageChannelItemWriter = new ChunkMessageChannelItemWriter<>();
        chunkMessageChannelItemWriter.setMessagingOperations(messagingTemplate);
        chunkMessageChannelItemWriter.setReplyChannel(replies());

        return chunkMessageChannelItemWriter;
    }


}
