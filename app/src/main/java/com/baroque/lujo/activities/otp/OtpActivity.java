package com.baroque.lujo.activities.otp;

import android.content.Intent;
import android.os.Bundle;

import com.baroque.lujo.activities.login.LoginActivity;
import com.baroque.lujo.activities.WelcomeActivity;
import com.baroque.lujo.databinding.ActivityOtpBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.baroque.lujo.R;
import com.google.gson.Gson;

import com.baroque.lujo.activities.login.UserModel;

import model.api_response.TokenAndExpiry;
import model.api_response.GenericResponse;
import utilities.Action;
import utilities.Constants;
import utilities.Resource;
import utilities.Utility;

import static utilities.Constants.ACTION_TO_PERFORM;
import static utilities.Constants.INTENT_FIRST_PARAM;
import static utilities.Constants.PREF_FILE_NAME;
import static utilities.Key.KEY_CURRENT_USER;
import static utilities.Key.KEY_TOKEN_EXPIRY;
import static utilities.Resource.Status.ERROR;
import static utilities.Resource.Status.SUCCESS;
import static utilities.Utility.saveObjectToSharedPreference;

public class OtpActivity extends AppCompatActivity implements View.OnClickListener{
    private ActivityOtpBinding binding;
    private UserModel user;
    Constants.PAGE_TYPE pageType; //signIn or signUp

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otp);
        binding.setLifecycleOwner(this);
        OTPViewModel viewModel = new ViewModelProvider(this).get(OTPViewModel.class);
        binding.setViewModel(viewModel);           //use this incase of live data
        binding.executePendingBindings();
        Intent intent = getIntent();
        this.pageType = (Constants.PAGE_TYPE) intent.getSerializableExtra(Constants.ACTION_TO_PERFORM); //sign up is default
        viewModel.setPageType( pageType );
        //Reading phone number to which OTP was sent
        user = (UserModel)intent.getSerializableExtra(INTENT_FIRST_PARAM);
        binding.getViewModel().setUser(user);

        //Nested API calls
        //Observing response of OTP Verification in case of signIn/Signup
        viewModel.getOTPVerificationResponse().observe(this, new Observer<Resource<GenericResponse>>() {
            @Override
            public void onChanged(Resource<GenericResponse> signInOtpResponse) {
                if (signInOtpResponse.status == SUCCESS){
                    Gson gson = new Gson();
                    //Converting Content object to string and then parsing JSON to read expiration and token and storing to sharedpreference
                    String json = gson.toJson(signInOtpResponse.data.getContent());
                    TokenAndExpiry tokenAndExpiry = gson.fromJson(json, TokenAndExpiry.class);
                    saveObjectToSharedPreference(OtpActivity.this, PREF_FILE_NAME, KEY_TOKEN_EXPIRY, tokenAndExpiry);

                    // Observing response of user profile
                    viewModel.getUserProfileResponse(tokenAndExpiry.getToken()).observe(OtpActivity.this, new Observer<Resource<UserModel>>() {
                        @Override
                        public void onChanged(Resource<UserModel> userProfileResponse) {
                            if(userProfileResponse.status == SUCCESS){
                                user = userProfileResponse.data; //updating user object
                            }else if (userProfileResponse.status == ERROR){
                                String errorMessage = getResources().getString(R.string.general_error);
                                Utility.showalert(OtpActivity.this,"Error",errorMessage,"Dismiss");
                            }
                            //Saving Signing in user OR User Profile into shared preference
                            saveObjectToSharedPreference(OtpActivity.this, PREF_FILE_NAME, KEY_CURRENT_USER, user);
                            if(pageType == Constants.PAGE_TYPE.SIGN_IN || pageType == Constants.PAGE_TYPE.SIGN_UP) { //to Welcome screen
                                Intent intent = new Intent(OtpActivity.this, WelcomeActivity.class);
                                intent.putExtra(ACTION_TO_PERFORM, pageType);   //page type will contain signup incase of signup and signin in case of signin
                                intent.putExtra(INTENT_FIRST_PARAM, user);
                                startActivity(intent);
                            }else {
                                finish(); // finishing this activity will take to previous screen which was MyAccount
                            }
                        }
                    });

                }else if (signInOtpResponse.status == ERROR){
                    String errorMessage = getResources().getString(R.string.general_error);
                    if (signInOtpResponse.data != null){
                        errorMessage = signInOtpResponse.data.getContent().toString();
                    }else if ( !TextUtils.isEmpty(signInOtpResponse.message )){
                        errorMessage = signInOtpResponse.message;
                    }
                    Utility.showalert(OtpActivity.this,"Error",errorMessage,"Dismiss");
                }
            }
        });

        //Observing response of Resend OTP
        viewModel.getResendOTPResponse().observe(this, new Observer<Resource<GenericResponse>>() {
            @Override
            public void onChanged(Resource<GenericResponse> userSignUpResponse) {
                if (userSignUpResponse.status == SUCCESS){
                    //Show a toast message that OTP has been Resent
                    Toast.makeText(OtpActivity.this, userSignUpResponse.data.getContent().toString(), Toast.LENGTH_SHORT).show();
                }else if (userSignUpResponse.status == ERROR){
                    String errorMessage = getResources().getString(R.string.general_error);
                    if (userSignUpResponse.data != null){
                        errorMessage = userSignUpResponse.data.getContent().toString();
                    }else if ( !TextUtils.isEmpty(userSignUpResponse.message )){
                        errorMessage = userSignUpResponse.message;
                    }
                    Utility.showalert(OtpActivity.this,"Error",errorMessage,"Dismiss");
                }
            }
        });

        //Observing CHANGE password action
        viewModel.getAction().observe(this, new Observer<Action>() {
            @Override
            public void onChanged(Action action) {
                if(action != null){
                    switch (action.getValue()){
                        case CHANGE_PHONE_NUMBER:
                            Intent intent = new Intent(OtpActivity.this, LoginActivity.class);
                            intent.putExtra(ACTION_TO_PERFORM, Constants.PAGE_TYPE.CHANGE_PHONE_NUMBER);
                            intent.putExtra(INTENT_FIRST_PARAM, user.getUserWithSwapPhoneNumbers());
//                            intent.putExtra(INTENT_SECOND_PARAM, binding.getViewModel().userPhoneNumber.getValue());
                            //If the Activity being started is already running in the current task then instead of launching the new instance of that Activity, all the other
                            // activities on top of it is destroyed (with call to onDestroy method) and this intent is delivered to the resumed instance of the Activity
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
                            startActivity(intent);
                            break;
                    }
                }
            }
        });


        viewModel.getIsBusy().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    binding.pbProgress.setVisibility(View.VISIBLE);
                }else{
                    binding.pbProgress.setVisibility(View.GONE);
                }
            }
        }) ;

        binding.txtOTP1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() == 1)
                    binding.txtOTP2.requestFocus();
                //If user has entered all 4 digits of OTP then enable submit button else dont
                if(binding.txtOTP1.getText().length() + binding.txtOTP2.getText().length() + binding.txtOTP3.getText().length() + binding.txtOTP4.getText().length() == 4){
                    binding.btnSubmit.setEnabled(true);
                }else{
                    binding.btnSubmit.setEnabled(false);
                }
            }
        });

        binding.txtOTP2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() == 1)
                    binding.txtOTP3.requestFocus();
                else if(editable.length() == 0)
                    binding.txtOTP1.requestFocus();
                //If user has entered all 4 digits of OTP then enable submit button else dont
                if(binding.txtOTP1.getText().length() + binding.txtOTP2.getText().length() + binding.txtOTP3.getText().length() + binding.txtOTP4.getText().length() == 4){
                    binding.btnSubmit.setEnabled(true);
                }else{
                    binding.btnSubmit.setEnabled(false);
                }
            }
        });

        binding.txtOTP3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length()==1)
                    binding.txtOTP4.requestFocus();
                else if(editable.length() == 0)
                    binding.txtOTP2.requestFocus();
                //If user has entered all 4 digits of OTP then enable submit button else dont
                if(binding.txtOTP1.getText().length() + binding.txtOTP2.getText().length() + binding.txtOTP3.getText().length() + binding.txtOTP4.getText().length() == 4){
                    binding.btnSubmit.setEnabled(true);
                }else{
                    binding.btnSubmit.setEnabled(false);
                }
            }
        });

        binding.txtOTP4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("OTP4" , String.valueOf(binding.txtOTP4.getText().length()));
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() == 0)
                    binding.txtOTP3.requestFocus();
                //If user has entered all 4 digits of OTP then enable submit button else dont
                if(binding.txtOTP1.getText().length() + binding.txtOTP2.getText().length() + binding.txtOTP3.getText().length() + binding.txtOTP4.getText().length() == 4){
                    binding.btnSubmit.setEnabled(true);
                }else{
                    binding.btnSubmit.setEnabled(false);
                }
            }
        });

        binding.llSupport.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case  R.id.llSupport:
                Intent intent = new Intent(Intent.ACTION_SEND);
                String[] recipients = {getResources().getString(R.string.support_email)};
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent.putExtra(Intent.EXTRA_SUBJECT,"Support");
                intent.setType("text/html");
                startActivity(Intent.createChooser(intent, "Send mail"));
                break;
        }
    }
}