package com.example.demo.src.todayDeal.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class GetTodayDealProductMoreInfoRes {
    private String productName;
    private Integer reviews;
    private Integer price;
    private Float totalStars;
    private String brandName;
    private String todayDeal;
    private String deliveryType;
    private String productImageUrl;
}
