package com.example.demo.src.todayDeal;

import com.example.demo.config.BaseException;
import com.example.demo.src.todayDeal.model.GetTodayDealProductMoreInfoRes;
import com.example.demo.src.todayDeal.model.GetTodayDealProductRes;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class TodayDealProvider {

    private final TodayDealDao todayDealDao;

    public TodayDealProvider(TodayDealDao todayDealDao) {
        this.todayDealDao = todayDealDao;
    }


    public List<GetTodayDealProductRes> getTodayDealProducts() throws BaseException {
        if (todayDealDao.isNullResponseTodayDeals()) {
            throw new BaseException(RESPONSE_NULL_ERROR);
        }

        try {
            List<GetTodayDealProductRes> getTodayDealProductResList = todayDealDao.getTodayDealProducts();
            return getTodayDealProductResList;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetTodayDealProductMoreInfoRes> getTodayDealProductsMoreInfoInStore(Integer offset) throws BaseException {
        if (todayDealDao.isNullResponseTodayDealsByOffset(offset)) {
            throw new BaseException(RESPONSE_NULL_ERROR);
        }

        try {
            List<GetTodayDealProductMoreInfoRes> getTodayDealProductMoreInfoResList = todayDealDao.getTodayDealProductsMoreInfoInStore(offset);

            return getTodayDealProductMoreInfoResList;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public boolean isPreDeletedTodayDeal(BigInteger idx) throws BaseException {
        try {
            return todayDealDao.isPreDeletedTodayDeal(idx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public boolean isNullResponseTodayDealByIdx(BigInteger idx) {
        return todayDealDao.isNullResponseTodayDealByIdx(idx);
    }
}
