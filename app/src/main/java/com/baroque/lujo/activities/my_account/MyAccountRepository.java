package com.baroque.lujo.activities.my_account;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.baroque.lujo.activities.country.CountryModel;
import com.baroque.lujo.activities.country.CountryResponse;
import com.google.gson.Gson;

import java.util.List;

import model.api_response.GenericResponse;
import network.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utilities.Resource;

public class MyAccountRepository {
    private final String TAG = getClass().getSimpleName();

    private MutableLiveData<Boolean> isBusy;
    public MutableLiveData<Boolean> getIsBusy() {
        if (isBusy == null) {
            isBusy = new MutableLiveData<>();
            isBusy.setValue(false);
        }
        return isBusy;
    }

    public MutableLiveData<Resource<GenericResponse>> uploadAvatar(String base64 , String token) {
        final MutableLiveData<Resource<GenericResponse>> mutableLiveData = new MutableLiveData<>();
        getIsBusy().setValue(true);
        ApiInterface.getInstance().upLoadAvatar(base64,token).enqueue(new Callback<GenericResponse>() {

            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                getIsBusy().setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "uploadAvatar.onResponse" );
                    mutableLiveData.setValue(Resource.success(response.body()));
                }else{
                    Gson gson = new Gson();
                    GenericResponse failResponse = gson.fromJson(response.errorBody().charStream(), GenericResponse.class);
                    mutableLiveData.setValue(Resource.error(failResponse.getContent().toString() , failResponse));
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                getIsBusy().setValue(false);
                Log.d(TAG, "uploadAvatar.onFailure" + call.toString());
                mutableLiveData.setValue(Resource.error(t.getMessage() , null));
            }

        });
        return mutableLiveData;
    }
}
