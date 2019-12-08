package com.WowChat.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupMessage {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("event")
    @Expose
    private String event;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("sender")
    @Expose
    private Integer sender;
    @SerializedName("group")
    @Expose
    private Integer group;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("amorpm")
    @Expose
    private String amorpm;
    @SerializedName("image")
    @Expose
    private String image;

    public GroupMessage(String id,String event, String text, Integer sender, Integer group, String date, String time, String amorpm, String image) {
        this.id = id;
        this.text = text;
        this.event=event;
        this.sender = sender;
        this.group = group;
        this.date = date;
        this.time = time;
        this.amorpm = amorpm;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
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

    public Integer getGroup() {
        return group;
    }

    public void setGroup(Integer group) {
        this.group = group;
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
