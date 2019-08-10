package com.WowChat.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Shot_Write {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("by")
    @Expose
    private Integer by;
    @SerializedName("to")
    @Expose
    private Integer to;

    /**
     * No args constructor for use in serialization
     *
     */
    public Shot_Write() {
    }

    /**
     *
     * @param to
     * @param id
     * @param text
     * @param title
     * @param image
     * @param by
     * @param date
     */
    public Shot_Write(Integer id, String title, String text, String image, String date, Integer by, Integer to) {
        super();
        this.id = id;
        this.title = title;
        this.text = text;
        this.image = image;
        this.date = date;
        this.by = by;
        this.to = to;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getBy() {
        return by;
    }

    public void setBy(Integer by) {
        this.by = by;
    }

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
    }

}