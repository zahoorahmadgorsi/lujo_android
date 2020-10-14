package com.baroque.lujo.activities.country;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import utilities.Resource;

public class CountryViewModel extends ViewModel {
    private final String TAG = getClass().getSimpleName();

    private CountryRepository countryRepository;
    public CountryViewModel(){
        countryRepository = new CountryRepository();
    }

    private MutableLiveData<Resource<List<CountryModel>>> mMutableCountriesLiveData;
    public LiveData<Resource<List<CountryModel>>> getCountries() {
        if(mMutableCountriesLiveData == null){
            mMutableCountriesLiveData = countryRepository.requestCountries();
        }
        return mMutableCountriesLiveData;
    }

    private MutableLiveData<Boolean> isBusy;
    public MutableLiveData<Boolean> getIsBusy() {
        if (isBusy == null) {
            isBusy = countryRepository.getIsBusy();
        }
        return isBusy;
    }
}
