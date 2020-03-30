package com.example.york.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CodeLib {

    private String codeId;

    private String codeType;

    private String codeNo;

    private String codeName;
    // 描述
    private String codeDesc;
    // 创建时间
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date createTime;
    // 创建用户
    private String createUser;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    // 更新时间
    private Date updateTime;
    // 更新用户
    private String updateUser;

    private String attr1;


}
