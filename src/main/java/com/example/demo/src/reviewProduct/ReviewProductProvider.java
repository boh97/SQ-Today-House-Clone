package com.example.demo.src.reviewProduct;

import com.example.demo.config.BaseException;
import com.example.demo.src.reviewProduct.model.*;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class ReviewProductProvider {
    private final ReviewProductDao reviewProductDao;

    public ReviewProductProvider(ReviewProductDao reviewProductDao) {
        this.reviewProductDao = reviewProductDao;
    }

    public List<GetReviewProductRes> getReviews() throws BaseException {
        if (reviewProductDao.isNullResponseReviews()) {
            throw new BaseException(RESPONSE_NULL_ERROR);
        }

        try {
            List<GetReviewProductRes> getReviewProductResList = reviewProductDao.getReviews();

            return getReviewProductResList;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetReviewProductRes> getReviewsByUserIdx(BigInteger userIdx) throws BaseException {
        if (reviewProductDao.isNullResponseReviewsByUserIdx(userIdx)) {
            throw new BaseException(RESPONSE_NULL_ERROR_BY_IDX);
        }

        try {
            List<GetReviewProductRes> getReviewProductResList = reviewProductDao.getReviewsByUserIdx(userIdx);

            return getReviewProductResList;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public boolean isPreCanceledReview(BigInteger idx) throws BaseException {
        try {
            if (reviewProductDao.isPreCanceledReview(idx)) {
                return true;
            }

            return false;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetReviewProductMoreInfoRes> getReviewsMoreInfoByProductIdx(BigInteger productIdx)throws BaseException {
        if (reviewProductDao.isNullResponseReviewsByProductIdx(productIdx)) {
            throw new BaseException(RESPONSE_NULL_ERROR_BY_IDX);
        }

        try {
            return reviewProductDao.getReviewsMoreInfoByProductIdx(productIdx);
        } catch (Exception exception) {
            throw  new BaseException(DATABASE_ERROR);
        }
    }

    public GetReviewProductMoreInfoDetailRes getReviewsMoreInfoDetailByProductIdx(BigInteger productIdx, BigInteger idx) throws BaseException {
        if (reviewProductDao.isNullResponseDetailReviewsByProductIdx(productIdx, idx)) {
            throw new BaseException(RESPONSE_NULL_ERROR_BY_IDX);
        }

        try {
            ReviewProductDetailInfo reviewProductDetailInfo = reviewProductDao.getReviewsDetailInfoByProductIdxAndIdx(productIdx, idx);
            Integer totalReviews = reviewProductDao.getCountOfTotalReviews(productIdx);

            return new GetReviewProductMoreInfoDetailRes(reviewProductDetailInfo, totalReviews);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
