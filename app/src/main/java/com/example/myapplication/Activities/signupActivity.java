package com.example.myapplication.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.ContentManagement.Repository.CentralFetch;
import com.example.myapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class signupActivity extends AppCompatActivity {

    private EditText emailView;
    private EditText nameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emailView = findViewById(R.id.id_email);
        nameView = findViewById(R.id.id_name);

        FloatingActionButton fb = findViewById(R.id.id_signup_send);
        fb.setOnClickListener(view -> {
                String name, email;
                name = nameView.getText().toString().trim();
                email = emailView.getText().toString().trim();

//                String host = "192.168.0.100";
                String host  = "192.168.43.62";
//                String host = "10.50.2.248";
                int port = 9090;

                CentralFetch.startPolling(host, port);
                CentralFetch.setOwner(name, email);

                if(name.length() > 0 && email.length() > 0){
                    Intent main = new Intent(getApplicationContext(), RecentChats.class);
                    startActivity(main);
                }else{
                    Snackbar.make(view, "Please enter details", Snackbar.LENGTH_SHORT).show();
                }
            });
    }

}