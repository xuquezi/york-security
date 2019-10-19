package com.example.york.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PageInfo {
    private long total;//总记录数
    private List rows;//当前页记录

    public PageInfo(long total, List rows) {
        this.total = total;
        this.rows = rows;
    }
}
