package com.baroque.lujo.activities.my_account;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.baroque.lujo.activities.country.CountryModel;
import com.baroque.lujo.activities.country.CountryRepository;
import com.baroque.lujo.activities.login.UserModel;
import com.baroque.lujo.activities.otp.OTPRepository;

import java.util.List;

import model.api_response.GenericResponse;
import model.api_response.TokenAndExpiry;
import utilities.Resource;

import static utilities.Constants.PREF_FILE_NAME;
import static utilities.Key.KEY_CURRENT_USER;
import static utilities.Key.KEY_TOKEN_EXPIRY;
import static utilities.Utility.getSavedObjectFromPreference;

//extending with AndroidViewModel to have application context to read token
public class MyAccountViewModel extends AndroidViewModel {

    private final String TAG = getClass().getSimpleName();
    private MyAccountRepository myAccountRepository;
    private OTPRepository otpRepository;    //to fetch the user profile
    TokenAndExpiry tokenAndExpiry;


    private MutableLiveData<UserModel> user = new MutableLiveData<>();
    public  MutableLiveData<UserModel> getUser() {
        return user;
    }
    public void setUser(UserModel user) {
        this.user.setValue(user);
    }

    public MyAccountViewModel(@NonNull Application application) {
        super(application);
        myAccountRepository = new MyAccountRepository();
        otpRepository = new OTPRepository();
        user.setValue(getSavedObjectFromPreference(getApplication(),PREF_FILE_NAME,KEY_CURRENT_USER,UserModel.class));
        tokenAndExpiry = getSavedObjectFromPreference(getApplication(),PREF_FILE_NAME,KEY_TOKEN_EXPIRY, TokenAndExpiry.class);
    }

    private MutableLiveData<Boolean> isBusy;
    public MutableLiveData<Boolean> getIsBusy() {
        if (isBusy == null) {
            isBusy = myAccountRepository.getIsBusy();
        }
        return isBusy;
    }

    private MutableLiveData<Resource<GenericResponse>> mGenericLiveData;
    public LiveData<Resource<GenericResponse>> uploadAvatar(String base64) {
        if(mGenericLiveData == null && tokenAndExpiry !=null){
                mGenericLiveData = myAccountRepository.uploadAvatar(base64 , tokenAndExpiry.getToken());
        }
        return mGenericLiveData;
    }

    //To read the user profile details
    private MutableLiveData<Resource<UserModel>> mUserProfileResponse;
    public LiveData<Resource<UserModel>> getUserProfile() {
        if(mUserProfileResponse == null && tokenAndExpiry !=null){
            mUserProfileResponse = otpRepository.getUserProfile(tokenAndExpiry.getToken());
        }
        return mUserProfileResponse;
    }
}
