package com.tourDSS.consumer;

import com.tourDSS.consumer.cassandra.CassandraRepository;
import com.tourDSS.consumer.spark.SparkKafkaConnector;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;


public class Main {
    public static void main(String[] args) throws InterruptedException {
        Logger.getLogger("org").setLevel(Level.OFF);
        Logger.getLogger("akka").setLevel(Level.OFF);

        CassandraRepository connector = new CassandraRepository();
        connector.initialize("127.0.0.1", null);

        SparkKafkaConnector.connectToTopic();
    }
}
