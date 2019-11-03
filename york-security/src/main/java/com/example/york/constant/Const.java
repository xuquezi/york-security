package com.example.york.constant;

public class Const {
    public static final long EXPIRATION_TIME = 1800_000;     // token有效期半小时(以毫秒ms计)
    public static final String SECRET = "YorkSecret";      // JWT密码     // Token前缀
    public static final String HEADER_STRING = "Authorization" +
            ""; // 存放Token的Header Key
    //public static final String TOKEN_PREFIX = "Bearer";
    public static final String SIGN_NAME = "york";
    public static final String TEMPLATE_CODE = "SMS_176940929";
}
