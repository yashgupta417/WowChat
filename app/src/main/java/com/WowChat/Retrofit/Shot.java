package com.WowChat.Retrofit;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Shot {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("image")
    @Expose
    private Object image;
    @SerializedName("by")
    @Expose
    private User by;
    @SerializedName("to")
    @Expose
    private User to;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("comments_on_this_shot")
    @Expose
    private List<CommentsOnThisShot> commentsOnThisShot;

    /**
     * No args constructor for use in serialization
     *
     */
    public Shot() {
    }

    /**
     *
     * @param to
     * @param text
     * @param title
     * @param image
     * @param by
     * @param date
     * @param commentsOnThisShot
     */
    public Shot(String title, String text, Object image, Integer by, Integer to, String date, List<CommentsOnThisShot> commentsOnThisShot) {
        super();
        this.title = title;
        this.text = text;
        this.image = image;
//        this.by = by;
//        this.to = to;
//        this.date = date;
//        this.commentsOnThisShot = commentsOnThisShot;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Object getImage() {
        return image;
    }

    public void setImage(Object image) {
        this.image = image;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<CommentsOnThisShot> getCommentsOnThisShot() {
        return commentsOnThisShot;
    }

    public void setCommentsOnThisShot(List<CommentsOnThisShot> commentsOnThisShot) {
        this.commentsOnThisShot = commentsOnThisShot;
    }

    public User getBy() {
        return by;
    }

    public void setBy(User by) {
        this.by = by;
    }

    public User getTo() {
        return to;
    }

    public void setTo(User to) {
        this.to = to;
    }
}