package com.baroque.lujo.activities.login;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;

import model.api_response.GenericResponse;
import network.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utilities.Resource;

public class LoginRepository {
    private final String TAG = getClass().getSimpleName();

    private MutableLiveData<Boolean> isBusy;
    public MutableLiveData<Boolean> getIsBusy() {
        if (isBusy == null) {
            isBusy = new MutableLiveData<>();
            isBusy.setValue(false);
        }
        return isBusy;
    }

    private MutableLiveData<Resource<GenericResponse>> mSignUpInResponse;
    public MutableLiveData<Resource<GenericResponse>> getSignUpInResponse() {
        if (mSignUpInResponse == null) {
            mSignUpInResponse = new MutableLiveData<>();
        }
        return mSignUpInResponse;
    }

    public void signUp(UserModel user) {
        getIsBusy().setValue(true);
        ApiInterface.getInstance().signUp(user.getFirstName()
                ,user.getLastName()
                ,user.getEmail()
                ,user.getPhonePrefix()
                ,user.getPhoneNumber()).enqueue(new Callback<GenericResponse>() {

            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                getIsBusy().setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "signUp.onResponse" );
                    mSignUpInResponse.setValue(Resource.success(response.body()));
                }else{
                    Gson gson = new Gson();
                    GenericResponse failResponse = gson.fromJson(response.errorBody().charStream(), GenericResponse.class);
                    mSignUpInResponse.setValue(Resource.error(failResponse.getContent().toString() , failResponse));
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                getIsBusy().setValue(false);
                Log.d(TAG, "signUp.onFailure" + call.toString());
                mSignUpInResponse.setValue(Resource.error(t.getMessage() , null));
            }

        });
    }

//    private MutableLiveData<Resource<GenericResponse>> mLoginResponse;
//    public MutableLiveData<Resource<GenericResponse>> getLoginResponse() {
//        if (mLoginResponse == null) {
//            mLoginResponse = new MutableLiveData<>();
//        }
//        return mLoginResponse;
//    }
    public void login(String phonePrefix, String phoneNumber) {
        getIsBusy().setValue(true);
        ApiInterface.getInstance().login(phonePrefix
                ,phoneNumber
        ).enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                getIsBusy().setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "login.onResponse" );
                    mSignUpInResponse.setValue(Resource.success(response.body()));
                }else{
                    Gson gson = new Gson();
                    GenericResponse failResponse = gson.fromJson(response.errorBody().charStream(), GenericResponse.class);
                    mSignUpInResponse.setValue(Resource.error(failResponse.getContent().toString() , failResponse));
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                getIsBusy().setValue(false);
                Log.d(TAG, "login.onFailure" + call.toString());
                mSignUpInResponse.setValue(Resource.error(t.getMessage() , null));
            }

        });
    }

    private MutableLiveData<Resource<GenericResponse>> mChangePhoneNumberResponse;
    public MutableLiveData<Resource<GenericResponse>> getChangePhoneNumberResponse() {
        if (mChangePhoneNumberResponse == null) {
            mChangePhoneNumberResponse = new MutableLiveData<>();
        }
        return mChangePhoneNumberResponse;
    }

    public void changePhoneNumber(String oldPhonePrefix, String oldPhoneNumber,String phonePrefix, String phoneNumber) {
        getIsBusy().setValue(true);
        ApiInterface.getInstance().changePhoneNumber(
                oldPhonePrefix
                ,oldPhoneNumber
                ,phonePrefix
                ,phoneNumber
        ).enqueue(new Callback<GenericResponse>() {

            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                getIsBusy().setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "changePhoneNumber.onResponse" );
                    mChangePhoneNumberResponse.setValue(Resource.success(response.body()));
                }else{
                    Gson gson = new Gson();
                    GenericResponse failResponse = gson.fromJson(response.errorBody().charStream(), GenericResponse.class);
                    mChangePhoneNumberResponse.setValue(Resource.error(failResponse.getContent().toString() , failResponse));
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                getIsBusy().setValue(false);
                Log.d(TAG, "changePhoneNumber.onFailure" + call.toString());
                mChangePhoneNumberResponse.setValue(Resource.error(t.getMessage() , null));
            }

        });
    }

    public void editProfile(String firstName, String lastName, String email, String phonePrefix, String phoneNumber, String token) {

        getIsBusy().setValue(true);
        ApiInterface.getInstance().editProfile(
                firstName
                ,lastName
                ,email
                ,phonePrefix
                ,phoneNumber
                ,token
        ).enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                getIsBusy().setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "editProfile.onResponse" );
                    mSignUpInResponse.setValue(Resource.success(response.body()));
                }else{
                    Gson gson = new Gson();
                    GenericResponse failResponse = gson.fromJson(response.errorBody().charStream(), GenericResponse.class);
                    mSignUpInResponse.setValue(Resource.error(failResponse.getContent().toString() , failResponse));
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                getIsBusy().setValue(false);
                Log.d(TAG, "editProfile.onFailure" + call.toString());
                mSignUpInResponse.setValue(Resource.error(t.getMessage() , null));
            }

        });
    }
}
