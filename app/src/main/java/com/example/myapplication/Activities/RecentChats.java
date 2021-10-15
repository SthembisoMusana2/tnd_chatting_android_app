package com.example.myapplication.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapters.RecentListAdapter;
import com.example.myapplication.ContentManagement.Repository.CentralFetch;
import com.example.myapplication.ContentManagement.ViewModels.UsersModel;
import com.example.myapplication.R;
import com.example.myapplication.chat_protocol.Message;
import com.example.myapplication.chat_protocol.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

@SuppressWarnings("ALL")
public class RecentChats extends AppCompatActivity {

    private ArrayList<User> allUsers;
    private String name, email;
    private RecentListAdapter adapter;

    private RecyclerView recyclerView;
    private UsersModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_chats);


        recyclerView = findViewById(R.id.id_rec_users_recycle_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        viewModel = new ViewModelProvider(this).get(UsersModel.class);
        viewModel.getUsers().observe(this, users -> {
            adapter = new RecentListAdapter(this, users, name, email);
            recyclerView.setAdapter(adapter);
        });
        Toolbar toolbar  = findViewById(R.id.id_recent_users_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
            actionBar.setTitle("The Nerd Department");
        }

        FloatingActionButton fb = findViewById(R.id.id_add_user);
        fb.setOnClickListener(view -> {
//            User polite = new User("Polite", "Sthembisomusana2@gmail.com");
//            CentralFetch.getAllUsers().add(polite);
//            viewModel.update();
            Intent addRecipient = new Intent(getApplicationContext(), SearchUsers.class);
            addRecipient.putExtra(MainActivity.EXTRA_USER_NAME, CentralFetch.getOwner().getUserName());
            startActivity(addRecipient);
        });
    }

    @Override
    protected void onResume() {
        viewModel.update();
        CentralFetch.setCurrentUser(null);
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.recent_chat_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.id_recent_settings){
            Intent settings = new Intent(this, SettingsActivity.class);
            startActivity(settings);
        }
        else if(id == R.id.id_recent_menu_profile){
            Intent profile = new Intent(this, UserProfileActivity.class);
            startActivity(profile);
        }

        return super.onOptionsItemSelected(item);
    }
}