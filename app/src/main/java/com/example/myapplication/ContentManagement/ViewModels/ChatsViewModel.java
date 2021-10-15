package com.example.myapplication.ContentManagement.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.ContentManagement.Repository.CentralFetch;
import com.example.myapplication.chat_protocol.Message;
import com.example.myapplication.chat_protocol.User;

import java.util.ArrayList;

public class ChatsViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<Message>> messages;
    private int position = 0;
    private ArrayList<Message>messageHistory;
    private User currentUser;

    public ChatsViewModel(){
        messages = new MutableLiveData<>();
    }

    public MutableLiveData<ArrayList<Message>> getMessages(){
        update();
        messages.setValue(this.messageHistory);
        return messages;
    }

    public int getPosition(){return this.position;}
    public void setPosition(int p){
        this.position = p;
    }
    public void setCurrentUser(){
        currentUser = CentralFetch.getAllUsers().get(position);
        currentUser.setUnreadMessages(0); // clear unread messages
    }

    public User getCurrentUser(){return this.currentUser;}

    public void update(){
        currentUser = CentralFetch.getAllUsers().get(this.position);
        messageHistory = currentUser.getHistory();
        messages.setValue(messageHistory);
    }
    public void loadData(){
        CentralFetch.setCurrentUserUpdate(currentUser, messages);
    }


}
