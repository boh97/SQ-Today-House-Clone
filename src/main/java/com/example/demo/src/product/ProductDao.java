package com.example.demo.src.product;

import com.example.demo.src.order.model.PostOrderReq;
import com.example.demo.src.product.model.*;
import com.example.demo.src.reviewProduct.model.PostReviewProductReq;
import com.example.demo.src.user.model.GetUserRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class ProductDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetProductRes> getProducts() {
        String getProductsQuery = "select * from Product where status = 1";

        return this.jdbcTemplate.query(getProductsQuery,
                (rs,rowNum) -> new GetProductRes(
                        rs.getObject("idx", BigInteger.class),
                        rs.getObject("brandIdx", BigInteger.class),
                        rs.getObject("categoryIdx", BigInteger.class),
                        rs.getString("name"),
                        rs.getObject("price", Integer.class),
                        rs.getString("deliveryType"),
                        rs.getString("isTodayDeal"),
                        rs.getTimestamp("createdAt"),
                        rs.getTimestamp("updatedAt"),
                        rs.getObject("status", Integer.class),
                        rs.getObject("totalStar", Float.class)));
    }

    public GetProductRes getProduct(BigInteger idx) {
        String getProductQuery = "select * from Product where status = 1 and idx = ?";

        return this.jdbcTemplate.queryForObject(getProductQuery
                , (rs,rowNum) -> new GetProductRes(
                        rs.getObject("idx", BigInteger.class),
                        rs.getObject("brandIdx", BigInteger.class),
                        rs.getObject("categoryIdx", BigInteger.class),
                        rs.getString("name"),
                        rs.getObject("price", Integer.class),
                        rs.getString("deliveryType"),
                        rs.getString("isTodayDeal"),
                        rs.getTimestamp("createdAt"),
                        rs.getTimestamp("updatedAt"),
                        rs.getObject("status", Integer.class),
                        rs.getObject("totalStar", Float.class)), idx);
    }

    public List<GetProductMoreInfoRes> getProductsMoreInfoByCategory(BigInteger categoryIdx) {
        String getProductsMoreInfoQuery = "select 상품이름, 리뷰수, 상품가격, 별점, 브랜드이름, 오늘의딜여부, 배송유형, 상품사진 from (select 상품Idx, 브랜드Idx, 카테고리Idx, 상품이름, count(case when ReviewProduct.productIdx = 상품Idx and ReviewProduct.status = 1\n" +
                "    then 1\n" +
                "    end) as 리뷰수, 상품가격, 브랜드이름, 오늘의딜여부, 상품사진, 별점, 배송유형, 상품등록시간 from ReviewProduct\n" +
                "right join (select 상품Idx, 브랜드Idx, 카테고리Idx, 상품이름, 상품가격, 별점, 오늘의딜여부, 상품등록시간, 상품사진, 배송유형, Brand.name as 브랜드이름 from Brand\n" +
                "right join (select Product.idx as 상품Idx, Product.name as 상품이름, Product.categoryIdx as 카테고리Idx, Product.price as 상품가격, Product.isTodayDeal as 오늘의딜여부\n" +
                "                 , Product.brandIdx as 브랜드Idx, PI.imageUrl as 상품사진, Product.createdAt as 상품등록시간, Product.totalStar as 별점, Product.deliveryType as 배송유형 from Product\n" +
                "left join ProductImage PI on Product.idx = PI.productIdx\n" +
                "group by Product.idx) a on a.브랜드Idx = Brand.idx) b on b.상품Idx = ReviewProduct.productIdx\n" +
                "where 카테고리Idx = ? \n" +
                "group by b.상품Idx) c\n" +
                "order by 리뷰수 DESC\n";

        return this.jdbcTemplate.query(getProductsMoreInfoQuery,
                (rs, rowNum) -> new GetProductMoreInfoRes(
                        rs.getString("상품이름"),
                        rs.getObject("리뷰수", Integer.class),
                        rs.getObject("상품가격", Integer.class),
                        rs.getObject("별점", Float.class),
                        rs.getString("브랜드이름"),
                        rs.getString("오늘의딜여부"),
                        rs.getString("배송유형"),
                        rs.getString("상품사진"))
                , categoryIdx);
    }

    public List<GetProductMoreInfoRes> getProductsMoreInfoByCategoryAndOffset(BigInteger categoryIdx, Integer offset) {
        String getProductsMoreInfoQuery = "select 상품이름, 리뷰수, 상품가격, 별점, 브랜드이름, 오늘의딜여부, 배송유형, 상품사진 from (select 상품Idx, 브랜드Idx, 카테고리Idx, 상품이름, count(case when ReviewProduct.productIdx = 상품Idx and ReviewProduct.status = 1\n" +
                "    then 1\n" +
                "    end) as 리뷰수, 상품가격, 브랜드이름, 오늘의딜여부, 상품사진, 별점, 배송유형, 상품등록시간 from ReviewProduct\n" +
                "right join (select 상품Idx, 브랜드Idx, 카테고리Idx, 상품이름, 상품가격, 별점, 오늘의딜여부, 상품등록시간, 상품사진, 배송유형, Brand.name as 브랜드이름 from Brand\n" +
                "right join (select Product.idx as 상품Idx, Product.name as 상품이름, Product.categoryIdx as 카테고리Idx, Product.price as 상품가격, Product.isTodayDeal as 오늘의딜여부\n" +
                "                 , Product.brandIdx as 브랜드Idx, PI.imageUrl as 상품사진, Product.createdAt as 상품등록시간, Product.totalStar as 별점, Product.deliveryType as 배송유형 from Product\n" +
                "left join ProductImage PI on Product.idx = PI.productIdx\n" +
                "group by Product.idx) a on a.브랜드Idx = Brand.idx) b on b.상품Idx = ReviewProduct.productIdx\n" +
                "where 카테고리Idx = ?\n" +
                "group by b.상품Idx) c\n" +
                "order by 리뷰수 DESC\n" +
                "limit 10\n" +
                "offset ?";

        Object[] getProductsMoreInfoParams = new Object[] { categoryIdx, offset };

        return this.jdbcTemplate.query(getProductsMoreInfoQuery,
                (rs, rowNum) -> new GetProductMoreInfoRes(
                        rs.getString("상품이름"),
                        rs.getObject("리뷰수", Integer.class),
                        rs.getObject("상품가격", Integer.class),
                        rs.getObject("별점", Float.class),
                        rs.getString("브랜드이름"),
                        rs.getString("오늘의딜여부"),
                        rs.getString("배송유형"),
                        rs.getString("상품사진"))
                , getProductsMoreInfoParams);
    }

    public boolean isPreExistsProduct(PostProductReq postProductReq) {
        String checkPreExistProductQuery = "select exists(select name from Product where name = ? and status = 1)";

        Integer existsNum = this.jdbcTemplate.queryForObject(checkPreExistProductQuery, Integer.class, postProductReq.getName());

        return existsNum == 1;
    }

    public PostProductRes createProduct(PostProductReq postProductReq) {

        String createProductQuery = "insert into Product (brandIdx, categoryIdx, name, price, deliveryType, isTodayDeal, status, totalStar) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Object[] createProductParams = new Object[] { postProductReq.getBrandIdx(), postProductReq.getCategoryIdx(), postProductReq.getName()
                , postProductReq.getPrice(), postProductReq.getDeliveryType(), postProductReq.getIsTodayDeal(), 1, 0.0};

        this.jdbcTemplate.update(createProductQuery, createProductParams);

        String getLastInsertIdxQuery = "select last_insert_id()";
        BigInteger lastInsertIdx = this.jdbcTemplate.queryForObject(getLastInsertIdxQuery, BigInteger.class);

        return new PostProductRes(lastInsertIdx, 1);
    }

    public boolean isPreDeletedProduct(BigInteger idx) {
        String checkPreDeletedProductQuery = "select exists(select idx from Product where idx = ? and status = 0)";

        Integer existsNum = this.jdbcTemplate.queryForObject(checkPreDeletedProductQuery, Integer.class, idx);

        return existsNum == 1;
    }

    public PatchProductRes deleteProduct(BigInteger idx) {
        String deleteProductQuery = "update Product set status = 0 where idx = ?";
        this.jdbcTemplate.update(deleteProductQuery, idx);

        String getLastInsertIdxQuery = "select last_insert_id()";
        BigInteger lastInsertIdx = this.jdbcTemplate.queryForObject(getLastInsertIdxQuery, BigInteger.class);

        return new PatchProductRes(lastInsertIdx, 0);
    }

    public List<GetProductMoreInfoRes> getProductsMoreInfo() {
        String getProductMoreInfoQuery = "select 상품이름, 리뷰수, 상품가격, 별점, 브랜드이름, 오늘의딜여부, 배송유형, 상품사진 from (select 상품이름, count(case when ReviewProduct.productIdx = 상품Idx and ReviewProduct.status = 1\n" +
                "    then 1\n" +
                "    end) as 리뷰수, 상품가격, 별점, 브랜드이름, 오늘의딜여부, 배송유형, 상품사진, 상품등록시간 from ReviewProduct\n" +
                "right join (select 상품Idx, 별점, 배송유형, 브랜드Idx, 카테고리Idx, 상품이름, 상품가격, 오늘의딜여부, 상품등록시간, 상품사진, Brand.name as 브랜드이름 from Brand\n" +
                "right join (select Product.idx as 상품Idx, Product.name as 상품이름, Product.categoryIdx as 카테고리Idx, Product.price as 상품가격, Product.isTodayDeal as 오늘의딜여부\n" +
                "                 , Product.brandIdx as 브랜드Idx, PI.imageUrl as 상품사진, Product.createdAt as 상품등록시간, Product.totalStar as 별점, Product.deliveryType as 배송유형 from Product\n" +
                "left join ProductImage PI on Product.idx = PI.productIdx\n" +
                "group by Product.idx) a on a.브랜드Idx = Brand.idx) b on b.상품Idx = ReviewProduct.productIdx\n" +
                "group by b.상품Idx) c\n" +
                "order by c.리뷰수 DESC;";

        return this.jdbcTemplate.query(getProductMoreInfoQuery,
                (rs, rowNum) -> new GetProductMoreInfoRes(
                        rs.getString("상품이름"),
                        rs.getObject("리뷰수", Integer.class),
                        rs.getObject("상품가격", Integer.class),
                        rs.getObject("별점", Float.class),
                        rs.getString("브랜드이름"),
                        rs.getString("오늘의딜여부"),
                        rs.getString("배송유형"),
                        rs.getString("상품사진")));
    }

    public Integer getProductPrice(PostOrderReq postOrderReq) {
        //더할 point의 값 구한다.
        String getProductPriceQuery = "select price from Product where idx = ? and status = 1";
        return this.jdbcTemplate.queryForObject(getProductPriceQuery, Integer.class, postOrderReq.getProductIdx());
    }

    public void setProductTotalStars(PostReviewProductReq postReviewProductReq) {
        String getProductTotalStarsQuery = "select totalStar from Product where idx = ?";
        String getTotalReviewsQuery = "select count(case when ReviewProduct.status = 1 then 1 end) as 리뷰수 from Product\n" +
                "left join ReviewProduct on Product.idx = ReviewProduct.productIdx\n" +
                "where Product.idx = ?\n" +
                "group by Product.idx";

        double thisReviewTotalStars = Math.round((postReviewProductReq.getDurabilityStars() + postReviewProductReq.getPriceStars()
                + postReviewProductReq.getDesignStars() + postReviewProductReq.getDeliveryStars()) / 4.0 * 10) / 10.0;


        Integer reviews = this.jdbcTemplate.queryForObject(getTotalReviewsQuery, Integer.class, postReviewProductReq.getProductIdx());
        double totalStars = this.jdbcTemplate.queryForObject(getProductTotalStarsQuery, double.class, postReviewProductReq.getProductIdx());

        String setProductTotalStarQuery = "update Product set totalStar = ? where idx = ?";


        if (!reviews.equals(0)) {
            double newTotalStar = Math.round((thisReviewTotalStars + totalStars * reviews) / (reviews + 1) * 10) / 10.0;

            Object[] setProductTotalStarParams = new Object[]{ newTotalStar, postReviewProductReq.getProductIdx()};

            this.jdbcTemplate.update(setProductTotalStarQuery, setProductTotalStarParams);
        } else {
            Object[] setProductTotalStarParams = new Object[] { thisReviewTotalStars, postReviewProductReq.getProductIdx() };

            this.jdbcTemplate.update(setProductTotalStarQuery, setProductTotalStarParams);
        }
    }

    public boolean isNotExistedProduct(BigInteger idx) {
        String checkNullQuery = "select count(case when idx = ? and status = 1 then 1 end) as rowN from Product";

        Integer isExistedNum = this.jdbcTemplate.queryForObject(checkNullQuery, Integer.class, idx);
        return (isExistedNum.equals(0));
    }

    public List<GetProductMoreInfoRes> getProductsMoreInfoByWatching(BigInteger userIdx, Integer offset) {
        String getProductsByWatchingQuery = "select 상품이름, 리뷰수, 상품가격, 별점, 브랜드이름, 오늘의딜여부, 배송유형, 상품사진 from WatchProduct\n" +
                "right join (select 상품Idx, 상품이름, 리뷰수, 상품가격, 별점, 브랜드이름, 오늘의딜여부, 배송유형, 상품사진 from (select 상품이름, count(case when ReviewProduct.productIdx = 상품Idx and ReviewProduct.status = 1\n" +
                "    then 1\n" +
                "    end) as 리뷰수, 상품가격, 별점, 브랜드이름, 오늘의딜여부, 배송유형, 상품사진, 상품등록시간, 상품Idx from ReviewProduct\n" +
                "right join (select 상품Idx, 별점, 배송유형, 브랜드Idx, 카테고리Idx, 상품이름, 상품가격, 오늘의딜여부, 상품등록시간, 상품사진, Brand.name as 브랜드이름 from Brand\n" +
                "right join (select Product.idx as 상품Idx, Product.name as 상품이름, Product.categoryIdx as 카테고리Idx, Product.price as 상품가격, Product.isTodayDeal as 오늘의딜여부\n" +
                "                 , Product.brandIdx as 브랜드Idx, PI.imageUrl as 상품사진, Product.createdAt as 상품등록시간, Product.totalStar as 별점, Product.deliveryType as 배송유형 from Product\n" +
                "left join ProductImage PI on Product.idx = PI.productIdx\n" +
                "group by Product.idx) a on a.브랜드Idx = Brand.idx) b on b.상품Idx = ReviewProduct.productIdx\n" +
                "group by b.상품Idx) c\n" +
                "order by c.리뷰수 DESC) d on d.상품Idx = WatchProduct.productIdx\n" +
                "where WatchProduct.userIdx = ? and WatchProduct.status = 1\n" +
                "order by WatchProduct.createdAt DESC\n" +
                "limit 5\n" +
                "offset ?";

        Object[] getProductsByWatchingParams = new Object[] { userIdx, offset };

        return this.jdbcTemplate.query(getProductsByWatchingQuery,
                (rs, rowNum) -> new GetProductMoreInfoRes(
                        rs.getString("상품이름"),
                        rs.getObject("리뷰수", Integer.class),
                        rs.getObject("상품가격", Integer.class),
                        rs.getObject("별점", Float.class),
                        rs.getString("브랜드이름"),
                        rs.getString("오늘의딜여부"),
                        rs.getString("배송유형"),
                        rs.getString("상품사진")), getProductsByWatchingParams);
    }

    public void setProductTodayDeal(BigInteger productIdx, String isTodayDeal) {
        String setProductTodayDealQuery = "update Product set isTodayDeal = ? where idx = ? and status = 1";
        Object[] setProductTodayDealParams = new Object[] { isTodayDeal, productIdx };

        this.jdbcTemplate.update(setProductTodayDealQuery, setProductTodayDealParams);
    }

    public List<GetProductImageRes> getProductImages(BigInteger idx) {
        String getProductImagesQuery = "select idx, imageUrl from ProductImage\n" +
                "where productIdx = ? and status = 1";

        return this.jdbcTemplate.query(getProductImagesQuery,
                (rs, rowNum) -> new GetProductImageRes(
                        rs.getObject("idx", BigInteger.class),
                        rs.getString("imageUrl")
                ),idx);
    }

    public boolean isNullResponseByProductImages(BigInteger idx) {
        String checkProductImagesQuery = "select count(case when status = 1 then 1 end) as rowN from ProductImage\n" +
                "where productIdx = ?";
        Integer countOfProductImages = this.jdbcTemplate.queryForObject(checkProductImagesQuery, Integer.class, idx);

        return countOfProductImages.equals(0);
    }

    public boolean isNullResponseByProductInfoImages(BigInteger idx) {
        String checkProductInfoImagesQuery = "select count(case when status = 1 then 1 end) as rowN from ProductInfoImage\n" +
                "where productIdx = ?";
        Integer countOfProductInfoImages = this.jdbcTemplate.queryForObject(checkProductInfoImagesQuery, Integer.class, idx);

        return countOfProductInfoImages.equals(0);
    }

    public List<GetProductInfoImageRes> getProductInfoImages(BigInteger idx) {
        String getProductInfoImagesQuery = "select idx, imageUrl from ProductInfoImage\n" +
                "where productIdx = ? and status = 1";

        return this.jdbcTemplate.query(getProductInfoImagesQuery,
                (rs, rowNum) -> new GetProductInfoImageRes(
                        rs.getObject("idx", BigInteger.class),
                        rs.getString("imageUrl")
                ), idx);
    }

    public boolean isNullResponseProduct() {
        String checkNullQuery = "select count(case when status = 1 then 1 end) as rowN from Product";
        Integer countOfProduct = this.jdbcTemplate.queryForObject(checkNullQuery, Integer.class);

        return countOfProduct.equals(0);
    }

    public boolean isNullResponseProductByIdx(BigInteger idx) {
        String checkNullQuery = "select count(case when idx = ? and status = 1 then 1 end) as rowN from Product";
        Integer countOfProduct = this.jdbcTemplate.queryForObject(checkNullQuery, Integer.class, idx);

        return countOfProduct.equals(0);
    }

    public boolean isNullResponseWatchByUserIdxAndOffset(BigInteger userIdx, Integer offset) {
        String checkNullQuery = "select count(case when userIdx = ? and status = 1 then 1 end) as rowN from WatchProduct";
        Integer countOfWatch = this.jdbcTemplate.queryForObject(checkNullQuery, Integer.class, userIdx);

        return countOfWatch.equals(0) || countOfWatch <= offset;
    }

    public boolean isNullResponseProductByCategoryIdx(BigInteger categoryIdx) {
        String checkNullQuery = "select count(case when categoryIdx = ? and status = 1 then 1 end) as rowN from Product";
        Integer countOfProductsInCategory = this.jdbcTemplate.queryForObject(checkNullQuery, Integer.class, categoryIdx);

        return countOfProductsInCategory.equals(0);
    }

    public boolean isNullResponseProductByCategoryIdxAndOffset(BigInteger categoryIdx, Integer offset) {
        String checkNullQuery = "select count(case when categoryIdx = ? and status = 1 then 1 end) as rowN from Product";
        Integer countOfProductsInCategory = this.jdbcTemplate.queryForObject(checkNullQuery, Integer.class, categoryIdx);

        return countOfProductsInCategory.equals(0) || countOfProductsInCategory <= offset;
    }

    public List<ProductInfo> getProductInfo() {
        String getProductInfoQuery = "select 상품이름, 리뷰수, 상품가격, 별점, 브랜드이름, 오늘의딜여부, 배송유형, 상품사진 from (select 상품이름, count(case when ReviewProduct.productIdx = 상품Idx and ReviewProduct.status = 1\n" +
                "    then 1\n" +
                "    end) as 리뷰수, 상품가격, 별점, 브랜드이름, 오늘의딜여부, 배송유형, 상품사진, 상품등록시간, 상품Idx from ReviewProduct\n" +
                "right join (select 상품Idx, 별점, 배송유형, 브랜드Idx, 카테고리Idx, 상품이름, 상품가격, 오늘의딜여부, 상품등록시간, 상품사진, Brand.name as 브랜드이름 from Brand\n" +
                "right join (select Product.idx as 상품Idx, Product.name as 상품이름, Product.categoryIdx as 카테고리Idx, Product.price as 상품가격, Product.isTodayDeal as 오늘의딜여부\n" +
                "                 , Product.brandIdx as 브랜드Idx, PI.imageUrl as 상품사진, Product.createdAt as 상품등록시간, Product.totalStar as 별점, Product.deliveryType as 배송유형 from Product\n" +
                "left join ProductImage PI on Product.idx = PI.productIdx\n" +
                "group by Product.idx) a on a.브랜드Idx = Brand.idx) b on b.상품Idx = ReviewProduct.productIdx\n" +
                "group by b.상품Idx) c\n" +
                "order by c.리뷰수 DESC";

        return this.jdbcTemplate.query(getProductInfoQuery
                , (rs, rowNum) -> new ProductInfo(
                        rs.getString("상품이름"),
                        rs.getObject("리뷰수", Integer.class),
                        rs.getObject("상품가격", Integer.class),
                        rs.getObject("별점", Float.class),
                        rs.getString("브랜드이름"),
                        rs.getString("오늘의딜여부"),
                        rs.getString("배송유형"),
                        rs.getString("상품사진")));
    }

    public ProductInfo getProductInfo(BigInteger idx) {
        String getProductInfoQuery = "select 상품이름, 리뷰수, 상품가격, 별점, 브랜드이름, 오늘의딜여부, 배송유형, 상품사진 from (select 상품이름, count(case when ReviewProduct.productIdx = 상품Idx and ReviewProduct.status = 1\n" +
                "    then 1\n" +
                "    end) as 리뷰수, 상품가격, 별점, 브랜드이름, 오늘의딜여부, 배송유형, 상품사진, 상품등록시간, 상품Idx from ReviewProduct\n" +
                "right join (select 상품Idx, 별점, 배송유형, 브랜드Idx, 카테고리Idx, 상품이름, 상품가격, 오늘의딜여부, 상품등록시간, 상품사진, Brand.name as 브랜드이름 from Brand\n" +
                "right join (select Product.idx as 상품Idx, Product.name as 상품이름, Product.categoryIdx as 카테고리Idx, Product.price as 상품가격, Product.isTodayDeal as 오늘의딜여부\n" +
                "                 , Product.brandIdx as 브랜드Idx, PI.imageUrl as 상품사진, Product.createdAt as 상품등록시간, Product.totalStar as 별점, Product.deliveryType as 배송유형 from Product\n" +
                "left join ProductImage PI on Product.idx = PI.productIdx\n" +
                "group by Product.idx) a on a.브랜드Idx = Brand.idx) b on b.상품Idx = ReviewProduct.productIdx\n" +
                "group by b.상품Idx) c\n" +
                "where c.상품Idx = ?\n" +
                "order by c.리뷰수 DESC";

        return this.jdbcTemplate.queryForObject(getProductInfoQuery
                , (rs, rowNum) -> new ProductInfo(
                        rs.getString("상품이름"),
                        rs.getObject("리뷰수", Integer.class),
                        rs.getObject("상품가격", Integer.class),
                        rs.getObject("별점", Float.class),
                        rs.getString("브랜드이름"),
                        rs.getString("오늘의딜여부"),
                        rs.getString("배송유형"),
                        rs.getString("상품사진")), idx);
    }

    public int getProductCount() {
        String getProductCount = "select count(case when status = 1 then 1 end) as rowN from Product";
        return this.jdbcTemplate.queryForObject(getProductCount, Integer.class);
    }

    public boolean getIsScrapped(String productName, BigInteger userIdx) {
        String getProductIdxQuery = "select idx from Product where name = ?";
        BigInteger productIdx = this.jdbcTemplate.queryForObject(getProductIdxQuery, BigInteger.class, productName);

        String getIsScrappedQuery = "select count(case when userIdx = ? and productIdx = ? and status = 1 then 1 end) as rowN from ScrapProduct";
        Object[] getIsScrappedParams = new Object[] { userIdx, productIdx };
        Integer countOfIsScrapped = this.jdbcTemplate.queryForObject(getIsScrappedQuery, Integer.class, getIsScrappedParams);

        return !countOfIsScrapped.equals(0);
    }
}
