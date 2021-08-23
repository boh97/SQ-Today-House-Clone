package com.example.demo.src.todayDeal;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.product.ProductDao;
import com.example.demo.src.product.model.GetProductMoreInfoRes;
import com.example.demo.src.todayDeal.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigInteger;
import java.util.List;
import static com.example.demo.config.BaseResponseStatus.*;

@Repository
public class TodayDealDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ProductDao productDao;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public List<GetTodayDealProductRes> getTodayDealProducts() {
        String getTodayDealsQuery = "select * from TodayDeal where status = 1";

        return this.jdbcTemplate.query(getTodayDealsQuery
                , (rs, rowNum) -> new GetTodayDealProductRes(
                        rs.getObject("idx", BigInteger.class),
                        rs.getObject("productIdx", BigInteger.class),
                        rs.getTimestamp("createdAt"),
                        rs.getTimestamp("updatedAt"),
                        rs.getObject("status", Integer.class)));
    }

    public List<GetTodayDealProductMoreInfoRes> getTodayDealProductsMoreInfoInStore(Integer offset) {
        String getTodayDealProductsMoreInfoQuery = "select 상품이름, 리뷰수, 상품가격, 별점, 브랜드이름, 오늘의딜여부, 배송유형, 상품사진 from (select 상품이름, count(case when ReviewProduct.productIdx = 상품Idx and ReviewProduct.status = 1\n" +
                "    then 1\n" +
                "    end) as 리뷰수, 상품가격, 별점, 브랜드이름, 오늘의딜여부, 배송유형, 상품사진, 상품등록시간 from ReviewProduct\n" +
                "right join (select 상품Idx, 별점, 배송유형, 브랜드Idx, 카테고리Idx, 상품이름, 상품가격, 오늘의딜여부, 상품등록시간, 상품사진, Brand.name as 브랜드이름 from Brand\n" +
                "right join (select Product.idx as 상품Idx, Product.name as 상품이름, Product.categoryIdx as 카테고리Idx, Product.price as 상품가격, Product.isTodayDeal as 오늘의딜여부\n" +
                "                 , Product.brandIdx as 브랜드Idx, PI.imageUrl as 상품사진, Product.createdAt as 상품등록시간, Product.totalStar as 별점, Product.deliveryType as 배송유형 from Product\n" +
                "left join ProductImage PI on Product.idx = PI.productIdx\n" +
                "group by Product.idx) a on a.브랜드Idx = Brand.idx) b on b.상품Idx = ReviewProduct.productIdx\n" +
                "group by b.상품Idx) c\n" +
                "where c.오늘의딜여부 = 'Y'\n" +
                "order by c.리뷰수 DESC\n" +
                "limit 4\n" +
                "offset ?";

        return this.jdbcTemplate.query(getTodayDealProductsMoreInfoQuery,
                (rs, rowNum) -> new GetTodayDealProductMoreInfoRes(
                        rs.getString("상품이름"),
                        rs.getObject("리뷰수", Integer.class),
                        rs.getObject("상품가격", Integer.class),
                        rs.getObject("별점", Float.class),
                        rs.getString("브랜드이름"),
                        rs.getString("오늘의딜여부"),
                        rs.getString("배송유형"),
                        rs.getString("상품사진")), offset);
    }

    public PostTodayDealRes createTodayDeal(PostTodayDealReq postTodayDealReq) throws BaseException {
        String createTodayDealQuery = "insert into TodayDeal (productIdx, status) VALUES (?, ?)";
        Object[] createTodayDealParams = new Object[] { postTodayDealReq.getProductIdx(), postTodayDealReq.getStatus() };


        try {
            productDao.setProductTodayDeal(postTodayDealReq.getProductIdx(), "Y");
        } catch (Exception exception) {
            throw new BaseException(MODIFY_FAIL_PRODUCT_TODAYDEAL);
        }

        this.jdbcTemplate.update(createTodayDealQuery, createTodayDealParams);

        String lastInsertIdQuery = "select last_insert_id()";
        BigInteger idx = this.jdbcTemplate.queryForObject(lastInsertIdQuery, BigInteger.class);

        return new PostTodayDealRes(idx, postTodayDealReq.getStatus() );
    }

    public PatchTodayDealRes deleteTodayDeal(BigInteger idx) throws BaseException {
        String getProductIdxQuery = "select productIdx from TodayDeal where idx = ?";
        BigInteger productIdx = this.jdbcTemplate.queryForObject(getProductIdxQuery, BigInteger.class, idx);

        try {
            productDao.setProductTodayDeal(productIdx, "N");
        } catch (Exception exception) {
            throw new BaseException(MODIFY_FAIL_PRODUCT_TODAYDEAL);
        }

        String deleteTodayDealQuery = "update TodayDeal set status = 0 where idx = ?";
        this.jdbcTemplate.update(deleteTodayDealQuery, idx);

        return new PatchTodayDealRes(idx, 0);
    }

    public boolean isPreDeletedTodayDeal(BigInteger idx) {
        String getStatusTodayDealQuery = "select status from TodayDeal where idx = ?";
        Integer status = this.jdbcTemplate.queryForObject(getStatusTodayDealQuery, Integer.class, idx);

        return (status == 0);
    }

    public boolean isNullResponseTodayDeals() {
        String checkNullQuery = "select count(case when status = 1 then 1 end) as rowN from TodayDeal";
        Integer countOfTodayDeals = this.jdbcTemplate.queryForObject(checkNullQuery, Integer.class);

        return (countOfTodayDeals.equals(0));
    }

    public boolean isNullResponseTodayDealsByOffset(Integer offset) {
        String checkNullQuery = "select count(case when status = 1 then 1 end) as rowN from TodayDeal";
        Integer countOfTodayDealsByOffset = this.jdbcTemplate.queryForObject(checkNullQuery, Integer.class);

        return (countOfTodayDealsByOffset <= offset);
    }

    public boolean isNullResponseTodayDealByIdx(BigInteger idx) {
        String checkNullQuery = "select count(case when idx = ? and status = 1 then 1 end) as rowN from TodayDeal";
        Integer countOfTodayDeal = this.jdbcTemplate.queryForObject(checkNullQuery, Integer.class, idx);

        return (countOfTodayDeal.equals(0));
    }
}
