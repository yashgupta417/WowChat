package com.WowChat.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Message {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("sender")
    @Expose
    private Integer sender;
    @SerializedName("amorpm")
    @Expose
    private String amorpm;
    @SerializedName("recipient")
    @Expose
    private Integer recipient;
    @SerializedName("image")
    @Expose
    private String image;


    /**
     * No args constructor for use in serialization
     *
     */
    public Message() {
    }

    /**
     *
     * @param sender
     * @param text
     * @param recipient
     */

    public Message(String id,String text,Integer sender, Integer recipient, String date, String time,String amorpm,String image) {
        this.id=id;
        this.text = text;
        this.date = date;
        this.time = time;
        this.sender = sender;
        this.recipient = recipient;
        this.amorpm=amorpm;
        this.image=image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }




    public Integer getSender() {
        return sender;
    }

    public void setSender(Integer sender) {
        this.sender = sender;
    }

    public Integer getRecipient() {
        return recipient;
    }

    public void setRecipient(Integer recipient) {
        this.recipient = recipient;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAmorpm() {
        return amorpm;
    }

    public void setAmorpm(String amorpm) {
        this.amorpm = amorpm;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}