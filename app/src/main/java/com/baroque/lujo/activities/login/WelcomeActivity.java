package com.baroque.lujo.activities.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.baroque.lujo.R;
import com.baroque.lujo.activities.HomeActivity;
import com.baroque.lujo.databinding.ActivityWelcomeBinding;

import utilities.Constants;

import static utilities.Constants.INTENT_FIRST_PARAM;
//import static utilities.Constants.INTENT_SECOND_PARAM;
import static utilities.Constants.SPLASH_SCREEN_TIME_OUT;

public class WelcomeActivity extends AppCompatActivity {
    private ActivityWelcomeBinding binding;
    private UserModel user;
    Constants.PAGE_TYPE pageType; //signIn or signUp

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_welcome);

        Intent intent = getIntent();
        this.pageType = (Constants.PAGE_TYPE) intent.getSerializableExtra(Constants.ACTION_TO_PERFORM); //sign up is default
        user = (UserModel)intent.getSerializableExtra(INTENT_FIRST_PARAM); //Reading phone number to which OTP was sent
        if (user != null && user.getFirstName() != null && user.getLastName() != null) {    //user will be null if user is coming after login and not from signup
            binding.tvFirstAndSecondName.setText(user.getFirstName() + " " + user.getLastName()); // if first name and last name doesnt exist then it was showing "Lujo Member"
        }else{
            //firstname and last name will contain "Lujo Member" as name wasnt known
        }
        //Deciding to show Welcome or welcome back
        if (pageType == Constants.PAGE_TYPE.SIGN_IN) {
            binding.tvWelcome.setText("Welcome back");
            binding.llAnimations.setVisibility(View.GONE);  //Animations linear layout and next button is disappeared
            //closing this activity and opening new activity
            new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    //invoke the SecondActivity.
                    startActivity(intent);
                }
            }, SPLASH_SCREEN_TIME_OUT * 1000);
        }else{
            binding.tvWelcome.setText("Welcome to LUJO"); //concatenated to LUJO to welcome so final welcome message is "welcome to LUJO"
            //First name and last name will contain "Lujo Member" because in this else code code is falling as first and last name wasn't found
            binding.llAnimations.setVisibility(View.VISIBLE);  //Animations linear layout and next button is visible
            binding.btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
            });
        }
    }

    protected void onResume() {
        super.onResume();
        //Prepare for animation
        binding.imgWine.setAlpha(0.0f);
        binding.imgWine.setVisibility(View.VISIBLE);

        binding.imgWatch.setAlpha(0.0f);
        binding.imgWatch.setVisibility(View.VISIBLE);

        binding.imgTravel.setAlpha(0.0f);
        binding.imgTravel.setVisibility(View.VISIBLE);

        binding.imgSports.setAlpha(0.0f);
        binding.imgSports.setVisibility(View.VISIBLE);

        binding.imgFood.setAlpha(0.0f);
        binding.imgFood.setVisibility(View.VISIBLE);

        binding.imgAviation.setAlpha(0.0f);
        binding.imgAviation.setVisibility(View.VISIBLE);

        //start the animation
        binding.imgWine.animate().alpha(1.0f).setDuration(300).setStartDelay(150);
        binding.imgWatch.animate().alpha(1.0f).setDuration(300).setStartDelay(300);
        binding.imgTravel.animate().alpha(1.0f).setDuration(300).setStartDelay(450);
        binding.imgSports.animate().alpha(1.0f).setDuration(300).setStartDelay(600);
        binding.imgFood.animate().alpha(1.0f).setDuration(300).setStartDelay(750);
        binding.imgAviation.animate().alpha(1.0f).setDuration(300).setStartDelay(900)
            .setListener(new Animator.AnimatorListener() {
              @Override
              public void onAnimationStart(Animator animator) {              }

              @Override
              public void onAnimationEnd(Animator animator) {
                    binding.btnNext.setEnabled(true);
              }

              @Override
              public void onAnimationCancel(Animator animator) {              }

              @Override
              public void onAnimationRepeat(Animator animator) {              }
          }
        );
    }

    @Override
    public void onBackPressed() {
        //disabling back button
        return;
    }
}