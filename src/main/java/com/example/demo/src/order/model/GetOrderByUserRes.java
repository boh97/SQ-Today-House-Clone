package com.example.demo.src.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
public class GetOrderByUserRes {
    private BigInteger userIdx;
    private Integer countOfWaitingPayment;
    private Integer countOfCompletePayment;
    private Integer countOfReadyToShipping;
    private Integer countOfShipping;
    private Integer countOfCompleteShipping;
    private Integer countOfWritingReview;
}
