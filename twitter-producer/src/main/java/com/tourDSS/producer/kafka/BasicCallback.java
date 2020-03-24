package com.tourDSS.producer.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.logging.Level;
import java.util.logging.Logger;

public class BasicCallback implements Callback {
    public static final String CALLBACK_LOGGER = "Producer Callback";

    public void onCompletion(RecordMetadata metadata, Exception exception) {
        if (exception != null) {
            Logger.getLogger(CALLBACK_LOGGER).log(Level.SEVERE, exception.getMessage());
        }
    }
}
