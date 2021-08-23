package com.example.demo.src.scrapProduct;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.scrapProduct.model.*;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.Validation.*;

@RestController
@RequestMapping("/app/products/scraps")
public class ScrapProductController {

    private final ScrapProductProvider scrapProductProvider;
    private final ScrapProductService scrapProductService;
    private final ScrapProductDao scrapProductDao;

    public ScrapProductController(ScrapProductProvider scrapProductProvider, ScrapProductService scrapProductService, ScrapProductDao scrapProductDao) {
        this.scrapProductProvider = scrapProductProvider;
        this.scrapProductService = scrapProductService;
        this.scrapProductDao = scrapProductDao;
    }

    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetScrapProductRes>> getScrapsProduct() {
        try {
            List<GetScrapProductRes> getScrapProductResList = scrapProductProvider.getScrapsProduct();
            return new BaseResponse<>(getScrapProductResList);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/{userIdx}")
    public BaseResponse<List<GetScrapProductRes>> getScrapsProductByUserIdx(@PathVariable BigInteger userIdx) {
        if (userIdx == null) {
            return new BaseResponse<>(EMPTY_IDX);
        }
        if (!isValidatedIdx(userIdx)) {
            return new BaseResponse<>(INVALID_IDX);
        }

        try {
            List<GetScrapProductRes> getScrapProductResList = scrapProductProvider.getScrapsProductByUserIdx(userIdx);
            return new BaseResponse<>(getScrapProductResList);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/moreInfo/{userIdx}")
    public BaseResponse<List<GetScrapProductMoreInfoRes>> getScrapsProductMoreInfoByUserIdx(@PathVariable BigInteger userIdx) {
        if (userIdx == null) {
            return new BaseResponse<>(EMPTY_IDX);
        }
        if (!isValidatedIdx(userIdx)) {
            return new BaseResponse<>(INVALID_IDX);
        }

        try {
            List<GetScrapProductMoreInfoRes> getScrapProductMoreInfoResList
                    = scrapProductProvider.getScrapsProductMoreInfoByUserIdx(userIdx);

            return new BaseResponse<>(getScrapProductMoreInfoResList);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

//    @ResponseBody
//    @GetMapping("/moreInfo/{productIdx}")
//    public BaseResponse<List<GetScrapProductMoreInfoRes>> getScrapsProductMoreInfoByProductIdx(@PathVariable BigInteger productIdx) {
//        if (productIdx == null) {
//            return new BaseResponse<>(EMPTY_IDX);
//        }
//        if (!isValidatedIdx(productIdx)) {
//            return new BaseResponse<>(INVALID_IDX);
//        }
//
//        try {
//            List<GetScrapProductMoreInfoRes> getScrapProductMoreInfoResList
//                    = scrapProductProvider.getScrapsProductMoreInfoByProductIdx(productIdx);
//
//            return new BaseResponse<>(getScrapProductMoreInfoResList);
//        } catch (BaseException exception) {
//            return new BaseResponse<>(exception.getStatus());
//        }
//    }

    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostScrapProductRes> createScrapProduct(@RequestBody PostScrapProductReq postScrapProductReq) {
        if (postScrapProductReq.getUserIdx() == null) {
            return new BaseResponse<>(POST_SCRAPS_PRODUCT_EMPTY_USERIDX);
        }
        if (postScrapProductReq.getProductIdx() == null) {
            return new BaseResponse<>(POST_SCRAPS_PRODUCT_EMPTY_PRODUCTIDX);
        }

        if (!isValidatedIdx(postScrapProductReq.getUserIdx()) || !isValidatedIdx(postScrapProductReq.getProductIdx())) {
            return new BaseResponse<>(INVALID_IDX);
        }

        try {
            PostScrapProductRes postScrapProductRes = scrapProductService.createScrapProduct(postScrapProductReq);
            return new BaseResponse<>(postScrapProductRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @PatchMapping("/cancel/{idx}")
    public BaseResponse<PatchScrapProductRes> cancelScrap(@PathVariable BigInteger idx) {
        if (idx == null) {
            return new BaseResponse<>(EMPTY_IDX);
        }
        if (!isValidatedIdx(idx)) {
            return new BaseResponse<>(INVALID_IDX);
        }

        try {
            PatchScrapProductRes patchScrapProductRes = scrapProductService.cancelScrap(idx);

            return new BaseResponse<>(patchScrapProductRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
