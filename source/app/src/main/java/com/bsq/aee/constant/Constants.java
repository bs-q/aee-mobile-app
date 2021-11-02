package com.bsq.aee.constant;

public class Constants {
    public static final String PREF_NAME = "mvvm.prefs";

    public static final String VALUE_BEARER_TOKEN_DEFAULT="NULL";

    //Local Action manager
    public static final String ACTION_EXPIRED_TOKEN ="ACTION_EXPIRED_TOKEN";

    public static final int CASH=1;
    public static final int CARD=2;
    public static final int DONE=1;
    public static final int CANCEL=2;

    /**
     * # start-of-string
     * # a digit must occur at least once
     * # a lower case letter must occur at least once
     * # an upper case letter must occur at least once
     * # a special character must occur at least once
     * # no whitespace allowed in the entire string
     * # anything, at least eight places though
     * # end-of-string
     * */
    public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

    private Constants(){

    }
}
