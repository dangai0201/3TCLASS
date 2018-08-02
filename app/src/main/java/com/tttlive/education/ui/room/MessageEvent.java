package com.tttlive.education.ui.room;

/**
 * Created by mr.li on 2018/3/16.
 */

public class MessageEvent{
    private String message;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public  MessageEvent(  String type ,String message){
        this.message=message;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

