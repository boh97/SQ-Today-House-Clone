package com.example.demo.src.basketProduct;

import com.example.demo.src.basketProduct.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

import java.math.BigInteger;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Repository
public class BasketDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public boolean isNullResponseBasketProducts() {
        String checkNullQuery = "select count(case when status = 1 then 1 end) as rowN from ShoppingBasket";
        Integer countOfBaskets = this.jdbcTemplate.queryForObject(checkNullQuery, Integer.class);

        return countOfBaskets.equals(0);
    }

    public List<GetBasketRes> getBasketProducts() {
        String getBasketProductsQuery = "select * from ShoppingBasket where status = 1";

        return this.jdbcTemplate.query(getBasketProductsQuery,
                (rs, rowNum) -> new GetBasketRes(
                        rs.getObject("idx", BigInteger.class),
                        rs.getObject("userIdx", BigInteger.class),
                        rs.getObject("productIdx", BigInteger.class),
                        rs.getTimestamp("createdAt"),
                        rs.getTimestamp("updatedAt"),
                        rs.getObject("status", Integer.class)
                ));
    }

    public boolean isNullResponseBasketProductsByUser(BigInteger userIdx) {
        String checkNullQuery = "select count(case when userIdx = ? and status = 1 then 1 end) as rowN from ShoppingBasket";
        Integer countOfBasket = this.jdbcTemplate.queryForObject(checkNullQuery, Integer.class, userIdx);

        return countOfBasket.equals(0);
    }

    public GetBasketMoreInfoRes getBasketProductsByUser(BigInteger userIdx) {
        String getBasketProductsQuery = "select 상품IDX, 상품이름, 상품가격, 배송유형, ProductImage.imageUrl as 상품이미지URL from ProductImage\n" +
                "right join (select ShoppingBasket.userIdx as 유저IDX, Product.idx as 상품IDX, Product.name as 상품이름, Product.price as 상품가격, Product.deliveryType as 배송유형 from ShoppingBasket\n" +
                "left join Product on ShoppingBasket.productIdx = Product.idx\n" +
                "where ShoppingBasket.status = 1) a on a.상품IDX = ProductImage.productIdx\n" +
                "where a.유저IDX = ?\n" +
                "group by a.상품IDX";
        List<ProductBasketInfo> productBasketInfoList = this.jdbcTemplate.query(getBasketProductsQuery,
                (rs, rowNum) -> new ProductBasketInfo(
                        rs.getObject("상품IDX", BigInteger.class),
                        rs.getString("상품이름"),
                        rs.getObject("상품가격", Integer.class),
                        rs.getString("배송유형"),
                        rs.getString("상품이미지URL")
                ), userIdx);

        String getCountOfBasketQuery = "select count(case when userIdx = ? and status = 1 then 1 end) as rowN from ShoppingBasket";
        Integer countOfBasket = this.jdbcTemplate.queryForObject(getCountOfBasketQuery, Integer.class, userIdx);

        int totalPrice = 0;
        for (int i = 0; i < productBasketInfoList.size(); i++) {
            totalPrice += productBasketInfoList.get(i).getPrice();
        }

        return new GetBasketMoreInfoRes(productBasketInfoList, countOfBasket, totalPrice);
    }

    public PostBasketRes createBasket(PostBasketReq postBasketReq) {
        String insertBasketQuery = "insert into ShoppingBasket (userIdx, productIdx, status) VALUES (?, ?, ?)";
        Object[] insertBasketParams = new Object[] { postBasketReq.getUserIdx(), postBasketReq.getProductIdx(), 1 };

        this.jdbcTemplate.update(insertBasketQuery, insertBasketParams);


        String getLastInsertIdxQuery = "select last_insert_id()";
        BigInteger lastInsertIdx = this.jdbcTemplate.queryForObject(getLastInsertIdxQuery, BigInteger.class);

        return new PostBasketRes(lastInsertIdx, 1);
    }

    public boolean isDuplicatedBasket(PostBasketReq postBasketReq) {
        String checkDuplicatedBasketQuery = "select count(case when userIdx = ? and productIdx = ? and status = 1 then 1 end) as rowN from ShoppingBasket";
        Object[] checkDuplicatedBasketParams = new Object[] { postBasketReq.getUserIdx(), postBasketReq.getProductIdx() };

        Integer countOfDuplicated = this.jdbcTemplate.queryForObject(checkDuplicatedBasketQuery, Integer.class, checkDuplicatedBasketParams);

        return !countOfDuplicated.equals(0);
    }

    public boolean isPreCancelledBasket(BigInteger idx) {
        String checkPreCancelledBasketQuery = "select count(case when idx = ? and status = 0 then 1 end) as rowN from ShoppingBasket";
        Integer countOfCancelled = this.jdbcTemplate.queryForObject(checkPreCancelledBasketQuery, Integer.class, idx);

        return !countOfCancelled.equals(0);
    }

    public PatchBasketRes cancelBasket(BigInteger idx) {
        String deleteQuery = "update ShoppingBasket set status = 0 where idx = ?";
        this.jdbcTemplate.update(deleteQuery, idx);

        return new PatchBasketRes(idx, 0);
    }
}
