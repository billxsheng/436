package com.tourDSS.rollup.spark;

import com.datastax.spark.connector.japi.CassandraJavaUtil;
import com.tourDSS.rollup.models.Tweet;
import com.tourDSS.rollup.constants.Constants;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SparkLocationRollup {
    SparkConf conf = new SparkConf().setAppName("spark-streaming").setMaster("local").set("spark.executor.memory", "1g");
    JavaSparkContext sc = new JavaSparkContext(conf);

    public void runJob() {
        Date now = new Date();
        Logger.getLogger("SparkLocationRollup").log(Level.INFO, "Starting Rollup Job from " + now.toString());
        JavaPairRDD<String, Long> cassandraRdd = CassandraJavaUtil.javaFunctions(sc)
                .cassandraTable(Constants.CASSANDRA_KEYSPACE_NAME, Constants.CASSANDRA_CORE_TWEETS_TABLE, CassandraJavaUtil.mapRowTo(Tweet.class))
                .select("tweet_location", "tweet_sentiment")
                .where("id < minTimeUUID(?)", now)
                .mapToPair((tweet) -> new Tuple2<>(tweet.getTweetLocation() + " " + tweet.getTweetSentiment(), (long) 1))
                .reduceByKey((accumulator, n) -> (accumulator + n));

        CassandraJavaUtil
                .javaFunctions(cassandraRdd)
                .writerBuilder(Constants.CASSANDRA_KEYSPACE_NAME, Constants.CASSANDRA_CORE_LOCATION_TABLE, CassandraJavaUtil.mapTupleToRow(String.class, Long.class))
                .saveToCassandra();
    }
}
