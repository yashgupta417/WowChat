package com.WowChat.Room.Entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MessageTable {

    @PrimaryKey
    @NonNull
    private String messageid;
    private String text;
    private String sender;
    private String recipient;
    private String dateofmessaging;
    private String timeofmessaging;
    private String status;


    public MessageTable(@NonNull String messageid,String text, String sender, String recipient, String dateofmessaging, String timeofmessaging) {
        this.messageid=messageid;
        this.text = text;
        this.sender = sender;
        this.recipient = recipient;
        this.dateofmessaging = dateofmessaging;
        this.timeofmessaging = timeofmessaging;
    }



    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
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


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessageid() {
        return messageid;
    }

    public void setMessageid(String messageid) {
        this.messageid = messageid;
    }
}
