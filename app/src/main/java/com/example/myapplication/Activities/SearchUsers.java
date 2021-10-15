package com.example.myapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.ContentManagement.Repository.CentralFetch;
import com.example.myapplication.R;
import com.example.myapplication.chat_protocol.Message;
import com.example.myapplication.chat_protocol.User;

public class SearchUsers extends AppCompatActivity {

    private Message mQueryMessage;
    private String senderName = null;
    private EditText searchText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_users);

        Button go = findViewById(R.id.id_search_go);
        searchText = findViewById(R.id.id_search_text);
        mQueryMessage = new Message();
        Intent a = getIntent();
        senderName = a.getStringExtra(MainActivity.EXTRA_USER_NAME);

        go.setOnClickListener(view->{
            String mSearch = searchText.getText().toString();
            mQueryMessage.setUpQueryMessage(senderName, "Server", mSearch.trim());
            CentralFetch.sendMessage(mQueryMessage);
//            mQueryMessage.printMessage();
            Intent av = new Intent(this, RecentChats.class);
            startActivity(av);
        });
    }


}