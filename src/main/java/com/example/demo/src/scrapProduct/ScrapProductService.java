package com.example.demo.src.scrapProduct;

import com.example.demo.config.BaseException;
import com.example.demo.src.scrapProduct.model.PatchScrapProductRes;
import com.example.demo.src.scrapProduct.model.PostScrapProductReq;
import com.example.demo.src.scrapProduct.model.PostScrapProductRes;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class ScrapProductService {
    private final ScrapProductProvider scrapProductProvider;
    private final ScrapProductDao scrapProductDao;

    public ScrapProductService(ScrapProductProvider scrapProductProvider, ScrapProductDao scrapProductDao) {
        this.scrapProductProvider = scrapProductProvider;
        this.scrapProductDao = scrapProductDao;
    }

    public PostScrapProductRes createScrapProduct(PostScrapProductReq postScrapProductReq) throws BaseException {
        //중복 체크
        if (scrapProductProvider.isInScrapProduct(postScrapProductReq)) {
            throw new BaseException(POST_SCRAPS_PRODUCT_DUPLICATED_SCRAP);
        }

        try {
            PostScrapProductRes postScrapProductRes = scrapProductDao.createScrapProduct(postScrapProductReq);

            return postScrapProductRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PatchScrapProductRes cancelScrap(BigInteger idx) throws BaseException {
        if (scrapProductProvider.isNullResponseScraps(idx)) {
            throw new BaseException(RESPONSE_NULL_ERROR_BY_IDX);
        }

        if (scrapProductProvider.isPreCanceledScrap(idx)) {
            throw new BaseException(PATCH_PRODUCT_SCRAP_PRE_CANCELED);
        }

        try {
            PatchScrapProductRes patchScrapProductRes = scrapProductDao.cancelScrap(idx);
            return patchScrapProductRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
