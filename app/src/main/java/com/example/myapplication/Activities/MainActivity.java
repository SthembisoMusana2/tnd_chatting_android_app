package com.example.myapplication.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapters.ChatBubblesAdapter;
import com.example.myapplication.ContentManagement.Repository.CentralFetch;
import com.example.myapplication.ContentManagement.ViewModels.ChatsViewModel;
import com.example.myapplication.ContentManagement.ViewModels.UsersModel;
import com.example.myapplication.R;
import com.example.myapplication.chat_protocol.Data;
import com.example.myapplication.chat_protocol.Message;
import com.example.myapplication.chat_protocol.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.myapplication.MESSAGE";
    public static final String EXTRA_USER_NAME = "com.example.myapplication.USER_NAME";
    public static final String EXTRA_EMAIL = "com.example.myapplication.EMAIL";
    public static final String EXTRA_USER_REF = "com.example.myapplication.USER_REF";
    public static final String EXTRA_USER_POSITION = "com.example.myapplication.USER_POSITION";

    private ChatBubblesAdapter cAdapter;
    private Message message;
    private String timeCleanup;

    private EditText editText;
    private RecyclerView recView;
    private ChatsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        FloatingActionButton sendButton = findViewById(R.id.button);
        editText = findViewById(R.id.messageBox);


        if(actionBar != null)
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent  = getIntent();

        if(intent.hasExtra(MainActivity.EXTRA_USER_POSITION)){
            int position  = intent.getIntExtra(MainActivity.EXTRA_USER_POSITION, -1);

            recView = findViewById(R.id.id_chat_recycler_view);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recView.setLayoutManager(layoutManager);

            viewModel = new ViewModelProvider(this).get(ChatsViewModel.class);
            viewModel.setPosition(position);
            viewModel.setCurrentUser();
            viewModel.loadData();
            if(actionBar != null) {
                actionBar.setTitle(viewModel.getCurrentUser().getUserName());
                actionBar.setDisplayShowTitleEnabled(true);
            }
            viewModel.getMessages().observe(this, messages -> {
                // Update the User Interface
                cAdapter = new ChatBubblesAdapter(this, messages, CentralFetch.getOwner());
                recView.setAdapter(cAdapter);
                if(messages.size()>0) recView.scrollToPosition(messages.size()-1);
            });


            sendButton.setOnClickListener(view -> {
                String messageText = editText.getText().toString();
                Calendar calendar = Calendar.getInstance();
                String minutes;
                String hours;

                hours = calendar.get(Calendar.HOUR_OF_DAY)<10?"0"+calendar.get(Calendar.HOUR_OF_DAY)
                        :""+calendar.get(Calendar.HOUR_OF_DAY);
                minutes = calendar.get(Calendar.MINUTE)<10?"0"+calendar.get(Calendar.MINUTE)
                        :""+calendar.get(Calendar.MINUTE);

                timeCleanup = hours+":"+minutes;

                 // get time
                if(!messageText.trim().isEmpty()){
                    Data messageBody = new Data(messageText);
                    message = new Message();
                    message.setupStringMessage(CentralFetch.getOwner().getUserName(),
                            viewModel.getCurrentUser().getUserName(),
                            messageText.trim(),timeCleanup);
                    viewModel.getCurrentUser().getHistory().add(message);
                    viewModel.getMessages().setValue(viewModel.getCurrentUser().getHistory());
                    CentralFetch.sendMessage(message);
                    recView.scrollToPosition(viewModel.getCurrentUser().getHistory().size()-1);
                    editText.setText("");
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.user_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
        }

        return super.onContextItemSelected(item);
    }
}