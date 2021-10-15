package com.example.myapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.myapplication.Adapters.AddRecipientAdapter;
import com.example.myapplication.R;
import com.example.myapplication.Misc.UsersModel;

import java.util.ArrayList;

public class Recipient extends AppCompatActivity {
    protected RecyclerView recView;
    protected AddRecipientAdapter adapter;
    protected RecyclerView.LayoutManager layout_manager;
    protected ArrayList<UsersModel> data = new ArrayList<UsersModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipient);

        recView = (RecyclerView) findViewById(R.id.id_recyclerview);

        layout_manager = new LinearLayoutManager(this);
        recView.setLayoutManager(layout_manager);
        adapter = new AddRecipientAdapter(this, data);
        recView.setAdapter(adapter);
    }


}