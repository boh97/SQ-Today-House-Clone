package com.example.demo.src.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
public class PostOrderReq {
    private BigInteger userIdx;
    private BigInteger productIdx;
    private String status;
}
