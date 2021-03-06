package com.cukorders.helping;
import android.widget.ImageView;

import java.util.ArrayList;

public class InitPost {
    private String title;
    private String image1;
    private String endTime;
    private String place;
    private String chat_number;
    private String likes;


    public InitPost(String title, String sample_image, ImageView post_image, String place, String time, String chat_number, String likes) {
        this.title = title;
        this.image1 = image1;
        this.place = place;
        this.endTime = endTime;
        this.chat_number = chat_number;
        this.likes = likes;
    }

    public InitPost(){

    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }


    public String getChat_number() {
        return chat_number;
    }

    public void setChat_number(String chat_number) {
        this.chat_number = chat_number;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

}