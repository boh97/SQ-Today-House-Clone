package com.example.demo.src.user;

import com.example.demo.src.user.logout.LogoutDao;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexBirthDay;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;
import static com.example.demo.utils.Validation.*;

@RestController
@RequestMapping("/app/users")
public class UserController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserProvider userProvider;
    private final UserService userService;
    private final JwtService jwtService;
    private final LogoutDao logoutDao;

    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService, LogoutDao logoutDao) {
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
        this.logoutDao = logoutDao;
    }

//    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService){
//        this.userProvider = userProvider;
//        this.userService = userService;
//        this.jwtService = jwtService;
//    }

    /**
     * 회원 조회 API
     * [GET] /users
     * 회원 번호 및 이메일 검색 조회 API
     * [GET] /users? Email=
     * @return BaseResponse<List<GetUserRes>>
     */
    //Query String
    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/app/users
    public BaseResponse<List<GetUserRes>> getUsers() {
        try{
            List<GetUserRes> getUsersRes = userProvider.getUsers();
            return new BaseResponse<>(getUsersRes);
        } catch(BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 회원 1명 조회 API
     * [GET] /users/:userIdx
     * @return BaseResponse<GetUserRes>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{idx}") // (GET) 127.0.0.1:9000/app/users/:userIdx
    public BaseResponse<GetUserRes> getUser(@PathVariable("idx") BigInteger idx) {
        if (idx == null) {
            return new BaseResponse<>(EMPTY_IDX);
        }
        if (!isValidatedIdx(idx)) {
            return new BaseResponse<>(INVALID_IDX);
        }

        // Get Users
        try{
            //idx VAlidation
            GetUserRes getUserRes = userProvider.getUser(idx);
            return new BaseResponse<>(getUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 회원가입 API
     * [POST] /users
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {

        // TODO: email 관련한 짧은 validation 예시입니다. 그 외 더 부가적으로 추가해주세요!
        if(postUserReq.getEmail() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }
        //이메일 정규표현
        if(!isRegexEmail(postUserReq.getEmail())){
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }

        //생년월일 유효성 검사
        if (!isValidatedBirthDay(postUserReq.getBirthDay())) {
            return new BaseResponse<>(POST_USERS_INVALID_BIRTHDAY);
        }

        //거주형태 유효성 검사 (원룸&오피스텔, 아파트, 빌라, 단독주택)
        if (!isValidatedHousingType(postUserReq.getHousingType())) {
            return new BaseResponse<>(POST_USERS_INVALID_HOUSINGTYPE);
        }

        //집 평수 유효성 검사 (10평 미만, 10평대, 20평대, 30평대, 40평 이상)
        if (!isValidatedHouseSize(postUserReq.getHouseSize())) {
            return new BaseResponse<>(POST_USERS_INVALID_HOUSESIZE);
        }

        //성별 유효성 검사("F", "M")
        if (!isValidatedSex(postUserReq.getSex())) {
            return new BaseResponse<>(POST_USERS_INVALID_SEX);
        }

        if (postUserReq.getPassword() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD);
        }

        if (postUserReq.getNickname() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_NICKNAME);
        }

        if (postUserReq.getHousingType() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_HOUSINGTYPE);
        }

        if (postUserReq.getHouseSize() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_HOUSINGSIZE);
        }

        if (postUserReq.getBirthDay() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_BIRTHDAY);
        }

        if (postUserReq.getSex() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_SEX);
        }

        if (postUserReq.getStatus() != 1) {
            return new BaseResponse<>(POST_USERS_INVALID_STATUS);
        }

        try{
            PostUserRes postUserRes = userService.createUser(postUserReq);
            return new BaseResponse<>(postUserRes);
        } catch(BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 로그인 API
     * [POST] /users/logIn
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping("/login")
    public BaseResponse<PostLoginRes> login(@RequestBody PostLoginReq postLoginReq){
        try{
            // TODO: 로그인 값들에 대한 형식적인 validatin 처리해주셔야합니다!
            // TODO: 유저의 status ex) 비활성화된 유저, 탈퇴한 유저 등을 관리해주고 있다면 해당 부분에 대한 validation 처리도 해주셔야합니다.
            if (postLoginReq.getEmail() == null) {
                return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
            }

            if (postLoginReq.getPassword() == null) {
                return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD);
            }

            if (!isRegexEmail(postLoginReq.getEmail())) {
                return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
            }

            PostLoginRes postLoginRes = userProvider.login(postLoginReq);
            return new BaseResponse<>(postLoginRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 유저닉네임변경 API
     * [PATCH] /users/modify/nickname/:idx
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/modify/nickname/{idx}")
    public BaseResponse<String> modifyUserNickname(@PathVariable("idx") BigInteger idx, @RequestBody PatchUserReq patchUserReq) {
        if (idx == null) {
            return new BaseResponse<>(EMPTY_IDX);
        }
        if (!isValidatedIdx(idx)) {
            return new BaseResponse<>(INVALID_IDX);
        }

        if (userProvider.isNotExistedUser(idx)) {
            return new BaseResponse<>(RESPONSE_NULL_ERROR_BY_IDX);
        }

        try {
            //jwt에서 idx 추출 + jwt 유효성 검사
            BigInteger userIdxByJwt = jwtService.getUserIdx();

            //userIdx와 접근한 유저가 같은지 확인
            if(!idx.equals(userIdxByJwt)) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            //로그아웃 된 JWT인지 검사
            if (logoutDao.isLogOutJwt(jwtService.getJwt())) {
                return new BaseResponse<>(LOGOUT_JWT);
            }

            //같다면 유저네임 변경
            userService.modifyUserNickname(patchUserReq);

            String result = patchUserReq.getNickname() + "로 이름 변경 성공했습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 로그아웃 API
     * @param idx
     * @return
     */
    @ResponseBody
    @PostMapping("/logout/{idx}")
    public BaseResponse<PostLogoutRes> logout(@PathVariable("idx") BigInteger idx) {
        if (idx == null) {
            return new BaseResponse<>(EMPTY_IDX);
        }
        if (!isValidatedIdx(idx)) {
            return new BaseResponse<>(INVALID_IDX);
        }

        try {
            BigInteger userIdxByJwt = jwtService.getUserIdx();
            String jwtToken = jwtService.getJwt();

            if(jwtToken == null) {
                return new BaseResponse<>(EMPTY_JWT);
            }

            if (!idx.equals(userIdxByJwt)) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            PostLogoutRes postLogoutRes = userService.logout(idx, jwtToken);

            return new BaseResponse<>(postLogoutRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 소셜 로그인 API(카카오)
     * 기존에 로그인 되있던 유저면 로그인하고, 아니라면 새로 만든다.
     * @param accessToken
     * @return
     */
    @ResponseBody
    @PostMapping("/kakao/login")
    public BaseResponse<PostKakaoLoginRes> createKakaoUser(@RequestHeader String accessToken) {
        String reqURL = "https://kapi.kakao.com/v2/user/me";

        if (accessToken == null || accessToken.length() == 0) {
            return new BaseResponse<>(INVALID_ACCESS_TOKEN);
        }

        URL url;
        HttpURLConnection connection;

        String email;
        String nickname;

        try {
            url = new URL(reqURL);
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            connection.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = connection.getResponseCode();
            //System.out.println(responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));

            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = jsonParser.parse(result);

            JsonObject properties = jsonElement.getAsJsonObject().get("properties").getAsJsonObject();
            JsonObject kakaoAccount = jsonElement.getAsJsonObject().get("kakao_account").getAsJsonObject();

            email = kakaoAccount.getAsJsonObject().get("email").getAsString();
            nickname = properties.getAsJsonObject().get("nickname").getAsString();

        } catch (Exception exception) {
            return new BaseResponse<>(KAKAO_LOGIN_REQUEST_FAILED);
        }

        try {
            PostKakaoLoginRes postKakaoLoginRes = userService.createUserByKakao(email, nickname);
            return new BaseResponse<>(postKakaoLoginRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 카카오 액세스 토큰 인증 후, 추가로 앱에서 유저 정보 설정.
     * @param idx
     * @param postKakaoUserReq
     * @return
     */
    @ResponseBody
    @PatchMapping("/kakao/login/setInfo/{idx}")
    public BaseResponse<GetUserRes> setKakaoUserInfo(@PathVariable BigInteger idx, @RequestBody PostKakaoUserReq postKakaoUserReq) {
        if (idx == null) {
            return new BaseResponse<>(EMPTY_IDX);
        }
        if (!isValidatedIdx(idx)) {
            return new BaseResponse<>(INVALID_IDX);
        }

        if (postKakaoUserReq.getHousingType() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_HOUSINGTYPE);
        }

        if (postKakaoUserReq.getHouseSize() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_HOUSINGSIZE);
        }

        if (postKakaoUserReq.getBirthDay() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_BIRTHDAY);
        }

        if (postKakaoUserReq.getSex() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_SEX);
        }

        //RequestBody 의 유효성 처리
        //생년월일 정규표현
        if (!isValidatedBirthDay(postKakaoUserReq.getBirthDay())) {
            return new BaseResponse<>(POST_USERS_INVALID_BIRTHDAY);
        }

        //거주형태 유효성 검사 (원룸&오피스텔, 아파트, 빌라, 단독주택)
        if (!isValidatedHousingType(postKakaoUserReq.getHousingType())) {
            return new BaseResponse<>(POST_USERS_INVALID_HOUSINGTYPE);
        }

        //집 평수 유효성 검사 (10평 미만, 10평대, 20평대, 30평대, 40평 이상)
        if (!isValidatedHouseSize(postKakaoUserReq.getHouseSize())) {
            return new BaseResponse<>(POST_USERS_INVALID_HOUSESIZE);
        }

        //성별 유효성 검사("F", "M")
        if (!isValidatedSex(postKakaoUserReq.getSex())) {
            return new BaseResponse<>(POST_USERS_INVALID_SEX);
        }

        try {
            GetUserRes getUserRes = userService.setKakaoUserInfo(idx, postKakaoUserReq);
            return new BaseResponse<>(getUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 계정 탈퇴 API
     * @return
     */
    @ResponseBody
    @PatchMapping("/delete/{idx}")
    public BaseResponse<GetUserRes> deleteUser(@PathVariable BigInteger idx) {
        if (idx == null) {
            return new BaseResponse<>(EMPTY_IDX);
        }
        if (!isValidatedIdx(idx)) {
            return new BaseResponse<>(INVALID_IDX);
        }

        try {
            GetUserRes getUserRes = userService.deleteUser(idx);

            return new BaseResponse<>(getUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/moreInfo/{idx}")
    public BaseResponse<GetUserMoreInfoRes> getUserMoreInfo(@PathVariable BigInteger idx) {
        if (idx == null) {
            return new BaseResponse<>(EMPTY_IDX);
        }
        if (!isValidatedIdx(idx)) {
            return new BaseResponse<>(INVALID_IDX);
        }

        try {
            GetUserMoreInfoRes getUserMoreInfoRes = userProvider.getUserMoreInfo(idx);
            return new BaseResponse<>(getUserMoreInfoRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
