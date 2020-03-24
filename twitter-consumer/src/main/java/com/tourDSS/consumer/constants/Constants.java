package com.tourDSS.consumer.constants;

import java.util.ArrayList;
import java.util.Arrays;

public class Constants {
    public static final String CASSANDRA_KEYSPACE_NAME = "tourDSS";
    public static final String CASSANDRA_CORE_TWEETS_TABLE = "tweets";
    // Insert cities of interest
    public static final ArrayList<String> SENTIMENT_LOCATIONS = new ArrayList<>(Arrays.asList("Toronto", "California", "New York", "Ohio"));
}
