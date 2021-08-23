package com.example.demo.src.scrapProduct;

import com.example.demo.config.BaseException;
import com.example.demo.src.scrapProduct.model.GetScrapProductMoreInfoRes;
import com.example.demo.src.scrapProduct.model.GetScrapProductRes;
import com.example.demo.src.scrapProduct.model.PostScrapProductReq;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class ScrapProductProvider {
    private final ScrapProductDao scrapProductDao;

    public ScrapProductProvider(ScrapProductDao scrapProductDao) {
        this.scrapProductDao = scrapProductDao;
    }


    public List<GetScrapProductRes> getScrapsProduct() throws BaseException {
        if (scrapProductDao.isNullResponseScraps()) {
            throw new BaseException(RESPONSE_NULL_ERROR);
        }

        try {
            List<GetScrapProductRes> getScrapProductResList = scrapProductDao.getScrapsProduct();

            return getScrapProductResList;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetScrapProductRes> getScrapsProductByUserIdx(BigInteger userIdx) throws BaseException {
        if (scrapProductDao.isNullResponseScrapsByUserIdx(userIdx)) {
            throw new BaseException(RESPONSE_NULL_ERROR_BY_IDX);
        }

        try {
            List<GetScrapProductRes> getScrapProductResList = scrapProductDao.getScrapsProduct(userIdx);

            return getScrapProductResList;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetScrapProductMoreInfoRes> getScrapsProductMoreInfoByUserIdx(BigInteger userIdx) throws BaseException {
        if (scrapProductDao.isNullResponseScrapsByUserIdx(userIdx)) {
            throw new BaseException(RESPONSE_NULL_ERROR_BY_IDX);
        }

        try {
            List<GetScrapProductMoreInfoRes> getScrapProductMoreInfoResList
                    = scrapProductDao.getScrapsProductMoreInfoByUserIdx(userIdx);

            return getScrapProductMoreInfoResList;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public boolean isInScrapProduct(PostScrapProductReq postScrapProductReq) throws BaseException {
        try {
            if (scrapProductDao.isInScrapProduct(postScrapProductReq) == 1) {
                return true;
            }
            return false;

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public boolean isPreCanceledScrap(BigInteger idx) throws BaseException {
        try {
            if (scrapProductDao.isPreDeletedScrap(idx)) {
                return true;
            }
            return false;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public boolean isNullResponseScraps(BigInteger idx) {
        return scrapProductDao.isNullResponseScrapsByIdx(idx);
    }
}
