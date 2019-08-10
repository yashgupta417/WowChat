package com.WowChat.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentsOnThisShot {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("commented_by")
    @Expose
    private User commentedBy;
    @SerializedName("shot")
    @Expose
    private Shot shot;

    /**
     * No args constructor for use in serialization
     *
     */
    public CommentsOnThisShot() {
    }

    /**
     *
     * @param id
     * @param text
     * @param date
     */
    public CommentsOnThisShot(Integer id, String text, String date) {
        super();
        this.id = id;
        this.text = text;
        this.date = date;
//        this.commentedBy = commentedBy;
//        this.shot = shot;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public User getCommentedBy() {
        return commentedBy;
    }

    public void setCommentedBy(User commentedBy) {
        this.commentedBy = commentedBy;
    }

    public Shot getShot() {
        return shot;
    }

    public void setShot(Shot shot) {
        this.shot = shot;
    }
}