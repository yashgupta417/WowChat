package com.WowChat.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MemoryWrite {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("group")
    @Expose
    private Integer group;
    @SerializedName("member_posted")
    @Expose
    private Integer member_posted;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("image")
    @Expose
    private String image;

    public MemoryWrite(Integer group, Integer member_posted, String text, String image) {
        this.group = group;
        this.member_posted = member_posted;
        this.text = text;
        this.image = image;
    }

    public Integer getGroup() {
        return group;
    }

    public void setGroup(Integer group) {
        this.group = group;
    }

    public Integer getMember_posted() {
        return member_posted;
    }

    public void setMember_posted(Integer member_posted) {
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
