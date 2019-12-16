package com.WowChat.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MemoryRead {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("group")
    @Expose
    private GroupWrite group;
    @SerializedName("member_posted")
    @Expose
    private User member_posted;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("image")
    @Expose
    private String image;

    public MemoryRead(Integer id, GroupWrite group, User member_posted, String text, String image) {
        this.id = id;
        this.group = group;
        this.member_posted = member_posted;
        this.text = text;
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public GroupWrite getGroup() {
        return group;
    }

    public void setGroup(GroupWrite group) {
        this.group = group;
    }

    public User getMember_posted() {
        return member_posted;
    }

    public void setMember_posted(User member_posted) {
        this.member_posted = member_posted;
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
}
