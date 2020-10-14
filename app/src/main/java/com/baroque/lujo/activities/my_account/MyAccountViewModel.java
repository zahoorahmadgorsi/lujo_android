package com.baroque.lujo.activities.my_account;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.baroque.lujo.activities.login.UserModel;

import static utilities.Constants.PREF_FILE_NAME;
import static utilities.Key.KEY_CURRENT_USER;
import static utilities.Utility.getSavedObjectFromPreference;

//extending with AndroidViewModel to have application context to read token
public class MyAccountViewModel extends AndroidViewModel {
    private final String TAG = getClass().getSimpleName();

    private UserModel user;
    public  UserModel getUser() {
        return user;
    }

    public MyAccountViewModel(@NonNull Application application) {
        super(application);
        user = getSavedObjectFromPreference(getApplication(),PREF_FILE_NAME,KEY_CURRENT_USER,UserModel.class);
    }
}
