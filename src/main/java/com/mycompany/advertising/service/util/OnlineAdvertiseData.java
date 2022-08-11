package com.mycompany.advertising.service.util;

/**
 * Created by Amir on 8/7/2022.
 */
public class OnlineAdvertiseData {
    private String imageUrl;
    private String description;
    private String title;
    private AdvertiseTypeForOnlineData type;
    private int members;
    private int onlineMembers;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public AdvertiseTypeForOnlineData getType() {
        return type;
    }

    public void setType(AdvertiseTypeForOnlineData type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getOnlineMembers() {
        return onlineMembers;
    }

    public void setOnlineMembers(int onlineMembers) {
        this.onlineMembers = onlineMembers;
    }

    public int getMembers() {
        return members;
    }

    public void setMembers(int members) {
        this.members = members;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
