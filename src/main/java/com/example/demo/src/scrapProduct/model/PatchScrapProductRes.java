package com.example.demo.src.scrapProduct.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
public class PatchScrapProductRes {
    private BigInteger idx;
    private Integer status;
}
