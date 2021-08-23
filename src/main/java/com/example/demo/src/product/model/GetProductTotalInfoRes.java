package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetProductTotalInfoRes {
    private ProductInfo productInfo;

    private List<GetProductImageRes> productImageList;
    private List<GetProductInfoImageRes> productInfoImageList;
}
