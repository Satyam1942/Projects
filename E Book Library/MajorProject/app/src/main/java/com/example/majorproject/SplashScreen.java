package com.example.majorproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {
FirebaseUser mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Handler handler = new Handler();
        mUser = FirebaseAuth.getInstance().getCurrentUser();


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
        if(mUser!=null)
        {
        Intent intent = new Intent(SplashScreen.this,Home.class);
        startActivity(intent);
        finish();
        }  else{

            Intent intent = new Intent(SplashScreen.this,MainActivity.class);
            startActivity(intent);
            finish();
        }

            }
        },1000);
    }
}