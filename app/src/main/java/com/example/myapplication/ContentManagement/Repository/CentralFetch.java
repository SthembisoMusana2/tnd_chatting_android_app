package com.example.myapplication.ContentManagement.Repository;

import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.chat_protocol.Message;
import com.example.myapplication.chat_protocol.User;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CentralFetch {
    private static final ArrayList<User> allUsers = new ArrayList<>();
    private static FetchData fetchData;
    private static final Queue<Message> sendingMessageQueue = new LinkedList<>();
    private static ExecutorService thread;
    private static int POLLING_STATE = 0;
    private static MutableLiveData<ArrayList<User>> userLiveData;
    private static User currentUser;
    private static MutableLiveData<ArrayList<Message>> mCurrentUserMessages;
    public static int CONNECTION_STATE = 0;

    public static void startPolling(String host, int port){ // Fetch data and update the allUsers list
        if(CentralFetch.POLLING_STATE == 0){
            CentralFetch.POLLING_STATE = 1;
            fetchData = new FetchData(host, port, sendingMessageQueue);
            thread = Executors.newFixedThreadPool(1);
            fetchData.setOnReceivedListener(new MessageCallback() {
                @Override
                public void onReceived(Message message) {
                    if(message.getMessageType() == Message.TYPE_QUERY){
                        String body = message.getBody().getTextData();
                        String[] spilt = body.split(":");
                        for(User temp: allUsers){
                            if(temp.getUserName().equals(spilt[0])){
                                return;
                            }
                        }
                        User tempUser = new User(spilt[0], spilt[1]);
                        allUsers.add(tempUser);
                        userLiveData.postValue(allUsers);
                    }
                    else if(message.getMessageType() == Message.TYPE_TEXT){
                        for(User temp:allUsers){
                            if(temp.getUserName().equals(message.getSender())){
                                Message tempMessage = new Message();
                                tempMessage.messageCopy(message);
                                temp.getHistory().add(tempMessage);
                                int badgeV = temp.getUnreadMessages()+1;
                                temp.setUnreadMessages(badgeV);
                                userLiveData.postValue(allUsers);
                                if(currentUser != null){
                                    if(temp.getUserName().equals(currentUser.getUserName())){
                                        currentUser.setUnreadMessages(0);
                                        mCurrentUserMessages.postValue(temp.getHistory());
                                    }
                                }
                                return;
                            }
                        }
                        User temp = new User(message.getSender(), "No email");
                        Message tempMessage = new Message();
                        tempMessage.messageCopy(message);
                        temp.getHistory().add(tempMessage);
                        allUsers.add(temp);
                        userLiveData.postValue(allUsers);
                    }
                    else if(message.getMessageType() == Message.TYPE_STATUS){

                        System.out.println("Response Received");
                        // find the message that this one is responding to, update it
                        //1 Search for the user
                        //2 find the messages from the users lists using Id
                        //3 update the flag
                        if(message.getTrackingFlags() == Message.STATUS_SENT){
                            for(User user: allUsers){
                                if(user.getUserName().equals(message.getRecipientS())){
                                    for(int i=user.getHistory().size()-1; i>=0; i--){
                                        if(message.getMessageId() ==
                                                user.getHistory().get(i).getMessageId()){

                                            user.getHistory()
                                                    .get(i)
                                                    .setTrackingFlags(message.getTrackingFlags());
                                            System.out.println("I found the message From the Server");
                                        }
                                    }
                                }
                            }
                        }else{
                            for(User user: allUsers){
                                if(user.getUserName().equals(message.getSender())){
                                    for(int i=user.getHistory().size()-1; i>=0; i--){
                                        if(message.getMessageId() ==
                                                user.getHistory().get(i).getMessageId()){

                                            user.getHistory()
                                                    .get(i)
                                                    .setTrackingFlags(message.getTrackingFlags());
                                            System.out.println("I found the message");
                                        }
                                    }
                                }
                            }
                        }


                    }
                }
            });
            thread.execute(fetchData); // send data
        }
    }

    public static void stopPolling(){
        try{
            if(fetchData.getInStream() != null)fetchData.getInStream().close();
            if(fetchData.getOutStream() != null)fetchData.getOutStream().close();
            if(fetchData.getSocket() != null)fetchData.getSocket().close();
        }catch (Exception e){
            e.printStackTrace();
        }
        thread.shutdownNow();
        POLLING_STATE = 0;
    }

    public static ArrayList<User> getAllUsers(){
        return allUsers;
    }

    public static void sendMessage(Message m){
        Message temp = new Message();
        temp.messageCopy(m);
        sendingMessageQueue.add(temp);
    }

    public static void setLiveData(MutableLiveData<ArrayList<User>> users){
        userLiveData = users;
    }

    public static void setCurrentUserUpdate(User u, MutableLiveData<ArrayList<Message>> messages){
        currentUser = u;
        mCurrentUserMessages = messages;
    }

    public static User getOwner(){
        return fetchData.getOwner();
    }

    public static void setOwner(User user){
        fetchData.setOwner(user);
    }
    public static void setOwner(String username, String email){
       fetchData.setOwner(username, email);
    }

    public static void setCurrentUser(User user){
        currentUser = user;
    }

    private static void init(){
        for(int i=0; i<10; i++){
            User temp = new User("Sthembiso "+i,"Musana "+i);
            allUsers.add(temp);
        }
    }
}
