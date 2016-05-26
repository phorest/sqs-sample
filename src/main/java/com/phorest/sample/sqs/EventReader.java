package com.phorest.sample.sqs;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phorest.events.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class EventReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventReader.class);

    /*
        Spring boot automatically creates ObjectMapper bean if it doesn't exist and registers Joda Module with it if it's on classpath
     */
    @Autowired
    private ObjectMapper objectMapper;

    public <T> Event<T> readEvent(String messageText, Class<T> type) {
        try {
            return tryToReadSnsNotification(messageText, type);
        } catch (IOException e) {
            throw new UnreadableEventException("Failed to read Event of type: " + type + " from message text: " + messageText, e);
        }
    }

    private <T> Event<T> tryToReadSnsNotification(String messageText, Class<T> type) throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(Event.class, type);
        Event<T> event = objectMapper.readValue(messageText, javaType);
        LOGGER.debug("Read event: {}", event);
        return event;
    }
}
