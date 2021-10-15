package com.example.myapplication.ContentManagement.Repository;

import com.example.myapplication.chat_protocol.Message;

public interface MessageCallback {
    void onReceived(Message message);
}
