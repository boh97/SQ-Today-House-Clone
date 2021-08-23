package com.example.demo.src.reviewProduct;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.reviewProduct.model.*;
import com.example.demo.src.scrapProduct.model.GetScrapProductMoreInfoRes;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.Validation.*;

@RestController
@RequestMapping("/app/products/reviews")
public class ReviewProductController {
    private final ReviewProductProvider reviewProductProvider;
    private final ReviewProductService reviewProductService;
    private final ReviewProductDao reviewProductDao;

    public ReviewProductController(ReviewProductProvider reviewProductProvider, ReviewProductService reviewProductService, ReviewProductDao reviewProductDao) {
        this.reviewProductProvider = reviewProductProvider;
        this.reviewProductService = reviewProductService;
        this.reviewProductDao = reviewProductDao;
    }

    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetReviewProductRes>> getReviews() {
        try {
            List<GetReviewProductRes> getReviewProductResList
                    = reviewProductProvider.getReviews();

            return new BaseResponse<>(getReviewProductResList);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/{userIdx}")
    public BaseResponse<List<GetReviewProductRes>> getReviewsByUserIdx(@PathVariable BigInteger userIdx) {
        if (userIdx == null) {
            return new BaseResponse<>(EMPTY_IDX);
        }
        if (!isValidatedIdx(userIdx)) {
            return new BaseResponse<>(INVALID_IDX);
        }

        try {
            List<GetReviewProductRes> getReviewProductResList = reviewProductProvider.getReviewsByUserIdx(userIdx);

            return new BaseResponse<>(getReviewProductResList);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/moreInfo/{productIdx}")
    public BaseResponse<List<GetReviewProductMoreInfoRes>> getReviewsMoreInfoByProductIdx(@PathVariable BigInteger productIdx) {
        if (productIdx == null) {
            return new BaseResponse<>(EMPTY_IDX);
        }
        if (!isValidatedIdx(productIdx)) {
            return new BaseResponse<>(INVALID_IDX);
        }

        try {
            List<GetReviewProductMoreInfoRes> getReviewProductMoreInfoResList
                    = reviewProductProvider.getReviewsMoreInfoByProductIdx(productIdx);

            return new BaseResponse<>(getReviewProductMoreInfoResList);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostReviewProductRes> createReviewProduct(@RequestBody PostReviewProductReq postReviewProductReq) {
        if (postReviewProductReq.getProductIdx() == null) {
            return new BaseResponse<>(REVIEWS_EMPTY_PRODUCT_IDX);
        }
        if (postReviewProductReq.getUserIdx() == null) {
            return new BaseResponse<>(REVIEWS_EMPTY_USER_IDX);
        }
        if (postReviewProductReq.getReviewText() == null) {
            return new BaseResponse<>(REVIEWS_EMPTY_REVIEW_TEXT);
        }
        if (postReviewProductReq.getIsTodayHouse() == null) {
            return new BaseResponse<>(REVIEWS_EMPTY_IS_TODAYHOUSE);
        }

        if (postReviewProductReq.getDurabilityStars() == null) {
            return new BaseResponse<>(REVIEWS_EMPTY_DURABILITYSTAR);
        }
        if (postReviewProductReq.getPriceStars() == null) {
            return new BaseResponse<>(REVIEWS_EMPTY_PRICESTAR);
        }
        if (postReviewProductReq.getDesignStars() == null) {
            return new BaseResponse<>(REVIEWS_EMPTY_DESIGNSTAR);
        }
        if (postReviewProductReq.getDeliveryStars() == null) {
            return new BaseResponse<>(REVIEWS_EMPTY_DELIVERYSTAR);
        }

        if (!isValidatedReviewStars(postReviewProductReq.getDurabilityStars(), postReviewProductReq.getPriceStars()
                , postReviewProductReq.getDesignStars(), postReviewProductReq.getDeliveryStars())) {

            return new BaseResponse<>(REVIEWS_INVALID_STARS);
        }

        if (!isValidatedReviewIsTodayHouse(postReviewProductReq.getIsTodayHouse())) {
            return new BaseResponse<>(REVIEWS_INVALID_IS_TODAYHOUSE);
        }

        if (!isValidatedReviewText(postReviewProductReq.getReviewText())) {
            return new BaseResponse<>(REVIEWS_INVALID_REVIEW_TEXT);
        }

        try {
            PostReviewProductRes postReviewProductRes = reviewProductService.createReviewProduct(postReviewProductReq);
            return new BaseResponse<>(postReviewProductRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @PatchMapping("/cancel/{idx}")
    public BaseResponse<PatchReviewProductRes> cancelReviewProduct(@PathVariable BigInteger idx) {
        if (idx == null) {
            return new BaseResponse<>(EMPTY_IDX);
        }
        if (!isValidatedIdx(idx)) {
            return new BaseResponse<>(INVALID_IDX);
        }

        try {
            PatchReviewProductRes patchReviewProductRes = reviewProductService.cancelReviewProduct(idx);
            return new BaseResponse<>(patchReviewProductRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/moreInfo/{productIdx}/{idx}")
    public BaseResponse<GetReviewProductMoreInfoDetailRes> getReviewDetail(@PathVariable("productIdx") BigInteger productIdx, @PathVariable("idx") BigInteger idx) {
        if (productIdx == null || idx == null) {
            return new BaseResponse<>(EMPTY_IDX);
        }
        if (!isValidatedIdx(idx) || !isValidatedIdx(productIdx)) {
            return new BaseResponse<>(INVALID_IDX);
        }

        try {
            GetReviewProductMoreInfoDetailRes getReviewProductMoreInfoDetailRes = reviewProductProvider.getReviewsMoreInfoDetailByProductIdx(productIdx, idx);
            return new BaseResponse<>(getReviewProductMoreInfoDetailRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

    }
}
