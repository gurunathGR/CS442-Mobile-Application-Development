package com.christopherhield.classsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";

    private EditText username;
    private EditText password;
    private TextView loginMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.usernameText);
        password = findViewById(R.id.passwordText);
        loginMsg = findViewById(R.id.loginMsg);

    }

    public void buttonPressed(View v) {


        String uName = username.getText().toString();
        String pWord = password.getText().toString();

        if (uName.trim().equals("user1") && pWord.trim().equals("pass1")) {
            loginMsg.setText("Hello user! Welcome!");
        } else {
            loginMsg.setText("Login failed");
        }




    }
}
