package com.baroque.lujo.activities.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.baroque.lujo.R;
import com.baroque.lujo.databinding.ActivityPreLoginBinding;

import java.util.Timer;
import java.util.TimerTask;

import utilities.Constants;

import static utilities.Constants.ACTION_TO_PERFORM;

public class PreLoginActivity extends AppCompatActivity implements View.OnClickListener{
    //After completion of 2 Seconds, the next activity will get started.
    private static int BACKGROUND_SLIDE_INTERVAL = 3;
//    LinearLayout llTop;
    String mLLTop_bg = "pre_login_bg_";
    int i = 1;  // to count the sliding images
//    Button btnSignIn, btnSignUp;
    private ActivityPreLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_pre_login);
        binding.setLifecycleOwner(this);
        binding.executePendingBindings();
        initializeControls();

        //Changing background in round robin
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                i = i % 7 ;
                final String imageName  = mLLTop_bg + ( i + 1 ) ;
//                System.out.println(imageName);
                final int resID = getResources().getIdentifier(imageName , "drawable", getPackageName());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Stuff that updates the UI
                        binding.llTop.setBackgroundResource(resID);
                    }
                });
                i += 1;
            }

        }, 0, BACKGROUND_SLIDE_INTERVAL * 1000);
    }

    //this method initializes all the controls, events which are defined on XML file
    private void initializeControls(){
        //An other way to programmatically declaring an event handler
        binding.btnSignIn.setOnClickListener(this);
        binding.btnSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        Intent intent = new Intent(PreLoginActivity.this, LoginActivity.class);
        switch (view.getId()) {
            case R.id.btnSignIn:
                intent.putExtra(ACTION_TO_PERFORM, Constants.PAGE_TYPE.SIGN_IN);
                break;
            case R.id.btnSignUp:
                intent.putExtra(ACTION_TO_PERFORM,Constants.PAGE_TYPE.SIGN_UP);
                break;
        }
        startActivity(intent);
    }

}