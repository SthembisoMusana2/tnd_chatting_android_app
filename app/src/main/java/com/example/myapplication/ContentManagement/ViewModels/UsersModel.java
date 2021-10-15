package com.example.myapplication.ContentManagement.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.ContentManagement.Repository.CentralFetch;
import com.example.myapplication.chat_protocol.Message;
import com.example.myapplication.chat_protocol.User;

import java.util.ArrayList;

public class UsersModel extends ViewModel {

    private final  MutableLiveData<ArrayList<User>> liveUserData;
    private ArrayList<User> users;

    public UsersModel(){
        liveUserData = new MutableLiveData<>();
    }

    public MutableLiveData<ArrayList<User>> getUsers(){
        fetchData();
        liveUserData.setValue(this.users);
        return this.liveUserData;
    }

    public void setAllUsers(ArrayList<User> allUsers){
        this.users = allUsers;
    }

    public ArrayList<User> getUsersList(){return this.users;}

    public void fetchData(){
        CentralFetch.setLiveData(liveUserData);
        this.users = CentralFetch.getAllUsers();
    }
    public void update(){

        liveUserData.setValue(CentralFetch.getAllUsers());
    }


}
