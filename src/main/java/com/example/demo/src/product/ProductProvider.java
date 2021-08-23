package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.src.product.model.*;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class ProductProvider {

    private final ProductDao productDao;

    public ProductProvider(ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<GetProductRes> getProducts() throws BaseException {
        if (productDao.isNullResponseProduct()) {
            throw new BaseException(RESPONSE_NULL_ERROR);
        }

        try {
            List<GetProductRes> getProductResList = productDao.getProducts();

            return getProductResList;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetProductRes getProduct(BigInteger idx) throws BaseException {
        if (productDao.isNotExistedProduct(idx)) {
            throw new BaseException(RESPONSE_NULL_ERROR_BY_IDX);
        }

        try {
            GetProductRes getProductRes = productDao.getProduct(idx);

            return getProductRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetProductMoreInfoRes> getProductsMoreInfoByCategory(BigInteger categoryIdx) throws BaseException {
        if (productDao.isNullResponseProductByCategoryIdx(categoryIdx)) {
            throw new BaseException(RESPONSE_NULL_ERROR_BY_IDX);
        }

        try {
            List<GetProductMoreInfoRes> getProductMoreInfoResList = productDao.getProductsMoreInfoByCategory(categoryIdx);

            return getProductMoreInfoResList;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetProductMoreInfoRes> getProductsMoreInfoByCategoryAndOffset(BigInteger categoryIdx, Integer offset) throws BaseException {
        if (productDao.isNullResponseProductByCategoryIdxAndOffset(categoryIdx, offset)) {
            throw new BaseException(RESPONSE_NULL_ERROR_BY_IDX);
        }

        try {
            List<GetProductMoreInfoRes> getProductMoreInfoResList = productDao.getProductsMoreInfoByCategoryAndOffset(categoryIdx, offset);

            return getProductMoreInfoResList;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public boolean isPreExistsProduct(PostProductReq postProductReq) {
        if (productDao.isPreExistsProduct(postProductReq)) {
            return true;
        }
        return false;
    }

    public boolean isPreDeletedProduct(BigInteger idx) {
        if (productDao.isPreDeletedProduct(idx)) {
            return true;
        }
        return false;
    }

    public List<GetProductMoreInfoRes> getProductsMoreInfo() throws BaseException {
        if (productDao.isNullResponseProduct()) {
            throw new BaseException(RESPONSE_NULL_ERROR);
        }

        try {
            List<GetProductMoreInfoRes> getProductMoreInfoResList
                    = productDao.getProductsMoreInfo();

            return getProductMoreInfoResList;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public boolean isNotExistedProduct(BigInteger idx) {
        return productDao.isNotExistedProduct(idx);
    }

    public List<GetProductMoreInfoRes> getProductsMoreInfoByWatching(BigInteger userIdx, Integer offset) throws BaseException {
        if (productDao.isNullResponseWatchByUserIdxAndOffset(userIdx, offset)) {
            throw new BaseException(RESPONSE_NULL_ERROR_BY_IDX);
        }

        try {
            List<GetProductMoreInfoRes> getProductMoreInfoResList = productDao.getProductsMoreInfoByWatching(userIdx, offset);

            return getProductMoreInfoResList;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }

    public List<GetProductImageRes> getProductImages(BigInteger idx) throws BaseException {
        if (productDao.isNullResponseByProductImages(idx)) {
            throw new BaseException(RESPONSE_NULL_ERROR_BY_IDX);
        }

        try {
            List<GetProductImageRes> getProductImageResList = productDao.getProductImages(idx);
            return getProductImageResList;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetProductInfoImageRes> getProductInfoImages(BigInteger idx) throws BaseException {
        if (productDao.isNullResponseByProductInfoImages(idx)) {
            throw new BaseException(RESPONSE_NULL_ERROR_BY_IDX);
        }

        try {
            List<GetProductInfoImageRes> getProductInfoImageResList = productDao.getProductInfoImages(idx);
            return getProductInfoImageResList;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetProductTotalInfoRes getProductTotalInfo(BigInteger idx) throws BaseException {
        if (productDao.isNullResponseProductByIdx(idx)) {
            throw new BaseException(RESPONSE_NULL_ERROR_BY_IDX);
        }

        try {
            GetProductTotalInfoRes getProductTotalInfoRes = new GetProductTotalInfoRes(
                    productDao.getProductInfo(idx),
                    productDao.getProductImages(idx),
                    productDao.getProductInfoImages(idx)
            );

            return getProductTotalInfoRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetProductMoreInfoByUserRes> getProductsMoreInfoByUser(BigInteger userIdx) throws BaseException {
        if (productDao.isNullResponseProduct()) {
            throw new BaseException(RESPONSE_NULL_ERROR);
        }

        try {
            List<GetProductMoreInfoByUserRes> getProductMoreInfoByUserResList = new ArrayList<>();

            List<ProductInfo> productInfoList = productDao.getProductInfo();

            for (int i = 0; i < productInfoList.size(); i++) {
                boolean isScrapped = productDao.getIsScrapped(productInfoList.get(i).getProductName(), userIdx);
                GetProductMoreInfoByUserRes getProductMoreInfoByUserRes = new GetProductMoreInfoByUserRes(productInfoList.get(i), isScrapped);

                getProductMoreInfoByUserResList.add(getProductMoreInfoByUserRes);
            }

            return getProductMoreInfoByUserResList;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
