package com.example.demo.src.basketProduct;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.basketProduct.model.*;
import lombok.Getter;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.Validation.isValidatedIdx;

@RestController
@RequestMapping("/app/products/baskets")
public class BasketController {

    private final BasketProvider basketProvider;
    private final BasketService basketService;
    private final BasketDao basketDao;

    public BasketController(BasketProvider basketProvider, BasketService basketService, BasketDao basketDao) {
        this.basketProvider = basketProvider;
        this.basketService = basketService;
        this.basketDao = basketDao;
    }


    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetBasketRes>> getBasketProducts() {
        try {
            List<GetBasketRes> getBasketResList = basketProvider.getBasketProducts();
            return new BaseResponse<>(getBasketResList);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/moreInfo/{userIdx}")
    public BaseResponse<GetBasketMoreInfoRes> getBasketProductsByUser(@PathVariable("userIdx") BigInteger userIdx) {
        if (userIdx == null) {
            return new BaseResponse<>(EMPTY_IDX);
        }
        if (!isValidatedIdx(userIdx)) {
            return new BaseResponse<>(INVALID_IDX);
        }

        try {
            GetBasketMoreInfoRes getBasketMoreInfoRes = basketProvider.getBasketProductsByUser(userIdx);
            return new BaseResponse<>(getBasketMoreInfoRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostBasketRes> createBasket(@RequestBody PostBasketReq postBasketReq) {
        if (postBasketReq.getUserIdx() == null) {
            return new BaseResponse<>(POST_BASKET_EMPTY_USERIDX);
        }
        if (postBasketReq.getProductIdx() == null) {
            return new BaseResponse<>(POST_BASKET_EMPTY_PRODUCTIDX);
        }

        try {
            PostBasketRes postBasketRes = basketService.createBasket(postBasketReq);
            return new BaseResponse<>(postBasketRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @PatchMapping("/cancel/{idx}")
    public BaseResponse<PatchBasketRes> cancelBasket(@PathVariable("idx") BigInteger idx) {
        if (idx == null) {
            return new BaseResponse<>(EMPTY_IDX);
        }
        if (!isValidatedIdx(idx)) {
            return new BaseResponse<>(INVALID_IDX);
        }

        try {
            PatchBasketRes patchBasketRes = basketService.cancelBasket(idx);
            return new BaseResponse<>(patchBasketRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

    }

}
