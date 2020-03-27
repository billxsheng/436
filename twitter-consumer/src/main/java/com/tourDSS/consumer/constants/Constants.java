package com.tourDSS.consumer.constants;

import java.util.ArrayList;
import java.util.Arrays;

public class Constants {
    public static final String CASSANDRA_KEYSPACE_NAME = "tourdss";
    public static final String KAFKA_TOPIC_NAME = "tourDSS";
    public static final String CASSANDRA_CORE_TWEETS_TABLE = "tweets";
    // Insert cities of interest
    public static final ArrayList<String> SENTIMENT_LOCATIONS = new ArrayList<>(Arrays.asList("Alabama", "Alaska", "American Samoa", "Arizona", "Arkansas", "California", "Colorado", "Connecticut", "Delaware", "District of Columbia", "Florida", "Georgia", "Guam", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Minor Outlying Islands", "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York", "North Carolina", "North Dakota", "Northern Mariana Islands", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Puerto Rico", "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "U.S. Virgin Islands", "Utah", "Vermont", "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming", "Toronto", "Montreal", "Winnipeg", "Vancouver", "Ottawa"));
}
