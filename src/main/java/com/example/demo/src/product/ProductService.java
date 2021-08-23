package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.src.product.model.PatchProductRes;
import com.example.demo.src.product.model.PostProductReq;
import com.example.demo.src.product.model.PostProductRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class ProductService {
    private final ProductProvider productProvider;
    private final ProductDao productDao;

    @Autowired
    public ProductService(ProductProvider productProvider, ProductDao productDao) {
        this.productProvider = productProvider;
        this.productDao = productDao;
    }

    public PostProductRes createProduct(PostProductReq postProductReq) throws BaseException {
        //이름 중복
        if (productProvider.isPreExistsProduct(postProductReq)) {
            throw new BaseException(POST_PRODUCTS_PRE_EXIST_PRODUCT);
        }

        try {
            return productDao.createProduct(postProductReq);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PatchProductRes deleteProduct(BigInteger idx) throws BaseException {
        if (productProvider.isNotExistedProduct(idx)) {
            throw new BaseException(RESPONSE_NULL_ERROR_BY_IDX);
        }

        if (productProvider.isPreDeletedProduct(idx)) {
            throw new BaseException(PATCH_PRE_DELETED_PRODUCT);
        }

        try {
            return productDao.deleteProduct(idx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
