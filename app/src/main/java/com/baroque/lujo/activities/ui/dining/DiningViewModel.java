package com.baroque.lujo.activities.ui.dining;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DiningViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public DiningViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dining fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}