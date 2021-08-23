package com.example.demo.src.todayDeal.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class PostTodayDealRes {
    private BigInteger idx;
    private Integer status;
}
