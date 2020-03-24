package com.tourDSS.producer.models;

import com.google.gson.annotations.SerializedName;

public class Tweet {
    private long id;
    private String text;
    private String lang;
    private User user;
    private boolean retweeted;
    private boolean is_quote_status;
    private long in_reply_to_status_id;

    @SerializedName("retweet_count")
    private int retweetCount;

    @SerializedName("favorite_count")
    private int favoriteCount;

    public Tweet(long id, String text, String lang, User user, int retweetCount, int favoriteCount, boolean retweeted, long in_reply_to_status_id, boolean is_quote_status) {
        this.id = id;
        this.text = text;
        this.lang = lang;
        this.user = user;
        this.retweetCount = retweetCount;
        this.favoriteCount = favoriteCount;
        this.retweeted = retweeted;
        this.in_reply_to_status_id = in_reply_to_status_id;
        this.is_quote_status = is_quote_status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public void setRetweetCount(int retweetCount) {
        this.retweetCount = retweetCount;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", lang='" + lang + '\'' +
                ", user=" + user +
                ", retweeted=" + retweeted +
                ", is_quote_status=" + is_quote_status +
                ", in_reply_to_status_id=" + in_reply_to_status_id +
                ", retweetCount=" + retweetCount +
                ", favoriteCount=" + favoriteCount +
                '}';
    }
}
