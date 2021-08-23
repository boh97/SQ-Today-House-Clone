package com.example.demo.src.order;

import com.example.demo.src.order.model.*;
import com.example.demo.src.product.ProductDao;
import com.example.demo.src.user.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;

@Repository
public class OrderDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public List<GetOrderRes> getOrders() {
        String getOrdersQuery = "select * from Purchase where status != '주문취소'";

        return this.jdbcTemplate.query(getOrdersQuery,
                (rs, rowNum) -> new GetOrderRes(
                        rs.getObject("idx", BigInteger.class),
                        rs.getObject("userIdx", BigInteger.class),
                        rs.getObject("productIdx", BigInteger.class),
                        rs.getTimestamp("createdAt"),
                        rs.getTimestamp("updatedAt"),
                        rs.getString("status")));
    }

    public GetOrderRes getOrder(BigInteger idx) {
        String getOrderQuery = "select * from Purchase where status != '주문취소' and idx = ?";

        return this.jdbcTemplate.queryForObject(getOrderQuery,
                (rs, rowNum) -> new GetOrderRes(
                        rs.getObject("idx", BigInteger.class),
                        rs.getObject("userIdx", BigInteger.class),
                        rs.getObject("productIdx", BigInteger.class),
                        rs.getTimestamp("createdAt"),
                        rs.getTimestamp("updatedAt"),
                        rs.getString("status")),
                idx);
    }

    public List<GetOrderByUserRes> getOrderByUserIdx(BigInteger userIdx) {
        String getOrdersByUserIdxQuery = "select Purchase.userIdx as 유저IDX,\n" +
                "       count(case when Purchase.status = '입금대기' then 1 end) as 입금대기_수,\n" +
                "       count(case when Purchase.status = '결제완료' then 1 end) as 결제완료_수,\n" +
                "       count(case when Purchase.status = '배송준비' then 1 end) as 배송준비_수,\n" +
                "       count(case when Purchase.status = '배송중' then 1 end) as 배송중_수,\n" +
                "       count(case when Purchase.status = '배송완료' then 1 end) as 배송완료_수,\n" +
                "       count(case when Purchase.status = '리뷰쓰기' then 1 end) as 리뷰쓰기_수 from Purchase\n" +
                "where Purchase.userIdx = ?\n" +
                "group by Purchase.userIdx";

        return this.jdbcTemplate.query(getOrdersByUserIdxQuery,
                (rs, rowNum) -> new GetOrderByUserRes(
                        rs.getObject("유저IDX", BigInteger.class),
                        rs.getObject("입금대기_수", Integer.class),
                        rs.getObject("결제완료_수", Integer.class),
                        rs.getObject("배송준비_수", Integer.class),
                        rs.getObject("배송중_수", Integer.class),
                        rs.getObject("배송완료_수", Integer.class),
                        rs.getObject("리뷰쓰기_수", Integer.class)
                ), userIdx);
    }


    public PostOrderRes createOrder(PostOrderReq postOrderReq) {
        String createOrderQuery = "insert into Purchase (userIdx, productIdx, status) VALUES (?, ?, ?)";

        Object[] createOrderParams = new Object[] { postOrderReq.getUserIdx(), postOrderReq.getProductIdx(), postOrderReq.getStatus() };

        this.jdbcTemplate.update(createOrderQuery, createOrderParams);

        String lastInsertIdQuery = "select last_insert_id()";
        BigInteger idx = this.jdbcTemplate.queryForObject(lastInsertIdQuery, BigInteger.class);

        //결제완료 시, 상품가격에 0.03 만큼의 point 더한다.
        if (postOrderReq.getStatus().equals("결제완료")) {
            //user 의 grade 값 가져온다.
            String userGrade = userDao.getUserGrade(postOrderReq);

            plusUserPointByGrade(userGrade, postOrderReq);
        }


        String getPostOrderResQuery = "select * from Purchase where idx = ?";
        return this.jdbcTemplate.queryForObject(getPostOrderResQuery,
                (rs, rowNum) -> new PostOrderRes(
                        rs.getObject("idx", BigInteger.class),
                        rs.getObject("userIdx", BigInteger.class),
                        rs.getObject("productIdx", BigInteger.class),
                        rs.getTimestamp("createdAt"),
                        rs.getTimestamp("updatedAt"),
                        rs.getString("status"))
                , idx);
    }

//    public PostOrderRes createOrder(PostOrderReq postOrderReq) {
//        String createOrderQuery = "insert into Purchase (userIdx, productIdx, status) VALUES (?, ?, ?)";
//
//        Object[] createOrderParams = new Object[] { postOrderReq.getUserIdx(), postOrderReq.getProductIdx(), postOrderReq.getStatus() };
//
//        this.jdbcTemplate.update(createOrderQuery, createOrderParams);
//
//        String lastInsertIdQuery = "select last_insert_id()";
//        BigInteger idx = this.jdbcTemplate.queryForObject(lastInsertIdQuery, BigInteger.class);
//
//        //결제완료 시, user의 포인트에 0.1 만큼의 point 더한다.
//        if (postOrderReq.getStatus().equals("결제완료")) {
//            //user 의 grade 값 가져온다.
//            String getUserGradeQuery = "select grade from User where idx = ? and status = 1";
//            String userGrade = this.jdbcTemplate.queryForObject(getUserGradeQuery, String.class, postOrderReq.getUserIdx());
//
//            setUserPointByGrade(userGrade, postOrderReq);
//        }
//
//
//        String getPostOrderResQuery = "select * from Purchase where idx = ?";
//        return this.jdbcTemplate.queryForObject(getPostOrderResQuery,
//                (rs, rowNum) -> new PostOrderRes(
//                        rs.getObject("idx", BigInteger.class),
//                        rs.getObject("userIdx", BigInteger.class),
//                        rs.getObject("productIdx", BigInteger.class),
//                        rs.getTimestamp("createdAt"),
//                        rs.getTimestamp("updatedAt"),
//                        rs.getString("status"))
//                , idx);
//    }


    /**
     * VIP 의 경우 포인트 3% 적립 혜택
     * @param grade
     * @param postOrderReq
     */
    public void plusUserPointByGrade(String grade, PostOrderReq postOrderReq) {
        if (grade.equals("VIP")) {
            //user 의 point 가져온다.
            Integer getPoint = userDao.getUserPoint(postOrderReq);
            Integer productPrice = productDao.getProductPrice(postOrderReq);

            Integer totalUserPoint = getPoint + ((productPrice * 3) / 100);

            userDao.setUserPoint(totalUserPoint, postOrderReq);
        }
    }

//    public void setUserPointByGrade(String grade, PostOrderReq postOrderReq) {
//        if (grade.equals("VIP")) {
//            //user 의 point 가져온다.
//            String getPointByUserQuery = "select point from User where idx = ? and status = 1";
//            Integer getPoint = this.jdbcTemplate.queryForObject(getPointByUserQuery, Integer.class, postOrderReq.getUserIdx());
//
//            //더할 point의 값 구한다.
//            String getProductPriceQuery = "select price from Product where idx = ? and status = 1";
//            Integer price = this.jdbcTemplate.queryForObject(getProductPriceQuery, Integer.class, postOrderReq.getProductIdx());
//
//            Integer totalUserPoint = getPoint + (price / 10);
//
//            String plusUserPointQuery = "update User set point = ? where idx = ? and status = 1";
//            Object[] plusUserPointParams = new Object[]{totalUserPoint, postOrderReq.getUserIdx()};
//            this.jdbcTemplate.update(plusUserPointQuery, plusUserPointParams);
//        } else { // WELCOME
//            //user 의 point 가져온다.
//            String getPointByUserQuery = "select point from User where idx = ? and status = 1";
//            Integer getPoint = this.jdbcTemplate.queryForObject(getPointByUserQuery, Integer.class, postOrderReq.getUserIdx());
//
//            //더할 point의 값 구한다.
//            String getProductPriceQuery = "select price from Product where idx = ? and status = 1";
//            Integer price = this.jdbcTemplate.queryForObject(getProductPriceQuery, Integer.class, postOrderReq.getProductIdx());
//
//            Integer totalUserPoint = getPoint + (price / 100);
//
//            String plusUserPointQuery = "update User set point = ? where idx = ? and status = 1";
//            Object[] plusUserPointParams = new Object[]{totalUserPoint, postOrderReq.getUserIdx()};
//            this.jdbcTemplate.update(plusUserPointQuery, plusUserPointParams);
//        }
//    }

    public int cancelOrder(BigInteger idx) {
        String cancelOrderQuery = "update Purchase set status = ? where idx = ?";
        Object[] cancelOrderParams = new Object[] { "주문취소", idx };

        return this.jdbcTemplate.update(cancelOrderQuery, cancelOrderParams);
    }

    public PatchOrderRes getPatchOrder(BigInteger idx) {
        String getPatchOrderQuery = "select idx, status from Purchase where idx = ?";

        return this.jdbcTemplate.queryForObject(getPatchOrderQuery,
                (rs, rowNum) -> new PatchOrderRes(
                        rs.getObject("idx", BigInteger.class),
                        rs.getString("status")
                ), idx);
    }


    public PostOrderRes createOrderWithPoint(PostOrderReq postOrderReq) {

        String createOrderWithPointQuery = "insert into Purchase (userIdx, productIdx, status) VALUES (?, ?, ?)";
        Object[] createOrderWithPointParams = new Object[] { postOrderReq.getUserIdx(), postOrderReq.getProductIdx(), postOrderReq.getStatus() };

        this.jdbcTemplate.update(createOrderWithPointQuery, createOrderWithPointParams);

        String lastInsertIdQuery = "select last_insert_id()";
        BigInteger idx = this.jdbcTemplate.queryForObject(lastInsertIdQuery, BigInteger.class);

        //결제완료 시, user의 포인트에 0.1 만큼의 point 더한다.
        if (postOrderReq.getStatus().equals("결제완료")) {
            minusUserPoint(postOrderReq);
        }

        String getPostOrderResQuery = "select * from Purchase where idx = ?";
        return this.jdbcTemplate.queryForObject(getPostOrderResQuery,
                (rs, rowNum) -> new PostOrderRes(
                        rs.getObject("idx", BigInteger.class),
                        rs.getObject("userIdx", BigInteger.class),
                        rs.getObject("productIdx", BigInteger.class),
                        rs.getTimestamp("createdAt"),
                        rs.getTimestamp("updatedAt"),
                        rs.getString("status"))
                , idx);
    }

    public void minusUserPoint(PostOrderReq postOrderReq) {
        Integer getPoint = userDao.getUserPoint(postOrderReq);
        Integer productPrice = productDao.getProductPrice(postOrderReq);

        if (getPoint >= productPrice) {
            userDao.setUserPoint(getPoint - productPrice, postOrderReq);
        } else {
            userDao.setUserPoint(0, postOrderReq);
        }
    }

    public boolean isNullResponseOrderByIdx(BigInteger idx) {
        String checkNullQuery = "select count(case when idx = ? and status != '주문취소' then 1 end) as rowN from Purchase";
        Integer countOfOrderByIdx = this.jdbcTemplate.queryForObject(checkNullQuery, Integer.class, idx);

        return (countOfOrderByIdx.equals(0));
    }

    public boolean isNullResponseOrder() {
        String checkNullQuery = "select count(case when status != '주문취소' then 1 end) as rowN from Purchase";
        Integer countOfOrder = this.jdbcTemplate.queryForObject(checkNullQuery, Integer.class);

        return countOfOrder.equals(0);
    }

    public boolean isNullResponseOrderByUserIdx(BigInteger userIdx) {
        String checkNullQuery = "select count(case when userIdx = ? and status != '주문취소' then 1 end) as rowN from Purchase";
        Integer countOfOrderByUserIdx = this.jdbcTemplate.queryForObject(checkNullQuery, Integer.class, userIdx);

        return countOfOrderByUserIdx.equals(0);
    }
}
