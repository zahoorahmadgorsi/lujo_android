package com.baroque.lujo.activities.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.baroque.lujo.R;
import com.baroque.lujo.activities.country.CountriesActivity;
import com.baroque.lujo.activities.otp.OtpActivity;
import com.baroque.lujo.databinding.ActivityLoginBinding;

import com.baroque.lujo.activities.country.CountryModel;
import model.api_response.GenericResponse;
import utilities.Constants;
import utilities.Key;
import utilities.Resource;
import utilities.Utility;

import static utilities.Constants.ACTION_TO_PERFORM;
import static utilities.Constants.INTENT_FIRST_PARAM;
import static utilities.Constants.PREF_FILE_NAME;
import static utilities.Key.KEY_CURRENT_USER;
import static utilities.Resource.Status.ERROR;
import static utilities.Resource.Status.SUCCESS;
import static utilities.Utility.getSavedObjectFromPreference;
import static utilities.Utility.saveObjectToSharedPreference;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Constants.PAGE_TYPE pageType; //signIn or signUp, UPDATE PROFILE
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setLifecycleOwner(this);
        Intent intent = getIntent();
        this.pageType = (Constants.PAGE_TYPE) intent.getSerializableExtra(Constants.ACTION_TO_PERFORM); //sign up is default
        LoginViewModel viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        viewModel.setPageType( pageType );
        binding.setViewModel(viewModel);           //use this incase of live data
        binding.executePendingBindings();
        //Observing text fields validation response of user signup/UPDATE PROFILE
        viewModel.getIsUserDataValid().observe(this, new Observer<UserModel>() {
            @Override
            public void onChanged(UserModel user) {
                binding.cbTermsAndConditions.setError(null);    //resetting checkbox to default
                if (!TextUtils.isEmpty(user.getErrorMessage())){   //there is some error
                    switch( user.getErrorType()){
                        case FIRST_NAME:
                            binding.txtFirstName.requestFocus();
                            binding.txtFirstName.setError(user.getErrorMessage());
                            break;
                        case LAST_NAME:
                            binding.txtLastName.requestFocus();
                            binding.txtLastName.setError(user.getErrorMessage());
                            break;
                        case EMAIL:
                            binding.txtEmail.requestFocus();
                            binding.txtEmail.setError(user.getErrorMessage());
                            break;
                        case PHONE_NUMBER:
                            binding.txtPhoneNumber.requestFocus();
                            binding.txtPhoneNumber.setError(user.getErrorMessage());
                            break;
                        case TERMS_AND_CONDITIONS:
//                            binding.cbTermsAndConditions.requestFocus();
                            binding.cbTermsAndConditions.setError(user.getErrorMessage());
                            Toast.makeText(LoginActivity.this, user.getErrorMessage(), Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            }
        });

        //Observing response of user signup
        viewModel.getSignUpInResponse().observe(this, new Observer<Resource<GenericResponse>>() {
            @Override
            public void onChanged(Resource<GenericResponse> userSignUpResponse) {
                if (userSignUpResponse.status == SUCCESS){
                    UserModel user = viewModel.getUser();
                    if (pageType == Constants.PAGE_TYPE.SIGN_UP || pageType == Constants.PAGE_TYPE.SIGN_IN) {
                        //Navigate to OTP verification screen;
                        Intent intent = new Intent(LoginActivity.this, OtpActivity.class);
                        intent.putExtra(ACTION_TO_PERFORM, pageType);   //page type will contain signup incase of signup and signin in case of signin
                        intent.putExtra(INTENT_FIRST_PARAM, user);
                        startActivity(intent);
                    }else if (pageType == Constants.PAGE_TYPE.EDIT_PROFILE){
                        //rePlacing only editable fields
                        user.setFirstName(binding.txtFirstName.getText().toString());
                        user.setLastName(binding.txtLastName.getText().toString());
                        user.setEmail(binding.txtEmail.getText().toString());
                        user.setAlpha2Code(binding.tvCountryName.getText().toString());
                        user.setPhonePrefix(binding.tvPhonePrefix.getText().toString());
                        user.setPhoneNumber(binding.txtPhoneNumber.getText().toString());
                        saveObjectToSharedPreference(LoginActivity.this, PREF_FILE_NAME, KEY_CURRENT_USER, user);
                        //Show a toast message that OTP has been Resent
                        Toast.makeText(LoginActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                        finish();//it will just move to previous activity which was my account
                    }
                }else if (userSignUpResponse.status == ERROR){
                    String errorMessage = getResources().getString(R.string.general_error);
                    if (userSignUpResponse.data != null){
                        errorMessage = userSignUpResponse.data.getContent().toString();
                    }else if ( !TextUtils.isEmpty(userSignUpResponse.message )){
                        errorMessage = userSignUpResponse.message;
                    }
                    Utility.showalert(LoginActivity.this,"Error",errorMessage,"Dismiss");
                }
            }
        });

        //Observing response i.e. OTP in case of login
//        viewModel.getLoginResponse().observe(this, new Observer<Resource<GenericResponse>>() {
//            @Override
//            public void onChanged(Resource<GenericResponse> signInResponse) {
//                if (signInResponse.status == SUCCESS){
//                    //Navigate to OTP verification screen;
//                    Intent intent = new Intent(LoginActivity.this, OtpActivity.class);
//                    UserModel user = viewModel.getUser();
//                    intent.putExtra(ACTION_TO_PERFORM, pageType);   //page type will contain signup incase of signup and signin in case of signin
//                    intent.putExtra(INTENT_FIRST_PARAM, user);
//                    startActivity(intent);
//                }else if (signInResponse.status == ERROR){
//                    String errorMessage = getResources().getString(R.string.general_error);
//                    if (signInResponse.data != null){
//                        errorMessage = signInResponse.data.getContent().toString();
//                    }else if ( !TextUtils.isEmpty(signInResponse.message )){
//                        errorMessage = signInResponse.message;
//                    }
//                    Utility.showalert(LoginActivity.this,"Error",errorMessage,"Dismiss");
//                }
//            }
//        });

        //Observing response of change password
        viewModel.getChangePasswordResponse().observe(this, new Observer<Resource<GenericResponse>>() {
            @Override
            public void onChanged(Resource<GenericResponse> userSignUpResponse) {
                if (userSignUpResponse.status == SUCCESS){
                    Toast.makeText(LoginActivity.this, userSignUpResponse.data.getContent().toString(), Toast.LENGTH_SHORT).show();
                    //Navigate to OTP verification screen;
                    Intent intent = new Intent(LoginActivity.this, OtpActivity.class);
                    UserModel user;
                    user = viewModel.getUser();   //swapping it again
                    intent.putExtra(ACTION_TO_PERFORM, pageType);   //page type will contain signup incase of signup and signin in case of signin
                    intent.putExtra(INTENT_FIRST_PARAM, user);
                    startActivity(intent);
                }else if (userSignUpResponse.status == ERROR){
                    String errorMessage = getResources().getString(R.string.general_error);
                    if (userSignUpResponse.data != null){
                        errorMessage = userSignUpResponse.data.getContent().toString();
                    }else if ( !TextUtils.isEmpty(userSignUpResponse.message )){
                        errorMessage = userSignUpResponse.message;
                    }
                    Utility.showalert(LoginActivity.this,"Error",errorMessage,"Dismiss");
                }
            }
        });

        //Obsering the busy boolean for progress bar
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

        initializeControls();

        if (this.pageType == Constants.PAGE_TYPE.SIGN_UP || this.pageType == Constants.PAGE_TYPE.EDIT_PROFILE){
            if (this.pageType == Constants.PAGE_TYPE.SIGN_UP) {
                this.setTitle("Create new account");
            }else if (this.pageType == Constants.PAGE_TYPE.EDIT_PROFILE){
                this.setTitle("Edit Profile");
                binding.btnSignIn.setText("U P D A T E");
                binding.llTermsAndConditions.setVisibility(View.GONE);
                UserModel user = getSavedObjectFromPreference(LoginActivity.this,PREF_FILE_NAME, KEY_CURRENT_USER, UserModel.class);
                if (user != null)
                    viewModel.setUser(user);
            }
            binding.llSignIn.setVisibility(View.GONE);
            binding.btnSignIn.setEnabled(true);   //it will be coloured as grey
        }else if (this.pageType == Constants.PAGE_TYPE.SIGN_IN || this.pageType == Constants.PAGE_TYPE.CHANGE_PHONE_NUMBER){
            if (this.pageType == Constants.PAGE_TYPE.SIGN_IN) {
                this.setTitle("Member Login");
                binding.tvPhoneNumberTitle.setText("Dear LUJO Member, please enter your phone number so we can send you a verification code.");
                binding.btnSignIn.setText("S E N D   V E R I F I C A T I O N   C O D E");
                binding.llOldPhoneNumber.setVisibility(View.GONE);
            }else if (this.pageType == Constants.PAGE_TYPE.CHANGE_PHONE_NUMBER) {
                this.setTitle("Change Phone Number");
                binding.tvPhoneNumberTitle.setText("Please change the phone number.");
                binding.btnSignIn.setText("C O N F I R M   &   R E S E N D   C O D E");
                binding.llOldPhoneNumber.setVisibility(View.VISIBLE);
                //Reading old phone number
                UserModel user = (UserModel)intent.getSerializableExtra(INTENT_FIRST_PARAM);
                binding.getViewModel().setUser(user);
            }
            binding.llSignUp.setVisibility(View.GONE);
            binding.llTermsAndConditions.setVisibility(View.GONE);
            binding.btnSignIn.setEnabled(false);   //it will be coloured as grey
        }
    }

    //this method initializes all the controls, events which are defined on XML file
    private void initializeControls(){
        binding.tvTermsAndConditions.setOnClickListener(this);
        binding.llSupport.setOnClickListener(this);
        binding.llCountry.setOnClickListener(this);
        binding.txtPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                System.out.print(charSequence);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                System.out.print(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                System.out.print(editable.toString());
                if(editable.toString().length() >= 6 ){
                    binding.btnSignIn.setEnabled(true);
                }else{
                    binding.btnSignIn.setEnabled(false);
                }
            }
        });
    }


    @Override
    public void onClick(View view)
    {
        switch (view.getId()) {
            case R.id.tvTermsAndConditions:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.URL_TERMS_AND_CONDITIONS));
                startActivity(browserIntent);
                break;
            case  R.id.llSupport:
                System.out.print("SignUp");
                Intent intent = new Intent(Intent.ACTION_SEND);
                String[] recipients = {getResources().getString(R.string.support_email)};
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent.putExtra(Intent.EXTRA_SUBJECT,"Support");
                intent.setType("text/html");
                startActivity(Intent.createChooser(intent, "Send mail"));
                break;
            case R.id.llCountry:
                intent = new Intent(LoginActivity.this, CountriesActivity.class);
                startActivityForResult(intent,1);
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                CountryModel selectedCountry = (CountryModel) data.getSerializableExtra(Key.KEY_SELECTED_COUNTRY);
                binding.getViewModel().setSelectedCountry(selectedCountry);
            }
        }
    }

}