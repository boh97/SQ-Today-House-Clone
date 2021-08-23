package com.example.demo.src.reviewProduct.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetReviewProductMoreInfoDetailRes {
    private ReviewProductDetailInfo reviewInfo;

    private Integer totalReviews;
}
