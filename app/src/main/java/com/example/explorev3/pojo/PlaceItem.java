package com.example.explorev3.pojo;

public class PlaceItem {

    private String mXid;
    private String mPlaceName;
    private String mLatt;
    private String mLon;
    private int mPlaceRating;


    public PlaceItem(String mXid, String mImagePreview, String mPlaceName, int mPlaceRating, String mLatt, String mLon) {
        this.mXid = mXid;
        this.mPlaceName = mPlaceName;
        this.mPlaceRating = mPlaceRating;
        this.mLatt = mLatt;
        this.mLon = mLon;
    }

    public PlaceItem(String mXid, String mPlaceName, int mPlaceRating, String mLatt, String mLon) {
        this.mXid = mXid;
        this.mPlaceName = mPlaceName;
        this.mPlaceRating = mPlaceRating;
        this.mLatt = mLatt;
        this.mLon = mLon;
    }

    public String getmXid() {
        return mXid;
    }

    public String getmPlaceName() {
        return mPlaceName;
    }

    public int getmPlaceRating() {
        return mPlaceRating;
    }

    public String getmLatt() {
        return mLatt;
    }

    public String getmLon() {
        return mLon;
    }
}
