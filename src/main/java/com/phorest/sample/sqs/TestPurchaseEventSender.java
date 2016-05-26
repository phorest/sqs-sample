package com.phorest.sample.sqs;

import com.amazonaws.services.sqs.AmazonSQSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@RestController
@RequestMapping("test/purchaseEvent")
public class TestPurchaseEventSender {

    @Autowired
    private AmazonSQSClient sqsClient;

    @Autowired
    private SqsQueue purchaseEventsQueue;

    @RequestMapping(method = RequestMethod.POST)
    public void send() {
        sqsClient.sendMessage(purchaseEventsQueue.getUrl(), (getEventAsString()));
    }

    private String getEventAsString() {
        return read(getResourceAsStream("/samples/sample-event.json"));
    }

    private InputStream getResourceAsStream(String fileName) {
        return this.getClass().getResourceAsStream(fileName);
    }

    private String read(InputStream input) {
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input))) {
            return buffer.lines().collect(Collectors.joining());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
