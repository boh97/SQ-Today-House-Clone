package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
public class PostUserReq {
    private String email;
    private String password;
    private String nickname;
    private String housingType;
    private String houseSize;
    private String birthDay;
    private String sex;
    private Integer status;
}
