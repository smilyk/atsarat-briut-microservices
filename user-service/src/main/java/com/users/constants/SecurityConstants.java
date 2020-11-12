package com.users.constants;

import com.users.SpringApplicationContext;
import com.users.security.AppProperties;

public class SecurityConstants {
    public static final String SIGN_UP_URL = "/users/v1";
    public static final String VERIFICATION_EMAIL_URL = "/users/email-verification";
    public static final String PASSWORD_RESET_REQUEST_URL = "/users/password-reset-request";
    public static final String PASSWORD_RESET_URL = "/users/password-reset";
    public static final String CREATE_ADMIN = "/users/create-admin";

    public static final long EXPIRATION_TIME = 864000000; // 10 days
    public static final String HEADER_STRING = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_SECRET = "fgjhjghjkfhgkjdghkfjg";


//    public static String getTokenSecret()
//    {
//        AppProperties appProperties = (AppProperties) SpringApplicationContext.getBean("AppProperties");
//        return appProperties.getTokenSecret();
//    }

}
