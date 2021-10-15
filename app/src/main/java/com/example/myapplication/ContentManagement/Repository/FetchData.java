package com.example.myapplication.ContentManagement.Repository;

import com.example.myapplication.chat_protocol.Message;
import com.example.myapplication.chat_protocol.User;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Queue;

public class FetchData implements Runnable{
    private Socket socket;
    private final int port;
    private final String host;
    private final Message receivedMessage;

    private ObjectInputStream inputStream = null;
    private ObjectOutputStream outputStream = null;
    private MessageCallback callback;
    private final Queue<Message> sendingMessageQueue;
    private User owner;

    public FetchData(String host, int port, Queue<Message>sMessageQueue){
        this.port = port;
        this.host = host;
        this.receivedMessage = new Message();
        this.sendingMessageQueue = sMessageQueue;
    }
    
    public Socket getSocket(){return this.socket;}
    public ObjectInputStream getInStream(){return this.inputStream;}
    public ObjectOutputStream getOutStream(){return this.outputStream;}
    public void setOnReceivedListener(MessageCallback callback){
        this.callback = callback;
    }
    public void setOwner(User user){
        this.owner  = new User(user.getUserName(), user.getUserEmail());
    }
    public void setOwner(String username, String email){
        this.owner  = new User(username, email);
    }
    public User getOwner(){return this.owner;}
    private void connect(){
        while(CentralFetch.CONNECTION_STATE == 0){
            try{
                socket = new Socket(host,port);
                CentralFetch.CONNECTION_STATE = 1;
                inputStream = new ObjectInputStream(socket.getInputStream());
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                System.out.println("Connected");
            }catch (Exception e){
                System.out.println("Error: Failed to connect\nRetrying");
            }
        }
    }

    @Override
    public void run(){
        connect();
        try{
            this.owner.handshake(inputStream, outputStream);
            new Thread(()->{
                try{
                    while(true){
                        receivedMessage.receiveMessage(inputStream);
                        this.callback.onReceived(receivedMessage);
                        System.out.println("Received Message: ");
                        receivedMessage.printMessage();

                        //Response object
                        if(receivedMessage.getMessageType() == Message.TYPE_TEXT){
                            Message resp = new Message();
                            resp.messageCopy(receivedMessage);
                            resp.setMessageType(Message.TYPE_STATUS);
                            resp.setTrackingFlags(Message.STATUS_RECEIVED);
                            String rec = receivedMessage.getRecipientS();
                            resp.setRecipientS(receivedMessage.getSender());
                            resp.setSender(rec);
                            sendingMessageQueue.add(resp);
                        }

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }).start();

            while(true) {
                if (!sendingMessageQueue.isEmpty()) {
                    // send the message
                    Message sentMessage = sendingMessageQueue.remove();
                    sentMessage.printMessage();
                    sentMessage.sendMessage(outputStream); // the message has no left the app
                    System.out.println("Sent");
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
