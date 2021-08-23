package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.product.model.*;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.Validation.*;

@RestController
@RequestMapping("/app/products")
public class ProductController {

    private final ProductProvider productProvider;
    private final ProductService productService;
    private final ProductDao productDao;

    public ProductController(ProductProvider productProvider, ProductService productService, ProductDao productDao) {
        this.productProvider = productProvider;
        this.productService = productService;
        this.productDao = productDao;
    }

    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetProductRes>> getProducts() {
        try {
            List<GetProductRes> getProductResList = productProvider.getProducts();
            return new BaseResponse<>(getProductResList);
        } catch(BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/{idx}")
    public BaseResponse<GetProductRes> getProduct(@PathVariable BigInteger idx) {
        if (idx == null) {
            return new BaseResponse<>(EMPTY_IDX);
        }
        if (!isValidatedIdx(idx)) {
            return new BaseResponse<>(INVALID_IDX);
        }

        try {
            GetProductRes getProductRes = productProvider.getProduct(idx);

            return new BaseResponse<>(getProductRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/moreInfo")
    public BaseResponse<List<GetProductMoreInfoRes>> getProductsMoreInfo() {
        try {
            List<GetProductMoreInfoRes> getProductMoreInfoResList
                    = productProvider.getProductsMoreInfo();

            return new BaseResponse<>(getProductMoreInfoResList);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


    @ResponseBody
    @GetMapping("/moreInfo/{categoryIdx}")
    public BaseResponse<List<GetProductMoreInfoRes>> getProductsMoreInfoByCategory(@PathVariable BigInteger categoryIdx) {
        if (categoryIdx == null) {
            return new BaseResponse<>(EMPTY_IDX);
        }

        if (!isValidatedIdx(categoryIdx)) {
            return new BaseResponse<>(INVALID_IDX);
        }

        try {
            List<GetProductMoreInfoRes> getProductMoreInfoResList
                    = productProvider.getProductsMoreInfoByCategory(categoryIdx);
            return new BaseResponse<>(getProductMoreInfoResList);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 특정 상품의 상품 사진들 보여준다.
     * @param idx
     * @return
     */
    @ResponseBody
    @GetMapping("/images/{idx}")
    public BaseResponse<List<GetProductImageRes>> getProductImages(@PathVariable BigInteger idx) {
        if (idx == null) {
            return new BaseResponse<>(EMPTY_IDX);
        }

        if (!isValidatedIdx(idx)) {
            return new BaseResponse<>(INVALID_IDX);
        }

        try {
            List<GetProductImageRes> getProductImageResList = productProvider.getProductImages(idx);

            return new BaseResponse<>(getProductImageResList);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/infoImages/{idx}")
    public BaseResponse<List<GetProductInfoImageRes>> getProductInfoImages(@PathVariable BigInteger idx) {
        if (idx == null) {
            return new BaseResponse<>(EMPTY_IDX);
        }

        if (!isValidatedIdx(idx)) {
            return new BaseResponse<>(INVALID_IDX);
        }

        try {
            List<GetProductInfoImageRes> getProductInfoImageResList = productProvider.getProductInfoImages(idx);

            return new BaseResponse<>(getProductInfoImageResList);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 카테고리 별 상품 리스트 조회 API
     * (페이지 번호에 따른 상품들 조회)
     * @param categoryIdx
     * @param offset
     * @return
     */
    @ResponseBody
    @GetMapping("/moreInfo/{categoryIdx}/{offset}")
    public BaseResponse<List<GetProductMoreInfoRes>> getProductsMoreInfoByCategoryAndOffset(@PathVariable BigInteger categoryIdx, @PathVariable Integer offset) {

        if (categoryIdx == null) {
            return new BaseResponse<>(EMPTY_IDX);
        }

        if (offset == null) {
            return new BaseResponse<>(EMPTY_OFFSET);
        }

        if (!isValidatedIdx(categoryIdx)) {
            return new BaseResponse<>(INVALID_IDX);
        }

        if (!isValidatedTenOffset(offset)) {
            return new BaseResponse<>(INVALID_OFFSET);
        }

        try {
            List<GetProductMoreInfoRes> getProductMoreInfoResList = productProvider.getProductsMoreInfoByCategoryAndOffset(categoryIdx, offset);
            return new BaseResponse<>(getProductMoreInfoResList);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostProductRes> createProduct(@RequestBody PostProductReq postProductReq) {

        if (postProductReq.getBrandIdx() == null) {
            return new BaseResponse<>(POST_PRODUCTS_EMPTY_BRANDIDX);
        }
        if (postProductReq.getCategoryIdx() == null) {
            return new BaseResponse<>(POST_PRODUCTS_EMPTY_CATEGORYIDX);
        }
        if (postProductReq.getName() == null) {
            return new BaseResponse<>(POST_PRODUCTS_EMPTY_NAME);
        }
        if (postProductReq.getPrice() == null) {
            return new BaseResponse<>(POST_PRODUCTS_EMPTY_PRICE);
        }
        if (postProductReq.getDeliveryType() == null) {
            return new BaseResponse<>(POST_PRODUCTS_EMPTY_DELIVERYTYPE);
        }
        if (postProductReq.getIsTodayDeal() == null) {
            return new BaseResponse<>(POST_PRODUCTS_EMPTY_ISTODAYDEAL);
        }

        if (!isValidatedDeliveryType(postProductReq.getDeliveryType())) {
            return new BaseResponse<>(POST_PRODUCTS_INVALID_DELIVERYTYPE);
        }
        if (!isValidatedIsTodayDeal(postProductReq.getIsTodayDeal())) {
            return new BaseResponse<>(POST_PRODUCTS_INVALID_ISTODAYDEAL);
        }

        try {
            PostProductRes postProductRes = productService.createProduct(postProductReq);
            return new BaseResponse<>(postProductRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


    @ResponseBody
    @PatchMapping("/delete/{idx}")
    public BaseResponse<PatchProductRes> deleteProduct(@PathVariable BigInteger idx) {
        if (idx == null) {
            return new BaseResponse<>(EMPTY_IDX);
        }

        if (!isValidatedIdx(idx)) {
            return new BaseResponse<>(INVALID_IDX);
        }

        try {
            PatchProductRes patchProductRes = productService.deleteProduct(idx);
            return new BaseResponse<>(patchProductRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/watch/moreInfo/{userIdx}/{offset}")
    public BaseResponse<List<GetProductMoreInfoRes>> getProductMoreInfoByWatching(@PathVariable BigInteger userIdx, @PathVariable Integer offset) {
        if (userIdx == null) {
            return new BaseResponse<>(EMPTY_IDX);
        }
        if (!isValidatedIdx(userIdx)) {
            return new BaseResponse<>(INVALID_IDX);
        }

        if (offset == null) {
            return new BaseResponse<>(EMPTY_OFFSET);
        }

        if (!isValidatedFiveOffset(offset)) {
            return new BaseResponse<>(INVALID_OFFSET);
        }

        try {
            List<GetProductMoreInfoRes> getProductMoreInfoResList = productProvider.getProductsMoreInfoByWatching(userIdx, offset);

            return new BaseResponse<>(getProductMoreInfoResList);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/totalInfo/detail/{idx}")
    public BaseResponse<GetProductTotalInfoRes> getProductTotalInfo(@PathVariable BigInteger idx) {
        if (idx == null) {
            return new BaseResponse<>(EMPTY_IDX);
        }
        if (!isValidatedIdx(idx)) {
            return new BaseResponse<>(INVALID_IDX);
        }

        try {
            GetProductTotalInfoRes getProductInfoTotalRes = productProvider.getProductTotalInfo(idx);
            return new BaseResponse<>(getProductInfoTotalRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/moreInfo/byUser/{userIdx}")
    public BaseResponse<List<GetProductMoreInfoByUserRes>> getProductsMoreInfoByUser(@PathVariable BigInteger userIdx) {
        if (userIdx == null) {
            return new BaseResponse<>(EMPTY_IDX);
        }
        if (!isValidatedIdx(userIdx)) {
            return new BaseResponse<>(INVALID_IDX);
        }

        try {
            List<GetProductMoreInfoByUserRes> getProductMoreInfoByUserResList = productProvider.getProductsMoreInfoByUser(userIdx);

            return new BaseResponse<>(getProductMoreInfoByUserResList);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
