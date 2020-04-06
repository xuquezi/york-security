package com.example.york.constant;

public class Const {
    // token有效期半小时(以毫秒ms计)
    public static final long EXPIRATION_TIME = 1800_000;
    // JWT密码
    public static final String SECRET = "YorkSecret";
    // 存放Token的Header Key
    public static final String HEADER_STRING = "Authorization";
    // 流程审批同意
    public static final String AGREE = "Yes";
    // 流程审批拒绝
    public static final String DISAGREE = "No";
    // 新增用户时默认头像url地址
    public static final String DEFAULT_AVATAR_URL = "https://york-vue-1259592596.cos.ap-chengdu.myqcloud.com/avatar/964e40b005724165b8cf772355796c8c.jpeg";
    // 新增用户时默认密码
    public static final String DEFAULT_PASSWORD = "123456";

    public static final String CANCEL = "cancel";

    public static final String FINISH = "finish";

    public static final String FLOW_CANCEL = "cancel";
    public static final String FLOW_BACK = "back";
    public static final String FLOW_SUBMIT = "submit";
    public static final String FLOW_FINISH = "finish";
}
