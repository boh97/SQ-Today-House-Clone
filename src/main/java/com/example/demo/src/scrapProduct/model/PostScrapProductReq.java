package com.example.demo.src.scrapProduct.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class PostScrapProductReq {
    private BigInteger userIdx;
    private BigInteger productIdx;
}
