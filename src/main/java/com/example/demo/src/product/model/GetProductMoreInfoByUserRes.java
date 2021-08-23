package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetProductMoreInfoByUserRes {
    private ProductInfo productInfo;
    private boolean isScrapped;
}
