package com.WowChat.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GroupRead {
    @SerializedName("group_id")
    @Expose
    private Integer groupId;
    @SerializedName("group_name")
    @Expose
    private String groupName;
    @SerializedName("group_image")
    @Expose
    private String groupImage;
    @SerializedName("members")
    @Expose
    private ArrayList<User> members;
    @SerializedName("president")
    @Expose
    private User president;
    @SerializedName("datetime_of_creation")
    private String dateTimeOfCreation;

    public GroupRead(Integer groupId,String groupName) {
        this.groupId=groupId;
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupImage() {
        return groupImage;
    }

    public void setGroupImage(String groupImage) {
        this.groupImage = groupImage;
    }

    public ArrayList<User> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<User> members) {
        this.members = members;
    }

    public User getPresident() {
        return president;
    }

    public void setPresident(User president) {
        this.president = president;
    }

    public String getDateTimeOfCreation() {
        return dateTimeOfCreation;
    }

    public void setDateTimeOfCreation(String dateTimeOfCreation) {
        this.dateTimeOfCreation = dateTimeOfCreation;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }
}
