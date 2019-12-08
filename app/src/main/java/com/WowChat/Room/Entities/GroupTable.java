package com.WowChat.Room.Entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class GroupTable {
    @NonNull
    @PrimaryKey
    String id;
    String name;
    String image;
    Integer unseenMessages;

    public GroupTable(@NonNull String id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.unseenMessages=0;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getUnseenMessages() {
        return unseenMessages;
    }

    public void setUnseenMessages(Integer unseenMessages) {
        this.unseenMessages = unseenMessages;
    }
}
