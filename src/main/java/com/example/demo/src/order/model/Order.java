package com.example.demo.src.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class Order {
    private BigInteger idx;
    private BigInteger userIdx;
    private BigInteger productIdx;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String status;
}
