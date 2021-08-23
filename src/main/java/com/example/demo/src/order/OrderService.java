package com.example.demo.src.order;

import com.example.demo.config.BaseException;
import com.example.demo.src.order.model.PatchOrderRes;
import com.example.demo.src.order.model.PostOrderReq;
import com.example.demo.src.order.model.PostOrderRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigInteger;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class OrderService {

    private final OrderProvider orderProvider;
    private final OrderDao orderDao;

    @Autowired
    public OrderService(OrderProvider orderProvider, OrderDao orderDao) {
        this.orderProvider = orderProvider;
        this.orderDao = orderDao;
    }

    @Transactional
    public PostOrderRes createOrder(PostOrderReq postOrderReq) throws BaseException {
        try {
            PostOrderRes postOrderRes = orderDao.createOrder(postOrderReq);

            return postOrderRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PatchOrderRes cancelOrder(BigInteger idx) throws BaseException {
        if (orderDao.isNullResponseOrderByIdx(idx)) {
            throw new BaseException(RESPONSE_NULL_ERROR_BY_IDX);
        }

        if(orderDao.cancelOrder(idx) == 0) {
            throw new BaseException(PATCH_ORDERS_MODIFY_FAIL_CANCEL);
        }

        try {
            return orderDao.getPatchOrder(idx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public PostOrderRes createOrderWithPoint(PostOrderReq postOrderReq) throws BaseException {
        try {
            PostOrderRes postOrderRes = orderDao.createOrderWithPoint(postOrderReq);

            return postOrderRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
