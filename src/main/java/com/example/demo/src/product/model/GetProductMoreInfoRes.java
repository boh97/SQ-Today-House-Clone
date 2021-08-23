package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetProductMoreInfoRes {
    private String productName;
    private Integer reviews;
    private Integer price;
    private Float totalStars;
    private String brandName;
    private String todayDeal;
    private String deliveryType;
    private String productImageUrl;
}
