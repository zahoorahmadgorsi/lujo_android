package com.baroque.lujo.activities.ui.aviation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AviationViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<String> mText;

    public AviationViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is aviation fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}