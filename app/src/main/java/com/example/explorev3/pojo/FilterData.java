package com.example.explorev3.pojo;

import java.util.ArrayList;

public class FilterData {
    private String radius;
    private String latt;
    private String lon;
    private ArrayList<String> checkResult;

    public FilterData(String radius, String latt, String lon, ArrayList<String> checkResult) {
        this.radius = radius;
        this.latt = latt;
        this.lon = lon;
        this.checkResult = checkResult;
    }

    public String getRadius() {
        return radius;
    }

    public String getLatt() {
        return latt;
    }

    public String getLon() {
        return lon;
    }

    public ArrayList<String> getCheckResult() {
        return checkResult;
    }
}
