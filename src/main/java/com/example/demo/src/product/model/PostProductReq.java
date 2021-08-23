package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class PostProductReq {
    private BigInteger brandIdx;
    private BigInteger categoryIdx;
    private String name;
    private Integer price;
    private String deliveryType;
    private String isTodayDeal;
    // private Integer status; // 1
    // private Float totalStar; // 0
}
