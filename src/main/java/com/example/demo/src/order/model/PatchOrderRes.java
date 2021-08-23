package com.example.demo.src.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
public class PatchOrderRes {
    private BigInteger idx;
    private String status;
}
