package com.example.myapplication.Misc;

import com.example.myapplication.R;

public class UsersModel {
    private String name;
    private Integer image = R.drawable.user_avar;
    private Integer selected;
    public UsersModel(String name, Integer image){
//        this.image = image;
        this.name = name;
    }

    public String getName(){return this.name;}
    public Integer getImageRef(){return this.image;}

}
