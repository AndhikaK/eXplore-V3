package com.example.explorev3.pojo;

public class UpdateAvatar {
    private String imageUrl;

    public UpdateAvatar() {

    }

    public UpdateAvatar(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
