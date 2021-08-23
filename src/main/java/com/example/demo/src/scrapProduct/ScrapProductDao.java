package com.example.demo.src.scrapProduct;

import com.example.demo.src.scrapProduct.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigInteger;
import java.util.List;

@Repository
public class ScrapProductDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public List<GetScrapProductRes> getScrapsProduct() {
        String getScrapsProductQuery = "select * from ScrapProduct where status = 1";

        return this.jdbcTemplate.query(getScrapsProductQuery,
                (rs, rowNum) -> new GetScrapProductRes(
                        rs.getObject("idx", BigInteger.class),
                        rs.getObject("userIdx", BigInteger.class),
                        rs.getObject("productIdx", BigInteger.class),
                        rs.getTimestamp("createdAt"),
                        rs.getTimestamp("updatedAt"),
                        rs.getObject("status", Integer.class)));
    }

    public List<GetScrapProductRes> getScrapsProduct(BigInteger userIdx) {
        String getScrapsProductQuery = "select * from ScrapProduct where status = 1 and userIdx = ?";

        return this.jdbcTemplate.query(getScrapsProductQuery,
                (rs, rowNum) -> new GetScrapProductRes(
                        rs.getObject("idx", BigInteger.class),
                        rs.getObject("userIdx", BigInteger.class),
                        rs.getObject("productIdx", BigInteger.class),
                        rs.getTimestamp("createdAt"),
                        rs.getTimestamp("updatedAt"),
                        rs.getObject("status", Integer.class))
                , userIdx);
    }

    public List<GetScrapProductMoreInfoRes> getScrapsProductMoreInfoByUserIdx(BigInteger userIdx) {
        String getScrapsProductMoreInfoQuery = "select 상품이름, 브랜드이름, 상품사진, 리뷰수, 상품가격, 별점, 오늘의딜여부, 배송유형 from ScrapProduct\n" +
                "right join (select 상품Idx, 브랜드Idx, 카테고리Idx, 상품이름, count(case when ReviewProduct.productIdx = 상품Idx and ReviewProduct.status = 1\n" +
                "    then 1\n" +
                "    end) as 리뷰수, 상품가격, 브랜드이름, 오늘의딜여부, 상품사진, 별점, 상품등록시간, 배송유형 from ReviewProduct\n" +
                "right join (select 상품Idx, 브랜드Idx, 카테고리Idx, 상품이름, 상품가격, 별점, 오늘의딜여부, 상품등록시간, 상품사진,배송유형, Brand.name as 브랜드이름 from Brand\n" +
                "right join (select Product.idx as 상품Idx, Product.name as 상품이름, Product.categoryIdx as 카테고리Idx, Product.price as 상품가격, Product.isTodayDeal as 오늘의딜여부, Product.brandIdx as 브랜드Idx, PI.imageUrl as 상품사진, Product.createdAt as 상품등록시간, Product.totalStar as 별점, Product.deliveryType as 배송유형 from Product\n" +
                "left join ProductImage PI on Product.idx = PI.productIdx\n" +
                "group by Product.idx) a on a.브랜드Idx = Brand.idx) b on b.상품Idx = ReviewProduct.productIdx\n" +
                "group by b.상품Idx) c on c.상품Idx = ScrapProduct.productIdx\n" +
                "where ScrapProduct.userIdx = ? and ScrapProduct.status = 1\n" +
                "order by ScrapProduct.updatedAt DESC";

        return this.jdbcTemplate.query(getScrapsProductMoreInfoQuery,
                (rs, rowNum) -> new GetScrapProductMoreInfoRes(
                        rs.getString("상품이름"),
                        rs.getString("브랜드이름"),
                        rs.getString("상품사진"),
                        rs.getObject("리뷰수", Integer.class),
                        rs.getObject("상품가격", Integer.class),
                        rs.getObject("별점", Float.class),
                        rs.getString("오늘의딜여부"),
                        rs.getString("배송유형"))
                , userIdx);

    }

    public int isInScrapProduct(PostScrapProductReq postScrapProductReq) {
        String checkingIsInScrapQuery = "select exists(select * from ScrapProduct where userIdx = ? and productIdx = ?)";

        Object[] checkingIsInScrapParams = new Object[] { postScrapProductReq.getUserIdx(), postScrapProductReq.getProductIdx() };

        return this.jdbcTemplate.queryForObject(checkingIsInScrapQuery, int.class, checkingIsInScrapParams);
    }

    public PostScrapProductRes createScrapProduct(PostScrapProductReq postScrapProductReq) {
        String createScrapQuery = "insert into ScrapProduct (userIdx, productIdx, status) VALUES (?, ?, ?)";
        Object[] createScrapParams = new Object[] { postScrapProductReq.getUserIdx(), postScrapProductReq.getProductIdx(), 1 };

        this.jdbcTemplate.update(createScrapQuery, createScrapParams);

        String lastInsertIdxQuery = "select last_insert_id()";
        BigInteger lastInsertIdx = this.jdbcTemplate.queryForObject(lastInsertIdxQuery, BigInteger.class);

        String getScrapsProductQuery = "select * from ScrapProduct where idx = ?";
        return this.jdbcTemplate.queryForObject(getScrapsProductQuery,
                (rs, rowNum) -> new PostScrapProductRes(
                        rs.getObject("idx", BigInteger.class),
                        rs.getObject("userIdx", BigInteger.class),
                        rs.getObject("productIdx", BigInteger.class),
                        rs.getTimestamp("createdAt"),
                        rs.getTimestamp("updatedAt"),
                        rs.getObject("status", Integer.class))
                , lastInsertIdx);
    }

    public boolean isPreDeletedScrap(BigInteger idx) {
        String checkPreDeletedScrapQuery = "select status from ScrapProduct where idx = ?";

        Integer status = this.jdbcTemplate.queryForObject(checkPreDeletedScrapQuery, Integer.class, idx);
        return (status == 0);
    }

    public PatchScrapProductRes cancelScrap(BigInteger idx) {
        String cancelScrapQuery = "update ScrapProduct set status = ? where idx = ?";
        Object[] cancelScrapParams = new Object[] { 0, idx };

        this.jdbcTemplate.update(cancelScrapQuery, cancelScrapParams);

        String getPatchScrapResQuery = "select idx, status from ScrapProduct where idx = ?";
        return this.jdbcTemplate.queryForObject(getPatchScrapResQuery,
                (rs, rowNum) -> new PatchScrapProductRes(
                        rs.getObject("idx", BigInteger.class),
                        rs.getObject("status", Integer.class))
                , idx);
    }

    public boolean isNullResponseScraps() {
        String checkNullQuery = "select count(case when status = 1 then 1 end) as rowN from ScrapProduct";
        Integer countOfScraps = this.jdbcTemplate.queryForObject(checkNullQuery, Integer.class);

        return countOfScraps.equals(0);
    }

    public boolean isNullResponseScrapsByUserIdx(BigInteger userIdx) {
        String checkNullQuery = "select count(case when userIdx = ? and status = 1 then 1 end) as rowN from ScrapProduct";
        Integer countOfScrapsByUserIdx = this.jdbcTemplate.queryForObject(checkNullQuery, Integer.class, userIdx);

        return countOfScrapsByUserIdx.equals(0);
    }

    public boolean isNullResponseScrapsByIdx(BigInteger idx) {
        String checkNullQuery = "select count(case when idx = ? and status = 1 then 1 end) as rowN from ScrapProduct";
        Integer countOfScrapsByIdx = this.jdbcTemplate.queryForObject(checkNullQuery, Integer.class, idx);

        return countOfScrapsByIdx.equals(0);
    }
}
