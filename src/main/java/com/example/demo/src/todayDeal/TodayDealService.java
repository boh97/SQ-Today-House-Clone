package com.example.demo.src.todayDeal;

import com.example.demo.config.BaseException;
import com.example.demo.src.product.ProductDao;
import com.example.demo.src.todayDeal.model.PatchTodayDealRes;
import com.example.demo.src.todayDeal.model.PostTodayDealReq;
import com.example.demo.src.todayDeal.model.PostTodayDealRes;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigInteger;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class TodayDealService {

    private final TodayDealProvider todayDealProvider;
    private final TodayDealDao todayDealDao;

    public TodayDealService(TodayDealProvider todayDealProvider, TodayDealDao todayDealDao) {
        this.todayDealProvider = todayDealProvider;
        this.todayDealDao = todayDealDao;
    }

    @Transactional
    public PostTodayDealRes createTodayDeal(PostTodayDealReq postTodayDealReq) throws BaseException {
        try {
            PostTodayDealRes postTodayDealRes = todayDealDao.createTodayDeal(postTodayDealReq);

            return postTodayDealRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public PatchTodayDealRes deleteTodayDeal(BigInteger idx) throws BaseException {
        if (todayDealProvider.isNullResponseTodayDealByIdx(idx)) {
            throw new BaseException(RESPONSE_NULL_ERROR_BY_IDX);
        }

        if (todayDealProvider.isPreDeletedTodayDeal(idx)) {
            throw new BaseException(PATCH_TODAY_DEAL_PRE_DELETED);
        }

        try {
            return todayDealDao.deleteTodayDeal(idx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
