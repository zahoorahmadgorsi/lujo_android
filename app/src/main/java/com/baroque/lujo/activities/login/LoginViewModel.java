package com.baroque.lujo.activities.login;

import android.app.Application;
import android.text.TextUtils;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.baroque.lujo.activities.country.CountryModel;
import model.api_response.TokenAndExpiry;
import model.api_response.GenericResponse;
import utilities.Constants;
import utilities.Resource;
import utilities.Utility;

import static com.baroque.lujo.activities.login.UserModel.SignUpErrors.EMAIL;
import static com.baroque.lujo.activities.login.UserModel.SignUpErrors.FIRST_NAME;
import static com.baroque.lujo.activities.login.UserModel.SignUpErrors.LAST_NAME;
import static com.baroque.lujo.activities.login.UserModel.SignUpErrors.PHONE_NUMBER;
import static com.baroque.lujo.activities.login.UserModel.SignUpErrors.TERMS_AND_CONDITIONS;
import static utilities.Constants.PREF_FILE_NAME;
import static utilities.Key.KEY_CURRENT_USER;
import static utilities.Key.KEY_TOKEN_EXPIRY;
import static utilities.Utility.getSavedObjectFromPreference;

//extending with AndroidViewModel to have application context to read token
public class LoginViewModel extends AndroidViewModel {

    private final String TAG = getClass().getSimpleName();
    //Quick brown fox

//    enable all below for production and disable all below for staging
//    public MutableLiveData<String> userAlpha2Code = new MutableLiveData<>("US");
//    public MutableLiveData<String> userPhonePrefix = new MutableLiveData<>("+1");
//    public MutableLiveData<String> userPhoneNumber = new MutableLiveData<>();
//    public MutableLiveData<String> userFirstName = new MutableLiveData<>();
//    public MutableLiveData<String> userLastName = new MutableLiveData<>();
//    public MutableLiveData<String> userEmail = new MutableLiveData<>();
//    public MutableLiveData<String> userOldPhonePrefix = new MutableLiveData<>();
//    public MutableLiveData<String> userOldPhoneNumber = new MutableLiveData<>();

//    //enable all below for staging and disable all below for production
    public MutableLiveData<String> userAlpha2Code = new MutableLiveData<>("AE");
    public MutableLiveData<String> userPhonePrefix = new MutableLiveData<>("+971");
    public MutableLiveData<String> userPhoneNumber = new MutableLiveData<>("589108662");
    public MutableLiveData<String> userFirstName = new MutableLiveData<>("zahoor ahmad");
    public MutableLiveData<String> userLastName = new MutableLiveData<>("gorsi");
    public MutableLiveData<String> userEmail = new MutableLiveData<>("zahoor.gorsi4@gmail.com");
    public MutableLiveData<String> userOldPhonePrefix = new MutableLiveData<>("+971");
    public MutableLiveData<String> userOldPhoneNumber = new MutableLiveData<>("543916370");




    //Current Logging in User
    private UserModel user;
    public  UserModel getUser() {
        user = getSavedObjectFromPreference(getApplication(),PREF_FILE_NAME,KEY_CURRENT_USER,UserModel.class);
        if (user == null){
            user =  new UserModel(userFirstName.getValue(), userLastName.getValue(), userEmail.getValue(), userPhonePrefix.getValue(), userPhoneNumber.getValue(),userOldPhonePrefix.getValue(),userOldPhoneNumber.getValue(),userAlpha2Code.getValue());
        }
        return user;
    }

    public void setUser( UserModel user)
    {
        this.userFirstName.setValue(user.getFirstName());
        this.userLastName.setValue(user.getLastName());
        this.userEmail.setValue(user.getEmail());
        this.userPhonePrefix.setValue(user.getPhonePrefix());
        this.userPhoneNumber.setValue(user.getPhoneNumber());
        this.userOldPhonePrefix.setValue(user.getOldPhonePrefix());
        this.userOldPhoneNumber.setValue(user.getOldPhoneNumber());
        String userAlpha2Code = "US";
        if (!TextUtils.isEmpty(user.getAlpha2Code())) { // if no alpha 2 code is available
            userAlpha2Code = user.getAlpha2Code();
        }
        this.userAlpha2Code.setValue( userAlpha2Code );
        this.user = user;
    }



    private LoginRepository loginRepository;
    public LoginViewModel(Application application){
        super(application);
        loginRepository = new LoginRepository();

    }

    CountryModel selectedCountry = new CountryModel(Constants.COUNTRY_CODE_US,"US", "+1","American", "United States of America","");    //default
    public void setSelectedCountry(CountryModel selectedCountry) {
        this.selectedCountry = selectedCountry;
        this.userPhonePrefix.setValue(selectedCountry.getPhonePrefix());
        this.userAlpha2Code.setValue(selectedCountry.getAlpha2Code());
    }
    public CountryModel getSelectedCountry() {
        return selectedCountry;
    }

    private MutableLiveData<Boolean> isBusy;
    public MutableLiveData<Boolean> getIsBusy() {
        if (isBusy == null) {
            isBusy = loginRepository.getIsBusy();
        }
        return isBusy;
    }

    private Constants.PAGE_TYPE pageType;
    public Constants.PAGE_TYPE getPageType() {
        return pageType;
    }
    public void setPageType(Constants.PAGE_TYPE pageType) {
        this.pageType = pageType;
    }

    private MutableLiveData<Boolean> termsAndConditions;
    public MutableLiveData<Boolean> getTermsAndConditions() {
        if (termsAndConditions == null){
            termsAndConditions = new MutableLiveData<>();
            termsAndConditions.setValue(false);
        }
        return termsAndConditions;
    }

    //this variable is used to validate all user textfields at the time of signup
    private MutableLiveData<UserModel> isUserDataValid;
    public LiveData<UserModel> getIsUserDataValid() {
        if (isUserDataValid == null) {
            isUserDataValid = new MutableLiveData<>();
        }
        return isUserDataValid;
    }

    private MutableLiveData<Resource<GenericResponse>> mSignUpInResponse;
    public LiveData<Resource<GenericResponse>> getSignUpInResponse() {
        if(mSignUpInResponse == null){
            mSignUpInResponse = loginRepository.getSignUpInResponse();
        }
        return mSignUpInResponse;
    }

//    private MutableLiveData<Resource<GenericResponse>> mLoginResponse;
//    public LiveData<Resource<GenericResponse>> getLoginResponse() {
//        if(mLoginResponse == null){
//            mLoginResponse = loginRepository.getLoginResponse();
//        }
//        return mLoginResponse;
//    }

    //LiveData object for change password request
    private MutableLiveData<Resource<GenericResponse>> mChangePhoneNumberResponse;
    public LiveData<Resource<GenericResponse>> getChangePasswordResponse() {
        if(mChangePhoneNumberResponse == null){
            mChangePhoneNumberResponse = loginRepository.getChangePhoneNumberResponse();
        }
        return mChangePhoneNumberResponse;
    }

    public void onLoginClicked() {
        switch(pageType){
            case SIGN_IN:
                loginRepository.login(userPhonePrefix.getValue(), userPhoneNumber.getValue());
                break;
            case SIGN_UP:
                user = this.getUser(); //in case user is null
                //user =  new UserModel(userFirstName.getValue(), userLastName.getValue(), userEmail.getValue(), userPhonePrefix.getValue(), userPhoneNumber.getValue(),userOldPhonePrefix.getValue(),userOldPhoneNumber.getValue(),userAlpha2Code.getValue());
                if (isInputValid(user)){
                    loginRepository.signUp(user);
                }
                break;
            case CHANGE_PHONE_NUMBER:
                user = this.getUser(); //incase user is null
                //repopulating with the latest values
                user =  new UserModel(userFirstName.getValue(), userLastName.getValue(), userEmail.getValue(), userPhonePrefix.getValue(), userPhoneNumber.getValue(),userOldPhonePrefix.getValue(),userOldPhoneNumber.getValue(),userAlpha2Code.getValue());
                loginRepository.changePhoneNumber(user.getOldPhonePrefix() ,user.getOldPhoneNumber(), user.getPhonePrefix(), user.getPhoneNumber());
                break;
            case EDIT_PROFILE:
                TokenAndExpiry tokenAndExpiry = getSavedObjectFromPreference(getApplication(),PREF_FILE_NAME,KEY_TOKEN_EXPIRY, TokenAndExpiry.class);
                user =  new UserModel(userFirstName.getValue(), userLastName.getValue(), userEmail.getValue(), userPhonePrefix.getValue(), userPhoneNumber.getValue(),"","","");
                if (isInputValid(user) && tokenAndExpiry !=null) {
                    loginRepository.editProfile(user.getFirstName(), user.getLastName(),user.getEmail(), user.getPhonePrefix(), user.getPhoneNumber(),tokenAndExpiry.getToken());
                }
                break;
        }

    }

    //this method is used to validate the input at user sign up
    private boolean isInputValid(UserModel user){
        if ((getPageType() == Constants.PAGE_TYPE.SIGN_UP || getPageType() == Constants.PAGE_TYPE.EDIT_PROFILE) && TextUtils.isEmpty(user.getFirstName())){
            user.setErrorType(FIRST_NAME);
            user.setErrorMessage("First name is required.");
            isUserDataValid.setValue(user);
            return false;
        }else if ((getPageType() == Constants.PAGE_TYPE.SIGN_UP || getPageType() == Constants.PAGE_TYPE.EDIT_PROFILE) && TextUtils.isEmpty(user.getLastName())){
            user.setErrorType(LAST_NAME);
            user.setErrorMessage("Last name is required.");
            isUserDataValid.setValue(user);
            return false;
        }else if ((getPageType() == Constants.PAGE_TYPE.SIGN_UP || getPageType() == Constants.PAGE_TYPE.EDIT_PROFILE) && !Utility.isValidEmail(user.getEmail() )){
            user.setErrorType(EMAIL);
            user.setErrorMessage("Valid email is required.");
            isUserDataValid.setValue(user);
            return false;
        }
        else if (TextUtils.isEmpty(user.getPhoneNumber()) || user.getPhoneNumber().length() < 6){
            user.setErrorType(PHONE_NUMBER);
            user.setErrorMessage("Valid phone number is required.");
            isUserDataValid.setValue(user);
            return false;
        }else if (getPageType() == Constants.PAGE_TYPE.SIGN_UP && getTermsAndConditions().getValue() == false ){
            user.setErrorType(TERMS_AND_CONDITIONS);
            user.setErrorMessage("Agreeing with terms and conditions is required.");
            isUserDataValid.setValue(user);
            return false;
        }
        isUserDataValid.setValue(user);
        return true;
    }

}


