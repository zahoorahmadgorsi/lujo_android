package utilities;

public class Action {
//    public static final int SHOW_WELCOME = 0;
//    public static final int SHOW_INVALID_PASSWARD_OR_LOGIN = 1;
    private final Constants.PAGE_TYPE mAction;

    public Action(Constants.PAGE_TYPE action) {
        mAction = action;
    }

    public Constants.PAGE_TYPE getValue() {
        return mAction;
    }
}
