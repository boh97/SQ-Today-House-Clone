package com.example.demo.src.reviewProduct;

import com.example.demo.config.BaseException;
import com.example.demo.src.product.ProductDao;
import com.example.demo.src.reviewProduct.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigInteger;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.MODIFY_FAIL_PRODUCT_TOTALSTAR;

@Repository
public class ReviewProductDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ProductDao productDao;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public List<GetReviewProductRes> getReviews() {
        String getReviewsProductQuery = "select * from ReviewProduct where status = 1";

        return this.jdbcTemplate.query(getReviewsProductQuery,
                (rs, rowNum) -> new GetReviewProductRes(
                        rs.getObject("idx", BigInteger.class),
                        rs.getObject("userIdx", BigInteger.class),
                        rs.getObject("productIdx", BigInteger.class),
                        rs.getString("reviewText"),
                        rs.getString("imageUrl"),
                        rs.getString("isTodayHouse"),
                        rs.getObject("durabilityStars", Integer.class),
                        rs.getObject("priceStars", Integer.class),
                        rs.getObject("designStars", Integer.class),
                        rs.getObject("deliveryStars", Integer.class),
                        rs.getTimestamp("createdAt"),
                        rs.getTimestamp("updatedAt"),
                        rs.getObject("status", Integer.class)));
    }

    public List<GetReviewProductRes> getReviewsByUserIdx(BigInteger userIdx) {
        String getReviewsByUserIdxQuery = "select * from ReviewProduct where status = 1 and userIdx = ?";

        return this.jdbcTemplate.query(getReviewsByUserIdxQuery,
                (rs, rowNum) -> new GetReviewProductRes(
                        rs.getObject("idx", BigInteger.class),
                        rs.getObject("userIdx", BigInteger.class),
                        rs.getObject("productIdx", BigInteger.class),
                        rs.getString("reviewText"),
                        rs.getString("imageUrl"),
                        rs.getString("isTodayHouse"),
                        rs.getObject("durabilityStars", Integer.class),
                        rs.getObject("priceStars", Integer.class),
                        rs.getObject("designStars", Integer.class),
                        rs.getObject("deliveryStars", Integer.class),
                        rs.getTimestamp("createdAt"),
                        rs.getTimestamp("updatedAt"),
                        rs.getObject("status", Integer.class))
                , userIdx);
    }



    public PostReviewProductRes createReview(PostReviewProductReq postReviewProductReq) throws BaseException {
        String createReviewProductQuery = "insert into ReviewProduct (userIdx, productIdx, reviewText, imageUrl, isTodayHouse, durabilityStars, priceStars, designStars, deliveryStars, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 1)";

        Object[] createReviewProductParams = new Object[] {
                postReviewProductReq.getUserIdx(), postReviewProductReq.getProductIdx(), postReviewProductReq.getReviewText()
                , postReviewProductReq.getImageUrl(), postReviewProductReq.getIsTodayHouse(), postReviewProductReq.getDurabilityStars()
                , postReviewProductReq.getPriceStars(), postReviewProductReq.getDesignStars(), postReviewProductReq.getDeliveryStars() };

        try {
            productDao.setProductTotalStars(postReviewProductReq);
        } catch (Exception exception) {
            throw new BaseException(MODIFY_FAIL_PRODUCT_TOTALSTAR);
        }

        this.jdbcTemplate.update(createReviewProductQuery, createReviewProductParams);

        String lastInsertIdQuery = "select last_insert_id()";
        BigInteger lastInsertIdx = this.jdbcTemplate.queryForObject(lastInsertIdQuery, BigInteger.class);



        return new PostReviewProductRes(lastInsertIdx, 1);
    }

    public boolean isPreCanceledReview(BigInteger idx) {
        String checkIsPreCanceledReviewQuery = "select status from ReviewProduct where idx = ?";

        Integer status = this.jdbcTemplate.queryForObject(checkIsPreCanceledReviewQuery
                , Integer.class
                , idx);

        return (status == 0);
    }

    public PatchReviewProductRes cancelReviewProduct(BigInteger idx) {

        String cancelReviewQuery = "update ReviewProduct set status = ? where idx = ?";
        Object[] cancelReviewParams = new Object[] { 0, idx };

        this.jdbcTemplate.update(cancelReviewQuery, cancelReviewParams);

        return new PatchReviewProductRes(idx, 0);
    }

    public List<GetReviewProductMoreInfoRes> getReviewsMoreInfoByProductIdx(BigInteger productIdx) {
//        String getReviewMoreInfoQuery = "select User.nickname as 유저이름, 내구성별점, 가격별점, 디자인별점, 배송별점, 이미지, 내용\n" +
//                "     , case when 오늘의집구매여부 = 'Y'\n" +
//                "         then '오늘의집 구매'\n" +
//                "         else '다른 쇼핑몰 구매' end as 오늘의집구매여부\n" +
//                "     , date_format(생성날짜, '%Y.%m.%d') as 생성날짜\n" +
//                "     , truncate((내구성별점 + 가격별점 + 디자인별점 + 배송별점)/4, 1) as 총별점, 도움이돼요_수 from User\n" +
//                "right join (select ReviewProduct.userIdx as 유저IDX, ReviewProduct.productIdx as 상품IDX, ReviewProduct.durabilityStars as 내구성별점\n" +
//                "     , ReviewProduct.priceStars as 가격별점, ReviewProduct.designStars as 디자인별점, ReviewProduct.deliveryStars as 배송별점\n" +
//                "     , ReviewProduct.imageUrl as 이미지, ReviewProduct.reviewText as 내용, ReviewProduct.isTodayHouse as 오늘의집구매여부, ReviewProduct.createdAt as 생성날짜\n" +
//                "     , count(case when LikeReviewProduct.status = 1 then 1 end) as 도움이돼요_수 from ReviewProduct\n" +
//                "left join LikeReviewProduct on LikeReviewProduct.reviewIdx = ReviewProduct.idx\n" +
//                "where ReviewProduct.status = 1\n" +
//                "group by ReviewProduct.idx) a on a.유저IDX = User.idx\n" +
//                "where 상품IDX = ?\n" +
//                "order by 생성날짜 DESC";

        String getReviewMoreInfoQuery = "select User.nickname as 유저이름, 내구성별점, 가격별점, 디자인별점, 배송별점, 이미지, 내용\n" +
                "     , case when 오늘의집구매여부 = 'Y'\n" +
                "         then '오늘의집 구매'\n" +
                "         else '다른 쇼핑몰 구매' end as 오늘의집구매여부\n" +
                "     , date_format(생성날짜, '%Y.%m.%d') as 생성날짜\n" +
                "     , truncate((내구성별점 + 가격별점 + 디자인별점 + 배송별점)/4, 1) as 총별점, 도움이돼요_수 from User\n" +
                "right join (select ReviewProduct.userIdx as 유저IDX, ReviewProduct.productIdx as 상품IDX, ReviewProduct.durabilityStars as 내구성별점\n" +
                "     , ReviewProduct.priceStars as 가격별점, ReviewProduct.designStars as 디자인별점, ReviewProduct.deliveryStars as 배송별점\n" +
                "     , ReviewProduct.imageUrl as 이미지, ReviewProduct.reviewText as 내용, ReviewProduct.isTodayHouse as 오늘의집구매여부, ReviewProduct.createdAt as 생성날짜\n" +
                "     , count(case when LikeReviewProduct.status = 1 then 1 end) as 도움이돼요_수 from ReviewProduct\n" +
                "left join LikeReviewProduct on LikeReviewProduct.reviewIdx = ReviewProduct.idx\n" +
                "where ReviewProduct.status = 1\n" +
                "group by ReviewProduct.idx) a on a.유저IDX = User.idx\n" +
                "where 상품IDX = ?\n" +
                "order by 도움이돼요_수 DESC";

        return this.jdbcTemplate.query(getReviewMoreInfoQuery,
                (rs, rowNum) -> new GetReviewProductMoreInfoRes(
                        rs.getString("유저이름"),
                        rs.getObject("내구성별점", Integer.class),
                        rs.getObject("가격별점", Integer.class),
                        rs.getObject("디자인별점", Integer.class),
                        rs.getObject("배송별점", Integer.class),
                        rs.getString("이미지"),
                        rs.getString("내용"),
                        rs.getString("오늘의집구매여부"),
                        rs.getString("생성날짜"),
                        rs.getObject("총별점", Float.class),
                        rs.getObject("도움이돼요_수", Integer.class)
                ), productIdx);
    }

    public boolean isNullResponseReviews() {
        String checkNullQuery = "select count(case when status = 1 then 1 end) as rowN from ReviewProduct";
        Integer countOfReviews = this.jdbcTemplate.queryForObject(checkNullQuery, Integer.class);

        return countOfReviews.equals(0);
    }

    public boolean isNullResponseReviewsByUserIdx(BigInteger userIdx) {
        String checkNullQuery = "select count(case when userIdx = ? and status = 1 then 1 end) as rowN from ReviewProduct";
        Integer countOfReviews = this.jdbcTemplate.queryForObject(checkNullQuery, Integer.class, userIdx);

        return countOfReviews.equals(0);
    }

    public boolean isNullResponseReviewsByProductIdx(BigInteger productIdx) {
        String checkNullQuery = "select count(case when productIdx = ? and status = 1 then 1 end) as rowN from ReviewProduct";
        Integer countOfReviews = this.jdbcTemplate.queryForObject(checkNullQuery, Integer.class, productIdx);

        return countOfReviews.equals(0);
    }

    public boolean isNullResponseReviewsByIdx(BigInteger idx) {
        String checkNullQuery = "select count(case when idx = ? and status = 1 then 1 end) as rowN from ReviewProduct";
        Integer countOfReviews = this.jdbcTemplate.queryForObject(checkNullQuery, Integer.class, idx);

        return countOfReviews.equals(0);
    }

    public boolean isNullResponseDetailReviewsByProductIdx(BigInteger productIdx, BigInteger idx) {
        String checkNullQuery = "select count(case when idx = ? and productIdx = ? and status = 1 then 1 end) as rowN from ReviewProduct";
        Object[] checkNullParams = new Object[] { idx, productIdx };
        Integer countOfReview = this.jdbcTemplate.queryForObject(checkNullQuery, Integer.class, checkNullParams);

        return countOfReview.equals(0);
    }


    public ReviewProductDetailInfo getReviewsDetailInfoByProductIdxAndIdx(BigInteger productIdx, BigInteger idx) {
        String getReviewDetailInfoQuery= "select 유저이름, Product.name as 상품이름, 내구성별점, 가격별점, 디자인별점, 배송별점, 이미지, 내용, 오늘의집구매여부, 생성날짜, 총별점, 도움이돼요_수  from Product\n" +
                "right join (select User.nickname as 유저이름, 내구성별점, 가격별점, 디자인별점, 배송별점, 이미지, 내용\n" +
                "     , case when 오늘의집구매여부 = 'Y'\n" +
                "         then '오늘의집 구매'\n" +
                "         else '다른 쇼핑몰 구매' end as 오늘의집구매여부\n" +
                "     , date_format(생성날짜, '%Y.%m.%d') as 생성날짜\n" +
                "     , truncate((내구성별점 + 가격별점 + 디자인별점 + 배송별점)/4, 1) as 총별점, 도움이돼요_수, 리뷰IDX, 상품IDX  from User\n" +
                "right join (select ReviewProduct.idx as 리뷰IDX, ReviewProduct.userIdx as 유저IDX, ReviewProduct.productIdx as 상품IDX, ReviewProduct.durabilityStars as 내구성별점\n" +
                "     , ReviewProduct.priceStars as 가격별점, ReviewProduct.designStars as 디자인별점, ReviewProduct.deliveryStars as 배송별점\n" +
                "     , ReviewProduct.imageUrl as 이미지, ReviewProduct.reviewText as 내용, ReviewProduct.isTodayHouse as 오늘의집구매여부, ReviewProduct.createdAt as 생성날짜\n" +
                "     , count(case when LikeReviewProduct.status = 1 then 1 end) as 도움이돼요_수 from ReviewProduct\n" +
                "left join LikeReviewProduct on LikeReviewProduct.reviewIdx = ReviewProduct.idx\n" +
                "where ReviewProduct.status = 1\n" +
                "group by ReviewProduct.idx) a on a.유저IDX = User.idx\n" +
                "where 상품IDX = ? and a.리뷰IDX = ?) b on b.상품IDX = Product.idx";
        Object[] getReviewDetailInfoParams = new Object[] { productIdx, idx };

        return this.jdbcTemplate.queryForObject(getReviewDetailInfoQuery,
                (rs, rowNum) -> new ReviewProductDetailInfo(
                        rs.getString("유저이름"),
                        rs.getString("상품이름"),
                        rs.getObject("내구성별점", Integer.class),
                        rs.getObject("가격별점", Integer.class),
                        rs.getObject("디자인별점", Integer.class),
                        rs.getObject("배송별점", Integer.class),
                        rs.getString("이미지"),
                        rs.getString("내용"),
                        rs.getString("오늘의집구매여부"),
                        rs.getString("생성날짜"),
                        rs.getObject("총별점", Float.class),
                        rs.getObject("도움이돼요_수", Integer.class)
                ), getReviewDetailInfoParams);
    }

    public Integer getCountOfTotalReviews(BigInteger productIdx) {
        String getCountOfTotalReviewsQuery = "select count(case when productIdx = ? and status = 1 then 1 end) as rowN from ReviewProduct";

        return this.jdbcTemplate.queryForObject(getCountOfTotalReviewsQuery, Integer.class, productIdx);
    }
}
