package com.baroque.lujo.activities.otp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.baroque.lujo.activities.login.UserModel;

import model.api_response.GenericResponse;
import utilities.Action;
import utilities.Constants;
import utilities.Resource;

public class OTPViewModel extends ViewModel {
    private final String TAG = getClass().getSimpleName();

    public MutableLiveData<String> otp1 = new MutableLiveData<>();
    public MutableLiveData<String> otp2 = new MutableLiveData<>();
    public MutableLiveData<String> otp3 = new MutableLiveData<>();
    public MutableLiveData<String> otp4 = new MutableLiveData<>();
    public MutableLiveData<String> userPhoneNumber = new MutableLiveData<>();
    public MutableLiveData<String> userPhonePrefix = new MutableLiveData<>();

    //Current Logging in User
    private UserModel user;
    public UserModel getUser() { return user; }
    public void setUser(UserModel user)
    {
        this.user = user;
        this.userPhoneNumber.setValue(user.getPhoneNumber());
        this.userPhonePrefix.setValue(user.getPhonePrefix());
    }

    private OTPRepository otpRepository;
    public OTPViewModel(){
        otpRepository = new OTPRepository();
    }

    //To read the response of OTP verification at the time of SignUp
    private MutableLiveData<Resource<GenericResponse>> mOTPVerificationResponse;
    public LiveData<Resource<GenericResponse>> getOTPVerificationResponse() {
        if(mOTPVerificationResponse == null){
            mOTPVerificationResponse = otpRepository.getOTPVerificationResponse();
        }
        return mOTPVerificationResponse;
    }

    private Constants.PAGE_TYPE pageType;
    public Constants.PAGE_TYPE getPageType() {
        return pageType;
    }
    public void setPageType(Constants.PAGE_TYPE pageType) {
        this.pageType = pageType;
    }

    //To read the response of OTP verification at the time of SignIn
//    private MutableLiveData<Resource<GenericResponse>> mLoginOTPVerificationResponse;
//    public LiveData<Resource<GenericResponse>> getLoginOTPVerificationResponse() {
//        if(mLoginOTPVerificationResponse == null){
//            mLoginOTPVerificationResponse = otpRepository.getLoginOTPVerificationResponse();
//        }
//        return mLoginOTPVerificationResponse;
//    }

    public void onOTPSubmitClicked() {
        switch(pageType) {
            case SIGN_IN:
                otpRepository.verifyLoginOTP(userPhonePrefix.getValue(), userPhoneNumber.getValue(), otp1.getValue() + otp2.getValue() + otp3.getValue() + otp4.getValue());
                break;
            case SIGN_UP:
            case CHANGE_PHONE_NUMBER:
                otpRepository.verifyOTP(userPhonePrefix.getValue(), userPhoneNumber.getValue(), otp1.getValue() + otp2.getValue() + otp3.getValue() + otp4.getValue());
                break;
        }
    }

    private MutableLiveData<Resource<GenericResponse>> mResendOTPResponse;
    public LiveData<Resource<GenericResponse>> getResendOTPResponse() {
        if(mResendOTPResponse == null){
            mResendOTPResponse = otpRepository.getResendOTPResponse();
        }
        return mResendOTPResponse;
    }
    public void onResendOTPClicked(){
        otpRepository.resendOTP(userPhonePrefix.getValue(),userPhoneNumber.getValue());
    }

    //Stores actions for view.
    private MutableLiveData<Action> mAction = new MutableLiveData<>();
    public LiveData<Action> getAction() {
        return mAction;
    }
    public void onChangePhoneNumberClicked(){
        mAction.setValue(new Action(Constants.PAGE_TYPE.CHANGE_PHONE_NUMBER));
    }

    private MutableLiveData<Boolean> isBusy;
    public MutableLiveData<Boolean> getIsBusy() {
        if (isBusy == null) {
            isBusy = otpRepository.getIsBusy();
        }
        return isBusy;
    }

    //To read the user profile details
    private MutableLiveData<Resource<UserModel>> mUserProfileResponse;
    public LiveData<Resource<UserModel>> getUserProfileResponse(String token) {
        if(mUserProfileResponse == null){
            mUserProfileResponse = otpRepository.getUserProfile(token);
        }
        return mUserProfileResponse;
    }
}
