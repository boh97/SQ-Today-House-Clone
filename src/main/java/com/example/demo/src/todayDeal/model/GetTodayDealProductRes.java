package com.example.demo.src.todayDeal.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class GetTodayDealProductRes {
    private BigInteger idx;
    private BigInteger productIdx;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Integer status;
}
