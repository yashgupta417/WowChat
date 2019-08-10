package com.WowChat.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FCMToken {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private Object name;
    @SerializedName("active")
    @Expose
    private Boolean active;
    @SerializedName("date_created")
    @Expose
    private String dateCreated;
    @SerializedName("application_id")
    @Expose
    private Object applicationId;
    @SerializedName("device_id")
    @Expose
    private Object deviceId;
    @SerializedName("registration_id")
    @Expose
    private String registrationId;
    @SerializedName("cloud_message_type")
    @Expose
    private String cloudMessageType;
    @SerializedName("user")
    @Expose
    private Object user;

    /**
     * No args constructor for use in serialization
     *
     */
    public FCMToken() {
    }

    /**
     * @param registrationId
     * @param cloudMessageType
     * @param user
     */
    public FCMToken(String registrationId, String cloudMessageType, Object user) {
        super();
        this.registrationId = registrationId;
        this.cloudMessageType = cloudMessageType;
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Object getName() {
        return name;
    }

    public void setName(Object name) {
        this.name = name;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Object getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Object applicationId) {
        this.applicationId = applicationId;
    }

    public Object getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Object deviceId) {
        this.deviceId = deviceId;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getCloudMessageType() {
        return cloudMessageType;
    }

    public void setCloudMessageType(String cloudMessageType) {
        this.cloudMessageType = cloudMessageType;
    }

    public Object getUser() {
        return user;
    }

    public void setUser(Object user) {
        this.user = user;
    }

}