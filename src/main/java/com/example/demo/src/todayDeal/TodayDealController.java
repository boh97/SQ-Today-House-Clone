package com.example.demo.src.todayDeal;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.todayDeal.model.*;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;
import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.Validation.*;

@RestController
@RequestMapping("/app/products/todayDeals")
public class TodayDealController {

    private final TodayDealProvider todayDealProvider;
    private final TodayDealService todayDealService;
    private final TodayDealDao todayDealDao;

    public TodayDealController(TodayDealProvider todayDealProvider, TodayDealService todayDealService, TodayDealDao todayDealDao) {
        this.todayDealProvider = todayDealProvider;
        this.todayDealService = todayDealService;
        this.todayDealDao = todayDealDao;
    }

    /**
     * 오늘의 딜 상품 전체 조회 API
     * @return
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetTodayDealProductRes>> getTodayDealProducts() {
        try {
            List<GetTodayDealProductRes> getTodayDealProductResList = todayDealProvider.getTodayDealProducts();

            return new BaseResponse<>(getTodayDealProductResList);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

//    /**
//     * 오늘의 딜 상품 조회 API (limit 10)
//     * 스토어의 "오늘의 딜" 탭
//     * @param offset
//     * @return
//     */
//    @ResponseBody
//    @GetMapping("/{offset}")
//    public BaseResponse<List<GetTodayDealProductRes>> getTodayDealProductsByOffset(@PathVariable Integer offset) {
//
//    }
//
    /**
     * 오늘의 딜 상품 조회 API (limit 4)
     * 스토어의 "스토어홈" 탭의 오늘의 딜 상품 조회
     * @param offset
     * @return
     */
    @ResponseBody
    @GetMapping("/store/{offset}")
    public BaseResponse<List<GetTodayDealProductMoreInfoRes>> getTodayDealProductsMoreInfoInStore(@PathVariable Integer offset) {
        if (offset == null) {
            return new BaseResponse<>(EMPTY_OFFSET);
        }
        if (!isValidatedFourOffset(offset)) {
            return new BaseResponse<>(INVALID_OFFSET);
        }

        try {
            List<GetTodayDealProductMoreInfoRes> getTodayDealProductMoreInfoResList = todayDealProvider.getTodayDealProductsMoreInfoInStore(offset);

            return new BaseResponse<>(getTodayDealProductMoreInfoResList);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostTodayDealRes> createTodayDeal(@RequestBody PostTodayDealReq postTodayDealReq) {
        if (postTodayDealReq.getProductIdx() == null) {
            return new BaseResponse<>(POST_TODAY_DEAL_EMPTY_PRODUCTIDX);
        }
        if (postTodayDealReq.getStatus() == null) {
            return new BaseResponse<>(POST_TODAY_DEAL_EMPTY_STATUS);
        }

        if (!isValidatedStatus(postTodayDealReq.getStatus())) {
            return new BaseResponse<>(POST_TODAY_DEAL_INVALID_STATUS);
        }

        try {
            PostTodayDealRes postTodayDealRes = todayDealService.createTodayDeal(postTodayDealReq);

            return new BaseResponse<>(postTodayDealRes);
        } catch(BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @PatchMapping("/delete/{idx}")
    public BaseResponse<PatchTodayDealRes> deleteTodayDeal(@PathVariable BigInteger idx) {
        if (idx == null) {
            return new BaseResponse<>(EMPTY_IDX);
        }
        if (!isValidatedIdx(idx)) {
            return new BaseResponse<>(INVALID_IDX);
        }

        try {
            PatchTodayDealRes patchTodayDealRes = todayDealService.deleteTodayDeal(idx);

            return new BaseResponse<>(patchTodayDealRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
