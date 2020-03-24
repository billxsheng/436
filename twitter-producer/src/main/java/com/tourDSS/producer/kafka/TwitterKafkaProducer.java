package com.tourDSS.producer.kafka;

import com.google.gson.Gson;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import com.tourDSS.producer.config.KafkaConfiguration;
import com.tourDSS.producer.config.TwitterConfiguration;
import com.tourDSS.producer.keys.Keys;
import com.tourDSS.producer.models.Tweet;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TwitterKafkaProducer {
    private Client client;
    private BlockingQueue<String> queue;
    private Gson gson;
    private Callback callback;

    public TwitterKafkaProducer() {
        Authentication authentication = new OAuth1(
                Keys.CONSUMER_KEY,
                Keys.CONSUMER_SECRET,
                Keys.ACCESS_TOKEN,
                Keys.ACCESS_SECRET);

        StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint();
        endpoint.trackTerms(Collections.singletonList(TwitterConfiguration.TERM));
        queue = new LinkedBlockingQueue<>(10000);

        client = new ClientBuilder()
                .hosts(Constants.STREAM_HOST)
                .authentication(authentication)
                .endpoint(endpoint)
                .processor(new StringDelimitedProcessor(queue))
                .build();
        gson = new Gson();
        callback = new com.tourDSS.producer.kafka.BasicCallback();
    }

    private Producer<Long, String> getProducer() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfiguration.SERVERS);
        properties.put(ProducerConfig.ACKS_CONFIG, "1");
        properties.put(ProducerConfig.LINGER_MS_CONFIG, 500);
        properties.put(ProducerConfig.RETRIES_CONFIG, 0);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return new KafkaProducer<>(properties);
    }

    public void run() {
        client.connect();
        try (Producer<Long, String> producer = getProducer()) {
            while (true) {
                Tweet tweet = gson.fromJson(queue.take(), Tweet.class);
                long key = tweet.getId();
                String msg = gson.toJson(tweet);
                ProducerRecord<Long, String> record = new ProducerRecord<>(KafkaConfiguration.TOPIC, key, msg);

                /* Sending record to Kafka topic */
                producer.send(record, callback);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            client.stop();
        }
    }
}
