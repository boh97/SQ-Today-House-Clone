package com.example.demo.src.basketProduct.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetBasketMoreInfoRes {
    private List<ProductBasketInfo> productBasketInfoList;
    private Integer countOfBasket;
    private int totalPrice;
}
