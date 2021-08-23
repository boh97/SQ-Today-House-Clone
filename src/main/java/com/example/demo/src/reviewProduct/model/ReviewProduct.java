package com.example.demo.src.reviewProduct.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class ReviewProduct {
    private BigInteger idx;
    private BigInteger userIdx;
    private BigInteger productIdx;
    private String reviewText;
    private String imageUrl;
    private String isTodayHouse;    //'Y' / 'N'
    private Integer durabilityStars;
    private Integer priceStars;
    private Integer designStars;
    private Integer deliveryStars;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Integer status;
}
