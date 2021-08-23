package com.example.demo.src.scrapProduct.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.criteria.CriteriaBuilder;
import java.math.BigInteger;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class ScrapProduct {
    private BigInteger idx;
    private BigInteger userIdx;
    private BigInteger productIdx;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Integer status;
}
