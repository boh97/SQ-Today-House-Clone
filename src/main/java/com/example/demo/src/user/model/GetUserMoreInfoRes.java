package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserMoreInfoRes {
    private String userName;
    private String userGrade;
    private Integer point;
    private Integer scraps;
    private Integer likes;
    //private Integer coupons; //기존 DB가 너무 크다는 피드백 반영해서 쿠폰은 구현하지 않는다!
    private Integer countOfWaitingPayment;
    private Integer countOfCompletePayment;
    private Integer countOfReadyToShipping;
    private Integer countOfShipping;
    private Integer countOfCompleteShipping;
    private Integer countOfWritingReview;
    private Integer reviews;
    private Integer questionsAndAnswers;
}
