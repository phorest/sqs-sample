package com.phorest.sample.sqs;

import com.amazon.sqs.javamessaging.message.SQSTextMessage;
import com.phorest.events.model.Event;
import com.phorest.events.model.PurchaseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

@Component
public class PurchaseEventListener implements MessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseEventListener.class);

    @Autowired
    private EventReader eventReader;

    @Override
    public void onMessage(Message message) {
        String messageText = getMessageText((SQSTextMessage) message);
        LOGGER.info("Received SQS message with text: {}", messageText);
        Event<PurchaseData> purchaseEvent = eventReader.readEvent(messageText, PurchaseData.class);
        LOGGER.info("Purchase event received: {}", purchaseEvent);
    }

    private String getMessageText(SQSTextMessage message) {
        try {
            return message.getText();
        } catch (JMSException e) {
            throw new UnreadableSqsMessageException("Failed to get message text from message", e);
        }
    }
}
