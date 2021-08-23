package com.example.demo.src.todayDeal.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
public class PatchTodayDealRes {
    private BigInteger idx;
    private Integer status;
}
