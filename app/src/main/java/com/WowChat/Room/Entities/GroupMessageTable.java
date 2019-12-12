package com.WowChat.Room.Entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class GroupMessageTable {
    @NonNull
    @PrimaryKey
    private String id;
    private String text;
    private String event;
    private String image;
    private String senderId;
    private String senderName;
    private String senderImage;
    private String group_id;
    private String dateofmessaging;
    private String timeofmessaging;
    private String AMorPM;
    private String status;

    public GroupMessageTable(@NonNull String id, String text,String event, String image, String senderId, String senderName, String senderImage,
                             String group_id, String dateofmessaging, String timeofmessaging, String AMorPM,String status) {
        this.id = id;
        this.text = text;
        this.event=event;
        this.image = image;
        this.senderId = senderId;
        this.senderName = senderName;
        this.senderImage = senderImage;
        this.group_id = group_id;
        this.dateofmessaging = dateofmessaging;
        this.timeofmessaging = timeofmessaging;
        this.AMorPM = AMorPM;
        this.status=status;
    }


    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getDateofmessaging() {
        return dateofmessaging;
    }

    public void setDateofmessaging(String dateofmessaging) {
        this.dateofmessaging = dateofmessaging;
    }

    public String getTimeofmessaging() {
        return timeofmessaging;
    }

    public void setTimeofmessaging(String timeofmessaging) {
        this.timeofmessaging = timeofmessaging;
    }

    public String getAMorPM() {
        return AMorPM;
    }

    public void setAMorPM(String AMorPM) {
        this.AMorPM = AMorPM;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderImage() {
        return senderImage;
    }

    public void setSenderImage(String senderImage) {
        this.senderImage = senderImage;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
