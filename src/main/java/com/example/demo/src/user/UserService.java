package com.example.demo.src.user;



import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.user.logout.LogoutDao;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;
    private final LogoutDao logoutDao;

    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService, LogoutDao logoutDao) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;
        this.logoutDao = logoutDao;
    }

    //POST
    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {
        //중복
        if(userProvider.checkEmail(postUserReq.getEmail()) == 1) {
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }

        String pwd;

        try{
            //암호화
            pwd = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(postUserReq.getPassword());
            postUserReq.setPassword(pwd);
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }

        try{
            BigInteger idx = userDao.createUser(postUserReq);
            //jwt 발급.
            String jwt = jwtService.createJwt(idx);
            return new PostUserRes(idx, jwt);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyUserNickname(PatchUserReq patchUserReq) throws BaseException {
        try{
            int result = userDao.modifyUserNickname(patchUserReq);

            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USERNAME);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostLogoutRes logout(BigInteger idx, String jwtToken) throws BaseException {
        try {
            PostLogoutRes postLogoutRes = logoutDao.logout(idx, jwtToken);

            return postLogoutRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostKakaoLoginRes createUserByKakao(String email, String nickname) throws BaseException {
        try {
            BigInteger idx;

            //이미 회원가입된 유저인지 회원가입해야하는 유저인지 구분하는 변수
            boolean isJoinedUser;
            if (userProvider.checkEmail(email) == 1) {
                idx = userDao.getIdx(email);
                isJoinedUser = true;
            } else {
                idx = userDao.createUserByKakao(email, nickname);
                isJoinedUser = false;
            }

            String jwt = jwtService.createJwt(idx);

            return new PostKakaoLoginRes(idx, jwt, isJoinedUser);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetUserRes setKakaoUserInfo(BigInteger idx, PostKakaoUserReq postKakaoUserReq) throws BaseException {
        try {
            userDao.setKakaoUserInfo(idx, postKakaoUserReq);
            GetUserRes getUserRes = userDao.getUser(idx);

            return getUserRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetUserRes deleteUser(BigInteger idx) throws BaseException {
        System.out.println(idx);
        System.out.println(userDao.isNotExistedUser(idx));

        if (userDao.isNotExistedUser(idx)) {
            throw new BaseException(RESPONSE_NULL_ERROR_BY_IDX);
        }

        if (userDao.isDeletedUser(idx)) {
            throw new BaseException(PATCH_PRE_DELETED_USER);
        }

        try {
            userDao.deleteUser(idx);
            GetUserRes getUserRes = userDao.getDeletedUser(idx);

            return getUserRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
