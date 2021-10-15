package com.example.myapplication.chat_protocol;

import java.io.Serializable;

public class Data  implements Serializable {
    private static final byte TEXT = 0x00;

    private static final byte OTHERTYPE = 0x01;
    private String data;
    private String[] users;
    private Integer[] otherType;
    private byte dataType;
    private int bodyLength;

    public Data(String text) {
        this.data = text;
        this.dataType = Data.TEXT;
        this.bodyLength  = 0;
    }

    public Data(Integer[] other) {
        this.otherType = other;
        dataType = Data.OTHERTYPE;
        this.bodyLength = other.length;
    }

    public Data(String[] users){
        this.users = users;
        this.bodyLength = users.length;
        this.dataType = Message.TYPE_USERS;
    }
    public Data() {
        this.data = null;
        this.otherType = null;
        this.bodyLength = -1;
    }

    public Integer[] getData() {return this.otherType;}
    public String getTextData() {return this.data;}
    public byte getDataType() {return this.dataType;}
    public String[] getUsersData(){return this.users;}
    public int getBodyLength() {return this.bodyLength;}
    public void setData(Integer[] data) {this.otherType = data;}
    public void setTextData(String text) {this.data = text;}
    public void setDataType(byte type) {this.dataType = type;}
    public void setBodyLength(int len) {this.bodyLength = len;}
    public void setUserData(String[] users){
        this.users = users;
        this.bodyLength = users.length;
        this.dataType = Message.TYPE_USERS;
    }


}
