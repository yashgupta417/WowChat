package com.WowChat.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GroupWrite {
    @SerializedName("id")
    @Expose
    private  Integer groupId;
    @SerializedName("group_name")
    @Expose
    private String groupName;
    @SerializedName("group_image")
    @Expose
    private String groupImage;
    @SerializedName("members")
    @Expose
    private ArrayList<Integer> members;
    @SerializedName("president")
    @Expose
    private Integer president;
    @SerializedName("datetime_of_creation")
    private String dateTimeOfCreation;

    public GroupWrite(String groupName, String groupImage, ArrayList<Integer> members, Integer president, String dateTimeOfCreation) {
        this.groupName = groupName;
        this.groupImage = groupImage;
        this.members = members;
        this.president = president;
        this.dateTimeOfCreation = dateTimeOfCreation;
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

    public ArrayList<Integer> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<Integer> members) {
        this.members = members;
    }

    public Integer getPresident() {
        return president;
    }

    public void setPresident(Integer president) {
        this.president = president;
    }

    public String getDateTimeOfCreation() {
        return dateTimeOfCreation;
    }

    public void setDateTimeOfCreation(String dateTimeOfCreation) {
        this.dateTimeOfCreation = dateTimeOfCreation;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getGroupId() {
        return groupId;
    }
}
