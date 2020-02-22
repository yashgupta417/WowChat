package com.WowChat.Room.Entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserInfoTable {
    @NonNull
    @PrimaryKey
    private String personUsername;
    private String personFirstName;
    private String personLastName;
    private String personEmail;
    private String personImage;
    private String personId;
    private String latestMesage;
    private String latestMessageTime;
    private String latestMessagedate;
    private String latestMessageAMorPM;
    private Integer unseenMessageCount;
    private Integer isGroup;


    public UserInfoTable(String personUsername, String personFirstName, String personLastName, String personEmail
            ,String personImage,String personId,String latestMesage,String latestMessageTime,String latestMessagedate,String latestMessageAMorPM
            ,Integer isGroup){
        this.personUsername = personUsername;
        this.personFirstName = personFirstName;
        this.personLastName = personLastName;
        this.personEmail = personEmail;
        this.personImage = personImage;
        this.personId=personId;
        this.latestMesage=latestMesage;
        this.latestMessageTime=latestMessageTime;
        this.latestMessagedate=latestMessagedate;
        this.latestMessageAMorPM=latestMessageAMorPM;
        this.isGroup=isGroup;
    }



    public String getPersonUsername() {
        return personUsername;
    }

    public void setPersonUsername(String personUsername) {
        this.personUsername = personUsername;
    }

    public String getPersonFirstName() {
        return personFirstName;
    }

    public void setPersonFirstName(String personFirstName) {
        this.personFirstName = personFirstName;
    }

    public String getPersonLastName() {
        return personLastName;
    }

    public void setPersonLastName(String personLastName) {
        this.personLastName = personLastName;
    }

    public String getPersonEmail() {
        return personEmail;
    }

    public void setPersonEmail(String personEmail) {
        this.personEmail = personEmail;
    }

    public String getPersonImage() {
        return personImage;
    }

    public void setPersonImage(String personImage) {
        this.personImage = personImage;
    }

    public String getLatestMesage() {
        return latestMesage;
    }

    public void setLatestMesage(String latestMesage) {
        this.latestMesage = latestMesage;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getLatestMessageTime() {
        return latestMessageTime;
    }

    public void setLatestMessageTime(String latestMessageTime) {
        this.latestMessageTime = latestMessageTime;
    }

    public Integer getUnseenMessageCount() {
        return unseenMessageCount;
    }

    public void setUnseenMessageCount(Integer unseenMessageCount) {
        this.unseenMessageCount = unseenMessageCount;
    }

    public String getLatestMessagedate() {
        return latestMessagedate;
    }

    public void setLatestMessagedate(String latestMessagedate) {
        this.latestMessagedate = latestMessagedate;
    }

    public String getLatestMessageAMorPM() {
        return latestMessageAMorPM;
    }

    public void setLatestMessageAMorPM(String latestMessageAMorPM) {
        this.latestMessageAMorPM = latestMessageAMorPM;
    }

    public Integer getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(Integer isGroup) {
        this.isGroup = isGroup;
    }
}
