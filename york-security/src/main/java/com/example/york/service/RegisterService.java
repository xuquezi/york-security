package com.example.york.service;

import com.aliyuncs.CommonResponse;

public interface RegisterService {
    CommonResponse sendCode(String tel);
}
