package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class GetProductRes {
    private BigInteger idx;
    private BigInteger brandIdx;
    private BigInteger categoryIdx;
    private String name;
    private Integer price;
    private String deliveryType;
    private String isTodayDeal;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Integer status;
    private Float totalStar;
}
