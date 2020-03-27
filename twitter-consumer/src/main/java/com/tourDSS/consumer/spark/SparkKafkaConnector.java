package com.tourDSS.consumer.spark;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.datastax.spark.connector.japi.CassandraJavaUtil.javaFunctions;
import static com.datastax.spark.connector.japi.CassandraJavaUtil.mapToRow;

import com.tourDSS.consumer.constants.Constants;
import com.tourDSS.consumer.models.Tweet;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.*;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class SparkKafkaConnector {

    public static void connectToTopic() throws InterruptedException {
        SparkConf sparkConf = new SparkConf().setAppName("com.oubre.consumer.spark-streaming").setMaster("local[2]").set("com.oubre.consumer.spark.executor.memory", "1g");
        sparkConf.set("com.oubre.consumer.spark.cassandra.connection.host", "127.0.0.1");

        /* Entry point for all Spark Streaming applications */
        JavaStreamingContext streamingContext = new JavaStreamingContext(sparkConf, Durations.seconds(1));

        Map<String, Object> kafkaParams = new HashMap<>();
        kafkaParams.put("bootstrap.servers", "localhost:9092");
        kafkaParams.put("key.deserializer", StringDeserializer.class);
        kafkaParams.put("value.deserializer", StringDeserializer.class);
        kafkaParams.put("group.id", "use_a_separate_group_id_for_each_stream");
        kafkaParams.put("auto.offset.reset", "latest");
        kafkaParams.put("enable.auto.commit", false);
        Collection<String> topics = Arrays.asList(Constants.KAFKA_TOPIC_NAME);

        /*
            DStream (Discretized Stream) is a continuous sequence (batches) of RDDs
            representing a continuous stream of data.
            In this case, it is a stream of Kafka ConsumerRecord objects
        */
        JavaDStream<ConsumerRecord<String, String>> tweetStream =
                KafkaUtils.createDirectStream(
                        streamingContext,
                        LocationStrategies.PreferConsistent(),
                        ConsumerStrategies.<String, String>Subscribe(topics, kafkaParams));

        /* Transforming consumer records into stream of tweets */
        JavaDStream<Tweet> tweets = tweetStream.map(record -> {
            JSONObject tweetObj = (JSONObject) new JSONParser().parse(record.value());
            JSONObject userObj = (JSONObject) tweetObj.get("user");
            String username = userObj.get("screen_name").toString();
            String user_location = null;

            if (userObj.containsKey("location") && tweetObj.get("is_quote_status").toString().equals("false")) {
                for (String location : Constants.SENTIMENT_LOCATIONS) {
                    if (userObj.get("location").toString().contains(location)) {
                        user_location = location;
                    }
                }

                if (user_location == null) {
                    return null;
                }
            } else {
                return null;
            }

            String text = tweetObj.get("text").toString();
            Tweet tweet = new Tweet(username, Tweet.processTweet(text), user_location);

            return tweet;
        }).filter((record) -> record != null && !record.getTweetText().startsWith("RT")).map((tweet -> {
            Tweet.analyze(tweet);
            return tweet;
        }));

        /*
            An RDD (or resilient distributed data set)
            represents an immutable (cannot be changed), partitioned collection of elements (elements divided into parts)
            that can be operated on in parallel (simultaneous operations, each sub-problem runs in a separate thread and results can be combined *PairRDDFunctions).
        */
        tweets.foreachRDD(rdd -> {
            javaFunctions(rdd).writerBuilder(Constants.CASSANDRA_KEYSPACE_NAME, Constants.CASSANDRA_CORE_TWEETS_TABLE, mapToRow(Tweet.class)).saveToCassandra();
        });

        streamingContext.start();
        streamingContext.awaitTermination();
    }
}
