package com.phorest.sample.sqs;

import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;

@Configuration
@DependsOn("awsCredentialsConfiguration")
public class SqsConfiguration {

    @Value("${aws.region:eu-west-1}")
    private String awsRegion;

    @Value("${sqs.events-queue-name:sqs-sample-events-queue}")
    private String sqsQueueName;

    @Autowired
    private PurchaseEventListener purchaseEventListener;

    private SQSConnection connection;

    @PostConstruct
    public void init() throws JMSException {
        connection = createConnection();
        AmazonSQSClient sqsClient = sqsClient();
        purchaseEventsQueue(sqsClient);
        setupListener();
        connection.start();
    }

    @PreDestroy
    public void cleanUp() throws JMSException {
        connection.close();
    }

    @Bean
    public AmazonSQSClient sqsClient() {
        AmazonSQSClient sqsClient = new AmazonSQSClient();
        sqsClient.setRegion(getRegion());
        return sqsClient;
    }

    @Bean
    public SqsQueue purchaseEventsQueue(AmazonSQSClient sqsClient) {
        CreateQueueRequest createQueueRequest = new CreateQueueRequest().withQueueName(sqsQueueName);
        CreateQueueResult createQueueResult = sqsClient.createQueue(createQueueRequest);
        return new SqsQueue(sqsQueueName, createQueueResult.getQueueUrl());
    }

    private void setupListener() throws JMSException {
        Session session = createSession();
        Queue jmsQueue = createJmsQueue(session);
        MessageConsumer consumer = createConsumer(session, jmsQueue);
        consumer.setMessageListener(purchaseEventListener);
    }

    private MessageConsumer createConsumer(Session session, Queue jmsQueue) throws JMSException {
        return session.createConsumer(jmsQueue);
    }

    private Session createSession() throws JMSException {
        return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    private Queue createJmsQueue(Session session) throws JMSException {
        return session.createQueue(sqsQueueName);
    }

    private SQSConnection createConnection() throws JMSException {
        SQSConnectionFactory connectionFactory = getSqsConnectionFactory();
        return connectionFactory.createConnection();
    }

    private SQSConnectionFactory getSqsConnectionFactory() {
        return SQSConnectionFactory.builder()
                .withRegion(getRegion())
                .withAWSCredentialsProvider(new DefaultAWSCredentialsProviderChain())
                .build();
    }

    private Region getRegion() {
        return Region.getRegion(Regions.fromName(awsRegion));
    }

}
