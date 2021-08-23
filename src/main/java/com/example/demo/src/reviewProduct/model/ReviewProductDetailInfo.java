package com.example.demo.src.reviewProduct.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReviewProductDetailInfo {
    private String userName;
    private String productName;     //새로 추가된 부분
    private Integer durabilityStars;
    private Integer priceStars;
    private Integer designStars;
    private Integer deliveryStars;
    private String imageUrl;
    private String reviewText;
    private String isTodayHouse; // "오늘의집 구매", "다른 쇼핑몰 구매"
    private String createdAt;
    private Float totalStars;
    private Integer likes;
}
