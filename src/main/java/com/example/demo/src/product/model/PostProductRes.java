package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.criteria.CriteriaBuilder;
import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
public class PostProductRes {
    private BigInteger idx;
    private Integer status;
}
