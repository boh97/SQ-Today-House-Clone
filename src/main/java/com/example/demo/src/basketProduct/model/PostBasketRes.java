package com.example.demo.src.basketProduct.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
public class PostBasketRes {
    private BigInteger idx;
    private Integer status;
}
