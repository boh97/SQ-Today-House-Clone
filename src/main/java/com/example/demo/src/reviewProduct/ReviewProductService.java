package com.example.demo.src.reviewProduct;

import com.example.demo.config.BaseException;
import com.example.demo.src.reviewProduct.model.PatchReviewProductRes;
import com.example.demo.src.reviewProduct.model.PostReviewProductReq;
import com.example.demo.src.reviewProduct.model.PostReviewProductRes;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigInteger;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class ReviewProductService {
    private final ReviewProductProvider reviewProductProvider;
    private final ReviewProductDao reviewProductDao;

    public ReviewProductService(ReviewProductProvider reviewProductProvider, ReviewProductDao reviewProductDao) {
        this.reviewProductProvider = reviewProductProvider;
        this.reviewProductDao = reviewProductDao;
    }

    @Transactional
    public PostReviewProductRes createReviewProduct(PostReviewProductReq postReviewProductReq) throws BaseException {
        try {
            PostReviewProductRes postReviewProductRes = reviewProductDao.createReview(postReviewProductReq);

            return postReviewProductRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PatchReviewProductRes cancelReviewProduct(BigInteger idx) throws BaseException {
        if (reviewProductDao.isNullResponseReviewsByIdx(idx)) {
            throw new BaseException(RESPONSE_NULL_ERROR_BY_IDX);
        }

        if (reviewProductProvider.isPreCanceledReview(idx)) {
            throw new BaseException(PATCH_REVIEWS_PRE_CANCELED_REVIEW);
        }

        try {
            PatchReviewProductRes patchReviewProductRes = reviewProductDao.cancelReviewProduct(idx);

            return patchReviewProductRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
