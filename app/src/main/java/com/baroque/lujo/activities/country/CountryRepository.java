package com.baroque.lujo.activities.country;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import network.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utilities.Resource;

public class CountryRepository {
    private final String TAG = getClass().getSimpleName();

    private MutableLiveData<Boolean> isBusy;
    public MutableLiveData<Boolean> getIsBusy() {
        if (isBusy == null) {
            isBusy = new MutableLiveData<>();
            isBusy.setValue(false);
        }
        return isBusy;
    }


    public MutableLiveData<Resource<List<CountryModel>>> requestCountries() {
        final MutableLiveData<Resource<List<CountryModel>>> mutableLiveData = new MutableLiveData<>();
        getIsBusy().setValue(true);
        ApiInterface.getInstance().getListOfCountries("all").enqueue(new Callback<CountryResponse>() {

            @Override
            public void onResponse(Call<CountryResponse> call, Response<CountryResponse> response) {
                getIsBusy().setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "requestCountries.onResponse" );
                    mutableLiveData.setValue(Resource.success(response.body().getContent()));
                }else{
                    mutableLiveData.setValue(Resource.error("Error" , null));
                }
            }

            @Override
            public void onFailure(Call<CountryResponse> call, Throwable t) {
                getIsBusy().setValue(false);
                Log.d(TAG, "requestCountries.onFailure" + call.toString());
                mutableLiveData.setValue(Resource.error(t.getMessage() , null));
            }

        });
        return mutableLiveData;
    }

}
