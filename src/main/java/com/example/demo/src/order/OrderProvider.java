package com.example.demo.src.order;

import com.example.demo.config.BaseException;
import com.example.demo.src.order.model.GetOrderByUserRes;
import com.example.demo.src.order.model.GetOrderRes;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class OrderProvider {

    private final OrderDao orderDao;

    public OrderProvider(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public List<GetOrderRes> getOrders() throws BaseException {
        if (orderDao.isNullResponseOrder()) {
            throw new BaseException(RESPONSE_NULL_ERROR);
        }

        try {
            List<GetOrderRes> getOrderResList = orderDao.getOrders();

            return getOrderResList;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetOrderRes getOrder(BigInteger idx) throws BaseException {
        if (orderDao.isNullResponseOrderByIdx(idx)) {
            throw new BaseException(RESPONSE_NULL_ERROR_BY_IDX);
        }

        try {
            GetOrderRes getOrderRes = orderDao.getOrder(idx);

            return getOrderRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public List<GetOrderByUserRes> getOrdersByUserIdx(BigInteger userIdx) throws BaseException {
        if (orderDao.isNullResponseOrderByUserIdx(userIdx)) {
            throw new BaseException(RESPONSE_NULL_ERROR_BY_IDX);
        }

        try {
            List<GetOrderByUserRes> getOrderByUserResList = orderDao.getOrderByUserIdx(userIdx);

            return getOrderByUserResList;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
