package com.example.demo.src.scrapProduct.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.criteria.CriteriaBuilder;

@Getter
@Setter
@AllArgsConstructor
public class GetScrapProductMoreInfoRes {
    private String productName;
    private String brandName;
    private String productImageUrl;
    private Integer reviews;
    private Integer price;
    private Float totalStars;
    private String todayDeal;
    private String deliveryType;
}
