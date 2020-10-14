package com.baroque.lujo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.baroque.lujo.R;

import com.baroque.lujo.activities.login.UserModel;
import utilities.Constants;

import static utilities.Constants.ACTION_TO_PERFORM;
import static utilities.Constants.INTENT_FIRST_PARAM;
import static utilities.Constants.PREF_FILE_NAME;
import static utilities.Constants.SPLASH_SCREEN_TIME_OUT;
import static utilities.Key.KEY_CURRENT_USER;
import static utilities.Utility.getSavedObjectFromPreference;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.myLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                //Intent is used to switch from one activity to another.
                Intent intent = new Intent(SplashActivity.this, PreLoginActivity.class);
                //Checking if user is already logged in or not
                UserModel user = getSavedObjectFromPreference(SplashActivity.this,PREF_FILE_NAME,KEY_CURRENT_USER, UserModel.class);
                if (user != null ){
                    //user is already logged in
                    //Navigate to Welcome screen;
                    intent = new Intent(SplashActivity.this, WelcomeActivity.class);
                    intent.putExtra(ACTION_TO_PERFORM, Constants.PAGE_TYPE.SIGN_IN); //show the message "welcome back"
                    intent.putExtra(INTENT_FIRST_PARAM, user);
                }
                //invoke the SecondActivity which could be prelogin (if not signed in) or welcome (if signed in)
                startActivity(intent);
                //the current activity will get finished.
                finish();
            }
        }, SPLASH_SCREEN_TIME_OUT * 1000);      //After completion of SPLASH_SCREEN_TIME_OUT Seconds, the next activity will get started.
    }
}