package com.cukorders.helping;
import android.widget.ImageView;

import java.io.Serializable;
import java.util.ArrayList;

public class InitPost implements Serializable {
    private String title;
    private String image1;
    private String endTime;
    private String place;
    private String chat_number;
    private String likes;
    private String postKey;
    private String uid;
    private String isSended;
    private String isFinished;
    private String isMatched;
    private String location;
    private String category;
    private String gender;
    private String age;
    private String clientGender;



    public InitPost(String title, String image1, String endTime, String place, String chat_number, String likes, String postKey, String uid, String isSended, String isFinished, String isMatched, String location, String category, String age, String gender, String clientGender
    ) {
        this.title = title;
        this.image1 = image1;
        this.place = place;
        this.endTime = endTime;
        this.chat_number = chat_number;
        this.likes = likes;
        this.postKey = postKey;
        this.uid = uid;
        this.isSended = isSended;
        this.isFinished = isFinished;
        this.isMatched = isMatched;
        this.location = location;
        this.category = category;
        this.gender = gender;
        this.age = age;
        this.clientGender = clientGender;
    }

    public InitPost(){

    }

    public String getClientGender() {
        return clientGender;
    }

    public void setClientGender(String clientGender) {
        this.clientGender = clientGender;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(String isFinished) {
        this.isFinished = isFinished;
    }

    public String getIsMatched() {
        return isMatched;
    }

    public void setIsMatched(String isMatched) {
        this.isMatched = isMatched;
    }

    public String getIsSended() {
        return isSended;
    }

    public void setIsSended(String isSended) {
        this.isSended = isSended;
    }
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
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