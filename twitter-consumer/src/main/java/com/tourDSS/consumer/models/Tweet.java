package com.tourDSS.consumer.models;

import com.datastax.driver.core.utils.UUIDs;
import com.monkeylearn.MonkeyLearn;
import com.monkeylearn.MonkeyLearnException;
import com.tourDSS.consumer.keys.keys;
import org.json.JSONObject;
import org.json.simple.JSONArray;

import java.io.Serializable;
import java.util.UUID;


public class Tweet implements Serializable {
    public static final String[] SENTIMENTS = new String[]{"NEGATIVE", "NEUTRAL", "POSITIVE"};
    public static final String TARGET_STRING = "";

    private UUID id;
    private String tweetUsername;
    private String tweetText;
    private String tweetLocation;
    private String tweetSentiment;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTweetLocation() {
        return tweetLocation;
    }

    public void setTweetLocation(String location) {
        tweetLocation = location;
    }

    public String getTweetUsername() {
        return tweetUsername;
    }

    public void setTweetUsername(String name) {
        tweetUsername = name;
    }

    public String getTweetText() {
        return tweetText;
    }

    public void setTweetText(String text) {
        tweetText = text;
    }

    public String getTweetSentiment() {
        return tweetSentiment;
    }

    public void setTweetSentiment(String sentiment) {
        tweetSentiment = sentiment;
    }

    public Tweet(String name, String text, String location) {
        tweetUsername = name;
        tweetText = text;
        tweetLocation = location;
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "id=" + id +
                ", name='" + tweetUsername + '\'' +
                ", text='" + tweetText + '\'' +
                ", location='" + tweetLocation + '\'' +
                ", sentiment='" + tweetSentiment + '\'' +
                '}';
    }

    public static String processTweet(String text) {
        return text.replaceAll(TARGET_STRING, "");
    }

    public static String analyze(Tweet tweet) throws MonkeyLearnException {
        MonkeyLearn ml = new MonkeyLearn(keys.ML_API_KEY);
        String modelId = "cl_pi3C7JiL";
        String[] mlData = {tweet.getTweetText()};

        JSONArray res = ml.classifiers.classify(modelId, mlData).arrayResult;
        JSONObject jo = new JSONObject();
        jo.put("res", res);
        String data = jo
                .getJSONArray("res")
                .getJSONArray(0)
                .getJSONArray(0)
                .getJSONObject(0)
                .get("label")
                .toString();

//        tweet.setTweetSentiment(SENTIMENTS[ThreadLocalRandom.current().nextInt(0, 3)]);
        tweet.setTweetSentiment(data);
        tweet.setId(UUIDs.timeBased());
        return tweet.toString();
    }
}
