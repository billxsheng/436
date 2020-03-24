package com.tourDSS.rollup.models;

import java.io.Serializable;


public class Tweet implements Serializable {
    public static final String TARGET_STRING = "";

    private String tweetLocation;
    private String tweetSentiment;

    public String getTweetLocation() {
        return tweetLocation;
    }

    public void setTweetLocation(String location) {
        tweetLocation = location;
    }

    public String getTweetSentiment() {
        return tweetSentiment;
    }

    public void setTweetSentiment(String sentiment) {
        tweetSentiment = sentiment;
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "location='" + tweetLocation + '\'' +
                ", sentiment='" + tweetSentiment + '\'' +
                '}';
    }
}
