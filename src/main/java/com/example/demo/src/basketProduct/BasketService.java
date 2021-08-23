package com.example.demo.src.basketProduct;

import com.example.demo.config.BaseException;
import com.example.demo.src.basketProduct.model.PatchBasketRes;
import com.example.demo.src.basketProduct.model.PostBasketReq;
import com.example.demo.src.basketProduct.model.PostBasketRes;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class BasketService {
    private final BasketProvider basketProvider;
    private final BasketDao basketDao;


    public BasketService(BasketProvider basketProvider, BasketDao basketDao) {
        this.basketProvider = basketProvider;
        this.basketDao = basketDao;
    }

    public PostBasketRes createBasket(PostBasketReq postBasketReq) throws BaseException {
        if (basketProvider.isDuplicatedBasket(postBasketReq)) {
            throw new BaseException(POST_BASKET_DUPLICATED);
        }

        try {
            PostBasketRes postBasketRes = basketDao.createBasket(postBasketReq);
            return postBasketRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PatchBasketRes cancelBasket(BigInteger idx) throws BaseException {
        if (basketProvider.isPreCancelledBasket(idx)) {
            throw new BaseException(PATCH_PRE_DELETED_BASKET);
        }

        try {
            PatchBasketRes patchBasketRes = basketDao.cancelBasket(idx);
            return patchBasketRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
