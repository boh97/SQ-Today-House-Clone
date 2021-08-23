package com.example.demo.src.reviewProduct.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
public class PatchReviewProductRes {
    private BigInteger idx;
    private Integer status;
}
