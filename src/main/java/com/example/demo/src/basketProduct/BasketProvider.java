package com.example.demo.src.basketProduct;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.basketProduct.model.GetBasketMoreInfoRes;
import com.example.demo.src.basketProduct.model.GetBasketRes;
import com.example.demo.src.basketProduct.model.PostBasketReq;
import com.example.demo.src.basketProduct.model.ProductBasketInfo;
import com.example.demo.src.product.model.Product;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class BasketProvider {
    private final BasketDao basketDao;

    public BasketProvider(BasketDao basketDao) {
        this.basketDao = basketDao;
    }


    public List<GetBasketRes> getBasketProducts() throws BaseException {
        if (basketDao.isNullResponseBasketProducts()) {
            throw new BaseException(RESPONSE_NULL_ERROR);
        }

        try {
            List<GetBasketRes> getBasketResList = basketDao.getBasketProducts();
            return getBasketResList;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetBasketMoreInfoRes getBasketProductsByUser(BigInteger userIdx) throws BaseException {
        if (basketDao.isNullResponseBasketProductsByUser(userIdx)) {
            throw new BaseException(RESPONSE_NULL_ERROR_BY_IDX);
        }

        try {
            GetBasketMoreInfoRes getBasketMoreInfoRes = basketDao.getBasketProductsByUser(userIdx);
            return getBasketMoreInfoRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public boolean isDuplicatedBasket(PostBasketReq postBasketReq) throws BaseException {
        try {
            return basketDao.isDuplicatedBasket(postBasketReq);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public boolean isPreCancelledBasket(BigInteger idx) {
        return basketDao.isPreCancelledBasket(idx);
    }
}
