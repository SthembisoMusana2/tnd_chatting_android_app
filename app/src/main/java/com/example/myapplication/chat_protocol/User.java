package com.example.myapplication.chat_protocol;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class User implements Serializable {
    public static Queue<User> updates = new LinkedList<>();
    private int unreadMessages;

    private String name, email;
    private ArrayList<User> cached = new ArrayList<User>(); // this is where we log our recent users
    private ArrayList<Message> messageHistory = new ArrayList<Message>(); // this is a history logger	// we need a message queue to relay the messages to the client connected to this node...
    private Queue<Message> messageQueue = new LinkedList<Message>(); // this is gonna be our message queue
    private Queue<Message> receivedQueue = new LinkedList<Message>();
    public User(String name, String email) {
        this.email = email;
        this.name = name;
        this.unreadMessages = 0;
        // append the user to the list of all users
    }

    public String getUserName() {
        return this.name;
    }
    public String getUserEmail() {
        return this.email;
    }
    public ArrayList<User> getCache(){
        return this.cached;
    }
    public Queue<Message> getMessageQueue(){
        return this.messageQueue;
    }
    public Queue<Message> getReceivedMessageQueue(){return this.receivedQueue;}
    public void setUnreadMessages(int j){this.unreadMessages = j;}
    public int getUnreadMessages(){return this.unreadMessages;}
    public void appendReceivedMessage(Message message){
        messageHistory.add(message);
        this.receivedQueue.add(message);
    }
    public ArrayList<Message> getHistory(){
        return this.messageHistory;
    }

    public boolean appendMessage(Message message) {
        messageHistory.add(message);
        return this.messageQueue.add(message);
    }
    public void handshake(ObjectInputStream in, ObjectOutputStream out)throws Exception {
        out.writeUTF(name);
        out.flush();
        Thread.sleep(100);
        out.writeUTF(email);
        out.flush();
        System.out.println("Response: "+in.readUTF());
    }
    public void update(){
        if(!User.updates.isEmpty()){
            User temp = User.updates.remove();
            if(temp.getUserName().equals(this.name)){
                this.messageHistory.clear();
                this.messageHistory = new ArrayList<>(temp.getHistory());
            }else{
                User.updates.add(temp); // put it back
            }
        }
    }
    public void sendMessage(Message message, ObjectOutputStream out)throws Exception {
        message.sendMessage(out);
    }
}