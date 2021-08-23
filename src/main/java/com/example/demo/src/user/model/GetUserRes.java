package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class GetUserRes {
    private BigInteger idx;
    private String email;
    private String password;
    private String nickname;
    private String grade;
    private Integer point;
    private String housingType;
    private String houseSize;
    private String birthDay;
    private String sex;
    private String socialLoginType;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Integer status;
}
