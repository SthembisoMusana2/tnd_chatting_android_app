package com.example.myapplication.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class NetworkCom extends Service {
    public NetworkCom() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}