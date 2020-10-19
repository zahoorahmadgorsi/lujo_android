package utilities;

import com.baroque.lujo.activities.login.UserModel;
import com.baroque.lujo.activities.my_account.MyAccountActivity;

import static utilities.Key.KEY_CURRENT_USER;
import static utilities.Utility.saveObjectToSharedPreference;

public final class Constants {
    public static String ACTION_TO_PERFORM = "SignInOrSignUp";
    public static String INTENT_FIRST_PARAM = "IntentFirstParameter";
//    public static String INTENT_SECOND_PARAM = "IntentSecondParameter";
    public static String COUNTRY = "COUNTRY";
    public static String PREF_FILE_NAME = "LUJO";
    public static enum PAGE_TYPE{ SIGN_UP , SIGN_IN, CHANGE_PHONE_NUMBER, EDIT_PROFILE} ;
    public enum MEMBERSHIP_TYPE {
        FREE,
        DINING,
        ALL_ACCESS
    }
    public enum MEMBERSHIP_PAGE_TYPE {
        UPGRADE,
        NOT_UPGRADE
    }
    public static String URL_TERMS_AND_CONDITIONS = "http://golujo.com/tos/";
    //Country Code
    public static final int COUNTRY_CODE_US = 238;
    public static int SPLASH_SCREEN_TIME_OUT = 1 ; //1 second
}
