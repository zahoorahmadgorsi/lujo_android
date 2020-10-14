package com.baroque.lujo.activities.otp;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.baroque.lujo.activities.login.UserModel;
import com.google.gson.Gson;

import model.api_response.GenericResponse;
import model.api_response.TokenAndExpiry;
import network.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utilities.Resource;

public class OTPRepository {
    private final String TAG = getClass().getSimpleName();

    private MutableLiveData<Boolean> isBusy;
    public MutableLiveData<Boolean> getIsBusy() {
        if (isBusy == null) {
            isBusy = new MutableLiveData<>();
            isBusy.setValue(false);
        }
        return isBusy;
    }

    private MutableLiveData<Resource<GenericResponse>> mOTPVerificationResponse;
    public MutableLiveData<Resource<GenericResponse>> getOTPVerificationResponse() {
        if (mOTPVerificationResponse == null) {
            mOTPVerificationResponse = new MutableLiveData<>();
        }
        return mOTPVerificationResponse;
    }
    public void verifyOTP(String phonePrefix, String phoneNumber , String OTP) {
        getIsBusy().setValue(true);
        ApiInterface.getInstance().verifyOTP(phonePrefix
                ,phoneNumber
                ,OTP).enqueue(new Callback<GenericResponse>() {

            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                getIsBusy().setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "verifyOTP.onResponse" );
                    mOTPVerificationResponse.setValue(Resource.success(response.body()));
                }else{
                    Gson gson = new Gson();
                    GenericResponse failResponse = gson.fromJson(response.errorBody().charStream(), GenericResponse.class);
                    mOTPVerificationResponse.setValue(Resource.error(failResponse.getContent().toString() , failResponse));
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                getIsBusy().setValue(false);
                Log.d(TAG, "verifyOTP.onFailure" + call.toString());
                mOTPVerificationResponse.setValue(Resource.error(t.getMessage() , null));
            }

        });
    }

    //To read the response of OTP verification at the time of SignIn
//    private MutableLiveData<Resource<GenericResponse>> mLoginOTPVerificationResponse;
//    public MutableLiveData<Resource<GenericResponse>> getLoginOTPVerificationResponse() {
//        if (mLoginOTPVerificationResponse == null) {
//            mLoginOTPVerificationResponse = new MutableLiveData<>();
//        }
//        return mLoginOTPVerificationResponse;
//    }
    public void verifyLoginOTP(String phonePrefix, String phoneNumber , String OTP) {
        getIsBusy().setValue(true);
        ApiInterface.getInstance().verifyLoginOTP(
                phonePrefix
                ,phoneNumber
                ,OTP).enqueue(new Callback<GenericResponse>() {

            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                getIsBusy().setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "verifyLoginOTP.onResponse" );
                    mOTPVerificationResponse.setValue(Resource.success(response.body()));
                }else{
                    Gson gson = new Gson();
                    GenericResponse failResponse = gson.fromJson(response.errorBody().charStream(), GenericResponse.class);
                    mOTPVerificationResponse.setValue(Resource.error(failResponse.getContent().toString() , failResponse));
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                getIsBusy().setValue(false);
                Log.d(TAG, "verifyLoginOTP.onFailure" + call.toString());
                mOTPVerificationResponse.setValue(Resource.error(t.getMessage() , null));
            }

        });
    }


    //To read the response of OTP verification at the time of SignUp
    private MutableLiveData<Resource<GenericResponse>> mResendOTPResponse;
    public MutableLiveData<Resource<GenericResponse>> getResendOTPResponse() {
        if (mResendOTPResponse == null) {
            mResendOTPResponse = new MutableLiveData<>();
        }
        return mResendOTPResponse;
    }
    public void resendOTP(String phonePrefix, String phoneNumber) {
        getIsBusy().setValue(true);
        ApiInterface.getInstance().resendOTP(phonePrefix
                ,phoneNumber
        ).enqueue(new Callback<GenericResponse>() {

            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                getIsBusy().setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "resendOTP.onResponse" );
                    mResendOTPResponse.setValue(Resource.success(response.body()));
                }else{
                    Gson gson = new Gson();
                    GenericResponse failResponse = gson.fromJson(response.errorBody().charStream(), GenericResponse.class);
                    mResendOTPResponse.setValue(Resource.error(failResponse.getContent().toString() , failResponse));
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                getIsBusy().setValue(false);
                Log.d(TAG, "resendOTP.onFailure" + call.toString());
                mResendOTPResponse.setValue(Resource.error(t.getMessage() , null));
            }

        });
    }

    //To read the user profile details
    public MutableLiveData<Resource<UserModel>> getUserProfile(String token) {
        final MutableLiveData<Resource<UserModel>> mutableLiveData = new MutableLiveData<>();
        getIsBusy().setValue(true);
        ApiInterface.getInstance().getUserProfile(token).enqueue(new Callback<GenericResponse>() {

            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                getIsBusy().setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "getUserProfile.onResponse" );
                    Gson gson = new Gson();
                    String json = gson.toJson(response.body().getContent());
                    UserModel userModel = gson.fromJson(json, UserModel.class);
                    mutableLiveData.setValue(Resource.success(userModel));
                }else{
                    mutableLiveData.setValue(Resource.error("Error" , null));
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                getIsBusy().setValue(false);
                Log.d(TAG, "getUserProfile.onFailure" + call.toString());
                mutableLiveData.setValue(Resource.error(t.getMessage() , null));
            }

        });
        return mutableLiveData;
    }

}
