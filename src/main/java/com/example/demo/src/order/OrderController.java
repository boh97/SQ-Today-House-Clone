package com.example.demo.src.order;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.order.model.*;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.Validation.*;

@RestController
@RequestMapping("app/orders")
public class OrderController {

    private final OrderProvider orderProvider;
    private final OrderService orderService;
    private final OrderDao orderDao;

    public OrderController(OrderProvider orderProvider, OrderService orderService, OrderDao orderDao) {
        this.orderProvider = orderProvider;
        this.orderService = orderService;
        this.orderDao = orderDao;
    }

    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetOrderRes>> getOrders() {
        try {
            List<GetOrderRes> getOrderResList = orderProvider.getOrders();
            return new BaseResponse<>(getOrderResList);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/{idx}")
    public BaseResponse<GetOrderRes> getOrder(@PathVariable BigInteger idx) {
        if (idx == null) {
            return new BaseResponse<>(EMPTY_IDX);
        }

        if(!isValidatedIdx(idx)) {
            return new BaseResponse<>(INVALID_IDX);
        }

        try {
            GetOrderRes getOrderRes = orderProvider.getOrder(idx);
            return new BaseResponse<>(getOrderRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

    }

    @ResponseBody
    @GetMapping("/fromUser/{userIdx}")
    public BaseResponse<List<GetOrderByUserRes>> getOrdersByUserIdx(@PathVariable BigInteger userIdx) {
        if (userIdx == null) {
            return new BaseResponse<>(EMPTY_IDX);
        }

        if(!isValidatedIdx(userIdx)) {
            return new BaseResponse<>(INVALID_IDX);
        }

        try {
            List<GetOrderByUserRes> getOrderByUserResList = orderProvider.getOrdersByUserIdx(userIdx);
            return new BaseResponse<>(getOrderByUserResList);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 주문 생성 API
     * "결제완료" 시, VIP 등급이라면, 포인트 혜택 3%!
     * Transactional 적용(orderDao)
     * @param postOrderReq
     * @return
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostOrderRes> createOrder(@RequestBody PostOrderReq postOrderReq) {
        if (postOrderReq.getUserIdx() == null) {
            return new BaseResponse<>(POST_ORDERS_EMPTY_USERIDX);
        }
        if (postOrderReq.getProductIdx() == null) {
            return new BaseResponse<>(POST_ORDERS_EMPTY_PRODUCTIDX);
        }
        if (postOrderReq.getStatus() == null) {
            return new BaseResponse<>(POST_ORDERS_EMPTY_STATUS);
        }

        if(!isValidatedStatusFromOrder(postOrderReq.getStatus())) {
            return new BaseResponse<>(POST_ORDERS_INVALID_STATUS);
        }

        try {
            PostOrderRes postOrderRes = orderService.createOrder(postOrderReq);
            return new BaseResponse<>(postOrderRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 주문 생성 API
     * "결제완료" 시, VIP 등급이라면, 포인트 혜택 3%!
     * Transactional 적용(orderDao)
     * @param postOrderReq
     * @return
     */
    @ResponseBody
    @PostMapping("/withPoint")
    public BaseResponse<PostOrderRes> createOrderWithPoint(@RequestBody PostOrderReq postOrderReq) {
        if (postOrderReq.getUserIdx() == null) {
            return new BaseResponse<>(POST_ORDERS_EMPTY_USERIDX);
        }
        if (postOrderReq.getProductIdx() == null) {
            return new BaseResponse<>(POST_ORDERS_EMPTY_PRODUCTIDX);
        }
        if (postOrderReq.getStatus() == null) {
            return new BaseResponse<>(POST_ORDERS_EMPTY_STATUS);
        }

        if(!isValidatedStatusFromOrder(postOrderReq.getStatus())) {
            return new BaseResponse<>(POST_ORDERS_INVALID_STATUS);
        }

        try {
            PostOrderRes postOrderRes = orderService.createOrderWithPoint(postOrderReq);
            return new BaseResponse<>(postOrderRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @PatchMapping("/cancel/{idx}")
    public BaseResponse<PatchOrderRes> cancelOrder(@PathVariable BigInteger idx) {
        if (idx == null) {
            return new BaseResponse<>(EMPTY_IDX);
        }
        if (!isValidatedIdx(idx)) {
            return new BaseResponse<>(INVALID_IDX);
        }

        try {
            PatchOrderRes patchOrderRes = orderService.cancelOrder(idx);
            return new BaseResponse<>(patchOrderRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
