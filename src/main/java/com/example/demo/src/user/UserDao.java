package com.example.demo.src.user;


import com.example.demo.src.order.model.PostOrderReq;
import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetUserRes> getUsers(){
        String getUsersQuery = "select * from User where status = 1";
        return this.jdbcTemplate.query(getUsersQuery,
                (rs,rowNum) -> new GetUserRes(
                        rs.getObject("idx", BigInteger.class),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("nickname"),
                        rs.getString("grade"),
                        rs.getObject("point", Integer.class),
                        rs.getString("housingType"),
                        rs.getString("houseSize"),
                        rs.getString("birthDay"),
                        rs.getString("sex"),
                        rs.getString("socialLoginType"),
                        rs.getTimestamp("createdAt"),
                        rs.getTimestamp("updatedAt"),
                        rs.getObject("status", Integer.class))
                );
    }

//    public List<GetUserRes> getUsersByEmail(String email){
//        String getUsersByEmailQuery = "select * from UserInfo where email =?";
//        String getUsersByEmailParams = email;
//        return this.jdbcTemplate.query(getUsersByEmailQuery,
//                (rs, rowNum) -> new GetUserRes(
//                        rs.getInt("userIdx"),
//                        rs.getString("userName"),
//                        rs.getString("ID"),
//                        rs.getString("Email"),
//                        rs.getString("password")),
//                getUsersByEmailParams);
//    }

    public GetUserRes getUser(BigInteger idx) {
        String getUserQuery = "select * from User where idx = ? and status = 1";
        BigInteger getUserParams = idx;

        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs,rowNum) -> new GetUserRes(
                        rs.getObject("idx", BigInteger.class),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("nickname"),
                        rs.getString("grade"),
                        rs.getObject("point", Integer.class),
                        rs.getString("housingType"),
                        rs.getString("houseSize"),
                        rs.getString("birthDay"),
                        rs.getString("sex"),
                        rs.getString("socialLoginType"),
                        rs.getTimestamp("createdAt"),
                        rs.getTimestamp("updatedAt"),
                        rs.getObject("status", Integer.class)),
                getUserParams);
    }
    

    public BigInteger createUser(PostUserReq postUserReq){
        String createUserQuery = "insert into User (email, password, nickname, housingType, houseSize, birthDay, sex, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Object[] createUserParams = new Object[]{postUserReq.getEmail(), postUserReq.getPassword(), postUserReq.getNickname(), postUserReq.getHousingType()
                , postUserReq.getHouseSize(), postUserReq.getBirthDay(), postUserReq.getSex() , postUserReq.getStatus() };
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, BigInteger.class);
    }

    public int checkEmail(String email){
        String checkEmailQuery = "select exists(select email from User where email = ? and status = 1)";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);

    }

    public int modifyUserNickname(PatchUserReq patchUserReq){
        String modifyUserNameQuery = "update User set nickname = ? where idx = ? and status = 1";
        Object[] modifyUserNameParams = new Object[]{ patchUserReq.getNickname(), patchUserReq.getIdx() };

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    public User getPwd(PostLoginReq postLoginReq){
        String getPwdQuery = "select * from User where email = ? and status = 1";
        String getPwdParams = postLoginReq.getEmail();

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs,rowNum)-> new User(
                        rs.getObject("idx", BigInteger.class),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("nickname"),
                        rs.getString("grade"),
                        rs.getObject("point", Integer.class),
                        rs.getString("housingType"),
                        rs.getString("houseSize"),
                        rs.getString("birthDay"),
                        rs.getString("sex"),
                        rs.getString("socialLoginType"),
                        rs.getTimestamp("createdAt"),
                        rs.getTimestamp("updatedAt"),
                        rs.getObject("status", Integer.class)),
                getPwdParams);
    }


    public boolean isValidStatus(PostLoginReq postLoginReq) {
        String checkStatusQuery = "select status from User where email = ?";
        String checkStatusParams = postLoginReq.getEmail();

        Integer status = this.jdbcTemplate.queryForObject(checkStatusQuery
                , Integer.class
                , checkStatusParams);

        return (status == 1);
    }

    public boolean isValidPassword(String password) {
        String checkPasswordQuery = "select exists(select password from User where password = ?)";
        String checkPasswordParams = password;

        return this.jdbcTemplate.queryForObject(checkPasswordQuery,
                boolean.class,
                checkPasswordParams);
    }

    public BigInteger getIdx(String email) {
        String getIdQuery = "select idx from User where email = ? and status = 1";
        String getIdParams = email;

        return this.jdbcTemplate.queryForObject(getIdQuery,
                BigInteger.class,
                getIdParams);
    }

    public BigInteger createUserByKakao(String email, String nickname) {
        String createUserQuery = "insert into User (email, nickname, housingType, houseSize, birthDay, sex, socialLoginType, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        Object[] createUserParams = new Object[] { email, nickname, "default"
                , "default", "default", "d", "KAKAO", 0 };

        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, BigInteger.class);
    }

    public void setKakaoUserInfo(BigInteger idx, PostKakaoUserReq postKakaoUserReq) {
        String setKakaoUserInfoQuery = "update User set housingType = ?, houseSize = ?, birthDay = ?, sex = ?, status = ? where idx = ?";
        Object[] setKakaoUserInfoParams = new Object[] { postKakaoUserReq.getHousingType(), postKakaoUserReq.getHouseSize()
                , postKakaoUserReq.getBirthDay(), postKakaoUserReq.getSex(), 1, idx };

        this.jdbcTemplate.update(setKakaoUserInfoQuery, setKakaoUserInfoParams);
    }

    public boolean isKakaoUser(BigInteger idx) {
        String checkSocialTypeQuery = "select socialLoginType from User where idx = ?";
        BigInteger checkSocialTypeParams = idx;

        String socialLoginType = this.jdbcTemplate.queryForObject(checkSocialTypeQuery
                , String.class
                , checkSocialTypeParams);

        if (socialLoginType == "KAKAO") {
            return true;
        }
        return false;
    }

    public boolean isDeletedUser(BigInteger idx) {
        String checkDeletedUserQuery = "select status from User where idx = ?";
        BigInteger checkDeletedUserParams = idx;

        Integer status = this.jdbcTemplate.queryForObject(checkDeletedUserQuery
                , Integer.class
                , checkDeletedUserParams);

        return (status == 0);
    }

    public void deleteUser(BigInteger idx) {
        String deleteUserQuery = "update User set status = ? where idx = ?";
        Object[] deleteUserParams = new Object[] { 0, idx };

        this.jdbcTemplate.update(deleteUserQuery, deleteUserParams);
    }

    public GetUserRes getDeletedUser(BigInteger idx) {
        String getDeletedUserQuery = "select * from User where idx = ? and status = 0";
        BigInteger getDeletedUserParams = idx;
        return this.jdbcTemplate.queryForObject(getDeletedUserQuery,
                (rs,rowNum) -> new GetUserRes(
                        rs.getObject("idx", BigInteger.class),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("nickname"),
                        rs.getString("grade"),
                        rs.getObject("point", Integer.class),
                        rs.getString("housingType"),
                        rs.getString("houseSize"),
                        rs.getString("birthDay"),
                        rs.getString("sex"),
                        rs.getString("socialLoginType"),
                        rs.getTimestamp("createdAt"),
                        rs.getTimestamp("updatedAt"),
                        rs.getObject("status", Integer.class)),
                getDeletedUserParams);
    }

    public GetUserMoreInfoRes getUserMoreInfo(BigInteger idx) {
        String getUserMoreInfoQuery = "select 유저닉네임, 유저등급, 유저포인트, 스크랩수, 좋아요수, 입금대기_수, 결제완료_수, 배송준비_수, 배송중_수, 배송완료_수, 리뷰쓰기_수, 리뷰수, count(case when Question.status = 1 then 1 end) as 질문수 from Question\n" +
                "right join (select 유저IDX, 유저닉네임, 유저등급, 유저포인트, 스크랩수, 좋아요수, 입금대기_수, 결제완료_수, 배송준비_수, 배송중_수, 배송완료_수, 리뷰쓰기_수, count(case when ReviewProduct.status = 1\n" +
                "    then 1\n" +
                "    end) as 리뷰수  from ReviewProduct\n" +
                "right join (select 유저IDX, 유저닉네임, 유저등급, 유저포인트, (상품스크랩수 + 사진스크랩수) as 스크랩수, 좋아요수, count(case when Purchase.status = '입금대기' then 1 end) as 입금대기_수\n" +
                "     , count(case when Purchase.status = '결제완료' then 1 end) as 결제완료_수, count(case when Purchase.status = '배송준비' then 1 end) as 배송준비_수\n" +
                "     , count(case when Purchase.status = '배송중' then 1 end) as 배송중_수, count(case when Purchase.status = '배송완료' then 1 end) as 배송완료_수\n" +
                "     , count(case when Purchase.status = '리뷰쓰기' then 1 end) as 리뷰쓰기_수 from Purchase\n" +
                "right join (select 유저IDX, 유저닉네임, 유저등급, 유저포인트, 상품스크랩수, 좋아요수, count(case when ScrapPicture.userIdx = 유저IDX and ScrapPicture.status = 1\n" +
                "    then 1\n" +
                "    end) as 사진스크랩수 from ScrapPicture\n" +
                "right join (select 유저IDX, 유저닉네임, 유저등급, 유저포인트, 상품스크랩수, count(case when LikePicture.userIdx = 유저IDX and LikePicture.status = 1\n" +
                "    then 1\n" +
                "    end) as 좋아요수 from LikePicture\n" +
                "right join (select 유저IDX, 유저닉네임, 유저등급, 유저포인트, count(case when ScrapProduct.userIdx = 유저IDX and ScrapProduct.status = 1\n" +
                "    then 1\n" +
                "    end) as 상품스크랩수 from ScrapProduct\n" +
                "right join (select User.idx as 유저IDX, User.nickname as 유저닉네임, User.grade as 유저등급, User.point as 유저포인트 from User\n" +
                "where User.status = 1) a on a.유저IDX = ScrapProduct.userIdx\n" +
                "group by a.유저IDX) b on b.유저IDX = LikePicture.userIdx\n" +
                "group by b.유저IDX) c on c.유저IDX = ScrapPicture.userIdx\n" +
                "group by c.유저IDX) d on d.유저IDX = Purchase.userIdx\n" +
                "group by d.유저IDX) e on e.유저IDX = ReviewProduct.userIdx\n" +
                "group by e.유저IDX) f on f.유저IDX = Question.userIdx\n" +
                "where f.유저IDX = ? \n" +
                "group by f.유저IDX";

        return this.jdbcTemplate.queryForObject(getUserMoreInfoQuery,
                (rs, rowNum) -> new GetUserMoreInfoRes(
                        rs.getString("유저닉네임"),
                        rs.getString("유저등급"),
                        rs.getObject("유저포인트", Integer.class),
                        rs.getObject("스크랩수", Integer.class),
                        rs.getObject("좋아요수", Integer.class),
                        rs.getObject("입금대기_수", Integer.class),
                        rs.getObject("결제완료_수", Integer.class),
                        rs.getObject("배송준비_수", Integer.class),
                        rs.getObject("배송중_수", Integer.class),
                        rs.getObject("배송완료_수", Integer.class),
                        rs.getObject("리뷰쓰기_수", Integer.class),
                        rs.getObject("리뷰수", Integer.class),
                        rs.getObject("질문수", Integer.class)
                ), idx);
    }


    public String getUserGrade(PostOrderReq postOrderReq) {
        String getUserGradeQuery = "select grade from User where idx = ? and status = 1";
        return this.jdbcTemplate.queryForObject(getUserGradeQuery, String.class, postOrderReq.getUserIdx());
    }

    public Integer getUserPoint(PostOrderReq postOrderReq) {
        String getPointByUserQuery = "select point from User where idx = ? and status = 1";
        return this.jdbcTemplate.queryForObject(getPointByUserQuery, Integer.class, postOrderReq.getUserIdx());
    }

    public void setUserPoint(Integer totalUserPoint, PostOrderReq postOrderReq) {
        String plusUserPointQuery = "update User set point = ? where idx = ? and status = 1";
        Object[] plusUserPointParams = new Object[]{totalUserPoint, postOrderReq.getUserIdx()};
        this.jdbcTemplate.update(plusUserPointQuery, plusUserPointParams);
    }

    public boolean isNotExistedUser(BigInteger idx) {
        String checkNullQuery = "select count(case when idx = ? and status = 1 then 1 end) as rowN from User";

        Integer isExistedNum = this.jdbcTemplate.queryForObject(checkNullQuery, Integer.class, idx);
        return (isExistedNum.equals(0));
    }

    public boolean isNullResponseUser() {
        String checkNullQuery = "select count(case when status = 1 then 1 end) as rowN from User";
        Integer countOfUser = this.jdbcTemplate.queryForObject(checkNullQuery, Integer.class);

        return countOfUser.equals(0);
    }
}
