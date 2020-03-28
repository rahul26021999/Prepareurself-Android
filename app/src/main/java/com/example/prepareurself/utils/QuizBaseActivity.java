package com.example.prepareurself.utils;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class QuizBaseActivity extends AppCompatActivity {

    Socket socket;
    public static final String SOCKETSERVERURL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            socket = IO.socket(SOCKETSERVERURL);
        }catch (URISyntaxException e){
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket(){
        return socket;
    }

}
