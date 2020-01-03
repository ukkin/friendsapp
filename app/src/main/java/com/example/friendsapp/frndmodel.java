package com.example.friendsapp;

public class frndmodel {

    String title;
    String desc;
    int icon;

    //constructor
    public frndmodel(String title, String desc, int icon) {
        this.title = title;
        this.desc = desc;
        this.icon = icon;
    }

    //getters


    public String getTitle() {
        return this.title;
    }

    public String getDesc() {
        return this.desc;
    }

    public int getIcon() {
        return this.icon;
    }
}

