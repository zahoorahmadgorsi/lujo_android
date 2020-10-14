package com.baroque.lujo.activities.my_account;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.baroque.lujo.activities.SplashActivity;
import com.baroque.lujo.activities.login.LoginActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.view.View;
import android.widget.ImageView;

import com.baroque.lujo.R;
import com.baroque.lujo.activities.login.LoginViewModel;
import com.baroque.lujo.databinding.ActivityMyAccountBinding;
import com.bumptech.glide.Glide;

import utilities.Constants;

import static utilities.Constants.ACTION_TO_PERFORM;
import static utilities.Constants.PREF_FILE_NAME;
import static utilities.Utility.clearSharedPreference;

public class MyAccountActivity extends AppCompatActivity implements View.OnClickListener{
    private ActivityMyAccountBinding binding;
    MyAccountViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_account);
        binding.setLifecycleOwner(this);
        // add back arrow to toolbar
//        if (getSupportActionBar() != null){
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Enabling the back button
//        }
        findViewById(R.id.btnSignOut).setOnClickListener(this);
        findViewById(R.id.btnEditProfile).setOnClickListener(this);
        findViewById(R.id.btnInvite).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel = new MyAccountViewModel(getApplication());
        binding.setViewModel(viewModel);           //use this incase of live data
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSignOut:
                AlertDialog alertDialog = new AlertDialog.Builder(MyAccountActivity.this, R.style.alertDialog).create();
                alertDialog.setTitle("Log Out");
                alertDialog.setMessage("Are you sure, you want to logout?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                clearSharedPreference(MyAccountActivity.this,PREF_FILE_NAME);
                                Intent intent = new Intent(MyAccountActivity.this , SplashActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        } );
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss(); //<-- change it with ur code
                            }
                        } );
                alertDialog.show();
                break;
            case R.id.btnEditProfile:
                Intent intent = new Intent(MyAccountActivity.this, LoginActivity.class);
                intent.putExtra(ACTION_TO_PERFORM, Constants.PAGE_TYPE.EDIT_PROFILE);
                startActivity(intent);
                break;
            case R.id.btnInvite:
                String message = "I would like to invite you to join the unprecedented world of Lujo. Download Lujo for iPhone https://apps.apple.com/us/app/lujo/id1233843327 and become a member to enjoy the finest that the world has to offer. A special price for a yearly membership awaits you with a unique code " + viewModel.getUser().getReferral_code();
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, message);
                startActivity(Intent.createChooser(share, getResources().getString(R.string.invite_a_friend)));
                break;
        }
    }

    @BindingAdapter("app:imageUri")
    public static void loadImageWithUri(ImageView imageView, String imageUri){
        Glide.with(imageView.getContext())
                .load(imageUri)
                .placeholder(R.drawable.ic_placeholder)
                .into(imageView);
    }

//can be call this metethod instead of onSupportNavigateUp
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // handle arrow click here
//        if (item.getItemId() == android.R.id.home) {
//            finish(); // close this activity and return to preview activity (if there is any)
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}