package com.example.myapplication.Misc;

public class Chat {
    private String messageBody, timeStamp, owner;

    public Chat(String owner, String timeStamp, String messageBody){
        this.messageBody=  messageBody;
        this.timeStamp = timeStamp;
        this.owner = owner;
    }

    public String getOwner(){return this.owner;}
    public String getMessageBody(){return this.messageBody;}
    public String getTimeStamp(){return this.timeStamp;}

}
