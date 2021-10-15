package com.example.myapplication.chat_protocol;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Message implements Serializable {

    public static final byte BROADCAST = 0x00; // recipient type
    public static final byte SINGLE_REC = 0x01;

    public static final byte TYPE_TEXT = 0x10;
    public static final byte TYPE_MEDIA = 0x20;
    public static final byte TYPE_USERS = 0x30; // query
    public static final byte TYPE_QUERY = 0x40;
    public static final byte TYPE_STATUS = 0x04;

    public static final byte EOM = -0x01;

    public static final byte CODE_SUCCESSFUL = 0x00; // status codes
    public static final byte CODE_FAILED = 0x01;
    public static final byte CODE_PENDING = 0x02;
    public static final byte CODE_FOUND = 0x03;
    public static final byte CODE_NOT_FOUND = 0x04;
    public static final byte CODE_UNKNOWN_REQUEST = 0x05;
    public static final byte CODE_REQUEST = 0x06;
    public static final byte CODE_RESPONSE = 0x07;

    public static final int STATUS_SENT = 0x01;
    public static final int STATUS_RECEIVED = 0x02;
    public static final int STATUS_READ = 0x03;


    private String[] recipient;
    private String sender, messageTime, singleRecipient;
    private byte messageType, eom;
    private Data messageBody;
    private int recipientLength = 0, bodyLength = 0;
    private int recipientType;
    private int messageId;
    private int trackingFlags;

    public Message(String sender, String[] recipient, byte messageType, Data messageBody, String time) {
        this.sender = sender;
        this.recipientType = Message.BROADCAST;
        this.recipientLength = this.recipient.length;
        this.recipient = recipient;
        this.messageType = messageType;
        this.bodyLength = messageBody.getBodyLength();
        this.messageBody = messageBody;
        this.messageTime = time;
        this.eom = Message.EOM;
        this.messageId = this.hashCode();
    }

    public Message(String sender, String recipient, byte messageType, Data messageBody, String time) {
        this.sender = sender;
        this.recipientType = Message.SINGLE_REC;
        this.singleRecipient = recipient;
        this.messageType = messageType;
        this.bodyLength = messageBody.getBodyLength();
        this.messageBody = messageBody;
        this.messageTime = time;
        this.eom = Message.EOM;
        this.messageId = this.hashCode();
    }
    public Message() {
        this.eom = Message.EOM;
        this.messageId = this.hashCode();
    }


    public String getSender() {return this.sender;}
    public int getRecipientType(){return this.recipientType;}
    public int getRecipientLength() {return this.recipientLength;}
    public String[] getRecipient() {return this.recipient;}
    public String getRecipientS(){return this.singleRecipient;}
    public int getMessageType() {return this.messageType;}
    public int getBodyLength() {return this.bodyLength;}
    public Data getBody() {return this.messageBody;}
    public String getMessageTime() {return this.messageTime;}
    public byte getEOM() {return this.eom;}
    public int getMessageId(){return this.messageId;}
    public int getTrackingFlags(){return this.trackingFlags;}



    public void setTrackingFlags(int t){this.trackingFlags = t;}
    public void setSender(String sender) {this.sender = sender;}
    public void setRecipientType(byte recType){this.recipientType = (int)recType;}
    public void setRecipient(String[] recipient) {
        this.recipient = recipient;
        this.recipientLength = this.recipient.length;
    }
    public void setRecipientS(String recName){
        this.singleRecipient = recName;
        this.recipientLength = 1;
    }
    public void setMessageType(byte messageType) {this.messageType = messageType;}
    public void setBodyLength(int bodyLength) {this.bodyLength = bodyLength;}
    public void setBody(Data body) {this.messageBody = body;}
    public void setMessageTime(String time) {this.messageTime = time;}


    public void sendMessage(ObjectOutputStream out)throws IOException {
        out.writeUTF(this.sender);
        out.flush();
        out.writeInt(this.recipientType);
        out.flush();
        out.writeInt(this.recipientLength);
        out.flush();
        if(this.recipientType == Message.SINGLE_REC){
            out.writeUTF(this.singleRecipient);
            out.flush();
        }

        out.writeByte(this.messageType);
        out.flush();

        out.writeInt(this.messageBody.getBodyLength());
        out.flush();

        if(this.messageType == Message.TYPE_TEXT || this.messageType == Message.TYPE_QUERY){
            out.writeUTF(this.messageBody.getTextData());
            out.flush();
        }
        else if(this.messageType == Message.TYPE_USERS) {
            if(this.sender.equals("Server")){
                for(String s: this.messageBody.getUsersData()){
                    out.writeUTF(s);
                    out.flush();
                }
            }
        }
        else if(this.messageType == Message.TYPE_MEDIA){
            for(Integer i : this.messageBody.getData()) {
                out.writeInt(i);
                out.flush();
            }
        }

        out.writeUTF(this.messageTime);
        out.flush();

        out.writeInt(this.messageId);
        out.flush();

        out.writeInt(this.trackingFlags);
        out.flush();

        out.writeByte(Message.EOM);
        out.flush();

    }
    public void receiveMessage(ObjectInputStream input) throws Exception {

        int count = 0;
        boolean exit = false;

        while(!exit) {
            try {
                if(count < 11) {
                    if(input.available() > 0) {
                        switch(count) {
                            case 0: //Sender
                                this.sender = input.readUTF();
                                count ++;
                                break;
                            case 1: // recipient Type
                                this.recipientType  = input.readInt();
                                count ++;
                                break;
                            case 2: // recipient Length
                                this.recipientLength = input.readInt();
                                count ++;
                                break;
                            case 3: // recipient
                                if(this.recipientType == Message.SINGLE_REC){
                                    this.singleRecipient = input.readUTF();
                                }else if(this.recipientType == Message.BROADCAST){
                                    this.recipient = new String[this.recipientLength];
                                    for(int j=0; j<this.recipientLength; j++) {
                                        this.recipient[j] = input.readUTF();
                                        Thread.sleep(10);
                                    }
                                }
                                count ++;
                                break;
                            case 4: // Message Type
                                this.messageType = input.readByte();
                                count ++;
                                break;
                            case 5: //Message Length
                                this.bodyLength = input.readInt();
                                count++;
                                break;
                            case 6: // Message Body
                                if(this.messageType == Message.TYPE_TEXT || this.messageType == Message.TYPE_QUERY){ // Text Message
                                    Data temp = new Data(input.readUTF());
                                    this.setBody(temp);
                                }else if(this.messageType == Message.TYPE_USERS){ //User query
                                    if(this.sender.equals("Server")){
                                        String[] temp = new String[this.bodyLength];
                                        for(int i=0; i<this.bodyLength; i++){
                                            temp[i] = input.readUTF();
                                        }
                                        Data body = new Data(temp);
                                        body.setBodyLength(temp.length);
                                        this.setBody(body);
                                    }
                                }else if(this.messageType == Message.TYPE_MEDIA){}
                                count ++;
                                break;
                            case 7:
                                this.messageTime = input.readUTF();
                                count++;
                                break;
                            case 8:
                                this.messageId = input.readInt();
                                count++;
                                break;
                            case 9:
                                this.trackingFlags = input.readInt();
                                count++;
                                break;
                            case 10:
                                this.eom = input.readByte();
                                count ++;
                                exit = true;
                                break;
                        }
                    }
                }
            }catch(Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void setupStringMessage(String sender, String rec, String mes , String time) {
        this.sender = sender;
        this.recipientType = Message.SINGLE_REC;
        this.recipientLength = 1;
        this.singleRecipient = new String(rec);
        this.messageType = Message.TYPE_TEXT;
        this.bodyLength = 1;
        Data body;
        body = new Data(mes);
        body.setBodyLength(1);
        this.messageBody = body;
        this.messageTime = time;
        this.eom = Message.EOM;
    }

    public void setUpQueryMessage(String sender, String rec, String query){
        this.sender = sender;
        this.recipientType = SINGLE_REC;
        this.recipientLength = 1;
        this.singleRecipient = rec;
        this.messageType = Message.TYPE_QUERY;
        this.bodyLength = 1;
        this.messageBody = new Data(query);
        this.messageTime = "No time";
        this.eom = Message.EOM;
    }

    public void messageCopy(Message message){
        this.sender = new String(message.getSender());
        this.recipientType = message.recipientType;
        this.recipientLength = message.recipientLength;
        this.singleRecipient = message.singleRecipient;
        this.messageType = message.messageType;
        this.bodyLength = message.bodyLength;
        this.messageBody = new Data(message.getBody().getTextData());
        this.messageTime = new String(message.messageTime);
        this.messageId = message.messageId;
        this.trackingFlags = message.trackingFlags;
        this.eom = message.eom;
    }

    public void printMessage() {
        System.out.println("Sender: "+this.sender);
        System.out.println("Recipient Type: "+this.recipientType);
        System.out.println("Recipient Length: "+this.recipientLength);
        System.out.println("Recipient: "+this.singleRecipient);
        System.out.println("Message Type: "+ this.messageType);
        System.out.println("Body Length: "+this.getBody().getBodyLength());
        System.out.println("Body: "+this.getBody().getTextData());
        System.out.println("Time: "+this.messageTime);
        System.out.println("ID: "+this.messageId);
        System.out.println("Status: "+this.trackingFlags);
        System.out.println("End of Message: "+this.eom);
    }
}
