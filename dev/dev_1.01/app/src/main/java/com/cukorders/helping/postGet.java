package com.cukorders.helping;

import java.io.Serializable;

public class postGet implements Serializable {
    private String isFinished;

    public postGet(String isFinished){
        this.isFinished= isFinished;
    }

    public postGet(){

    }

    public String getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(String isFinished) {
        this.isFinished = isFinished;
    }
}

