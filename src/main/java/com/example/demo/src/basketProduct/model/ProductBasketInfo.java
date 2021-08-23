package com.example.demo.src.basketProduct.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
public class ProductBasketInfo {
    private BigInteger productIdx;
    private String productName;
    private Integer price;
    private String productImageUrl;
    private String deliveryType;

}
