package com.example.explorev3.pojo;

public class PostItem {

    

    private String uid;
    private String name;
    private String address;
    private String desc;
    private String imgUrl;
    private int vote;

    public PostItem() {}

    public PostItem(String userUID, String placeName, String placeAddress, String placeDesc, String imgUrl, int vote) {
        this.uid = userUID;
        this.name = placeName;
        this.address = placeAddress;
        this.desc = placeDesc;
        this.imgUrl = imgUrl;
        this.vote = vote;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getDesc() {
        return desc;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public int getVote() {
        return vote;
    }
}
