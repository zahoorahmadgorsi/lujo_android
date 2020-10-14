package com.baroque.lujo.activities.ui.mybookings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyBookingsViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public MyBookingsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is my bookings fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}