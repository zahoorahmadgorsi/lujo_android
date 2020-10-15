package com.baroque.lujo.activities.my_account;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import com.baroque.lujo.activities.SplashActivity;
import com.baroque.lujo.activities.WelcomeActivity;
import com.baroque.lujo.activities.country.CountriesActivity;
import com.baroque.lujo.activities.country.CountryModel;
import com.baroque.lujo.activities.login.LoginActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.baroque.lujo.R;
import com.baroque.lujo.activities.login.LoginViewModel;
import com.baroque.lujo.activities.login.UserModel;
import com.baroque.lujo.activities.otp.OtpActivity;
import com.baroque.lujo.databinding.ActivityMyAccountBinding;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import model.api_response.GenericResponse;
import utilities.Constants;
import utilities.Resource;
import utilities.Utility;

import static utilities.Constants.ACTION_TO_PERFORM;
import static utilities.Constants.INTENT_FIRST_PARAM;
import static utilities.Constants.PREF_FILE_NAME;
import static utilities.Key.KEY_CURRENT_USER;
import static utilities.Resource.Status.ERROR;
import static utilities.Resource.Status.SUCCESS;
import static utilities.Utility.bitmapTobase64;
import static utilities.Utility.clearSharedPreference;
import static utilities.Utility.getSavedObjectFromPreference;
import static utilities.Utility.saveObjectToSharedPreference;

public class MyAccountActivity extends AppCompatActivity implements View.OnClickListener{
    private ActivityMyAccountBinding binding;
    MyAccountViewModel viewModel;
    int EDIT_PROFILE = 0, TAKE_A_PHOTO = 1 , SELECT_A_PHOTO = 2;    // activity results
    UserModel user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_account);
        binding.setLifecycleOwner(this);
        viewModel = new ViewModelProvider(this).get(MyAccountViewModel.class);
        binding.setViewModel(viewModel);           //use this incase of live data
        //Setting on click listeners
        binding.btnChangeProfilePhoto.setOnClickListener(this);
        binding.btnEditProfile.setOnClickListener(this);
        binding.btnInvite.setOnClickListener(this);
        binding.btnSignOut.setOnClickListener(this);

        // Observing response of user profile
        viewModel.getIsBusy().setValue(true);
        viewModel.getUserProfile().observe(MyAccountActivity.this, new Observer<Resource<UserModel>>() {
            @Override
            public void onChanged(Resource<UserModel> userProfileResponse) {
                viewModel.getIsBusy().setValue(false);
                if(userProfileResponse.status == SUCCESS){
                    UserModel user = userProfileResponse.data; //updating user object
                    viewModel.setUser(user);    //reassigning the user with updated URI, UI will be updated
                    //Saving Signing in user OR User Profile into shared preference
                    saveObjectToSharedPreference(MyAccountActivity.this, PREF_FILE_NAME, KEY_CURRENT_USER, user);
                }else if (userProfileResponse.status == ERROR){
                    String errorMessage = getResources().getString(R.string.general_error);
                    Utility.showalert(MyAccountActivity.this,"Error",errorMessage,"Dismiss");
                }
            }
        });

        binding.getViewModel().getIsBusy().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    binding.pbProgress.setVisibility(View.VISIBLE);
                }else{
                    binding.pbProgress.setVisibility(View.GONE);
                }
            }
        }) ;

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;    //
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
                startActivityForResult(intent,EDIT_PROFILE);
                break;
            case R.id.btnInvite:
                String message = "I would like to invite you to join the unprecedented world of Lujo. Download Lujo for iPhone https://apps.apple.com/us/app/lujo/id1233843327 and become a member to enjoy the finest that the world has to offer. A special price for a yearly membership awaits you with a unique code " + viewModel.getUser().getValue().getReferral_code();
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, message);
                startActivity(Intent.createChooser(share, getResources().getString(R.string.invite_a_friend)));
                break;
            case R.id.btnChangeProfilePhoto:
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , SELECT_A_PHOTO);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0: //edit profile
                    if (resultCode == RESULT_OK ) {  //from camera
                        UserModel user = getSavedObjectFromPreference(getApplication(),PREF_FILE_NAME,KEY_CURRENT_USER,UserModel.class);
                        //Reloading saved user object which was saved in edit profile
                        viewModel.setUser(user);
                    }
                    break;
                case 2: //select a photo
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        //converting bitmap to base64
                        InputStream imageStream = null;
                        try {
                            imageStream = this.getContentResolver().openInputStream(selectedImage);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                        String base64 = bitmapTobase64(yourSelectedImage);
                        base64 = "data:image/jpeg;base64," + base64;    //prefixing for server
                        //uploading the avatar
                        binding.getViewModel().uploadAvatar(base64).observe(this, new Observer<Resource<GenericResponse>>() {
                            @Override
                            public void onChanged(Resource<GenericResponse> genericResponse) {
                                if (genericResponse.status == SUCCESS){
                                    //Updating user in viewmodel, will automatically display on imageview, as its binded
                                    UserModel user = viewModel.getUser().getValue();
                                    //one way to update the avatar
                                    //user.setAvatar(selectedImage.toString());
                                    //Other way to update the avatar
                                    String strURIFromServer = genericResponse.data.getContent().toString();// it contains the URI
                                    user.setAvatar(strURIFromServer);
                                    viewModel.setUser(user);    //reassigning the user with updated URI, UI will be updated
                                    //Updating the cached value of user
                                    saveObjectToSharedPreference(MyAccountActivity.this, PREF_FILE_NAME, KEY_CURRENT_USER, user);
                                }else if (genericResponse.status == ERROR){
                                    Utility.showalert(MyAccountActivity.this,"Error",genericResponse.message,"Dismiss");
                                }
                            }
                        });

                    }
                    break;
            }
        }
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