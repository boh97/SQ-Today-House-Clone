package com.example.demo.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),
    INVALID_USER_STATUS(false,2004,"삭제되거나 휴면인 계정의 접근입니다."),
    INVALID_USER_PASSWORD(false,2005,"잘못된 비밀번호입니다."),

    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),

    // [POST] /users
    POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false,2017,"중복된 이메일입니다."),
    POST_USERS_EMPTY_PASSWORD(false,2018,"비밀번호를 입력해주세요."),
    POST_USERS_EMPTY_NICKNAME(false,2019,"닉네임을 입력해주세요."),
    POST_USERS_EMPTY_HOUSINGTYPE(false,2020,"거주하고 있는 집의 형태을 입력해주세요."),
    POST_USERS_EMPTY_HOUSINGSIZE(false,2021,"거주하고 있는 집의 평수를 입력해주세요."),
    POST_USERS_EMPTY_BIRTHDAY(false,2022,"생년월일을 입력해주세요."),
    POST_USERS_EMPTY_SEX(false,2023,"성별을 입력해주세요."),
    POST_USERS_INVALID_BIRTHDAY(false,2024,"생년월일 입력 값이 잘못되었지 않은지 확인해주세요."),
    POST_USERS_INVALID_HOUSINGTYPE(false,2025,"거주형태의 형식을 확인해주세요."),
    POST_USERS_INVALID_HOUSESIZE(false,2026,"집의 평수의 형식을 확인해주세요."),
    POST_USERS_INVALID_STATUS(false,2027,"유저의 상태 값을 확인해주세요."),
    POST_USERS_INVALID_SEX(false,2028,"유저의 성별의 형식을 확인해주세요."),

    // [PATCH] /users
    PATCH_PRE_DELETED_USER(false,2040,"이미 탈퇴한 회원입니다."),

    // [GET] /products
    EMPTY_IDX(false,2050,"IDX 값을 입력해주세요."),
    EMPTY_OFFSET(false,2051,"Offset(페이지번호)를 입력해주세요."),


    // [POST] /products
    POST_PRODUCTS_EMPTY_BRANDIDX(false,2100,"브랜드 IDX 를 입력해주세요."),
    POST_PRODUCTS_EMPTY_CATEGORYIDX(false,2101,"카테고리 IDX 를 입력해주세요."),
    POST_PRODUCTS_EMPTY_NAME(false,2102,"상품이름을 입력해주세요."),
    POST_PRODUCTS_EMPTY_PRICE(false,2103,"가격을 입력해주세요."),
    POST_PRODUCTS_EMPTY_DELIVERYTYPE(false,2104,"배송유형을 입력해주세요."),
    POST_PRODUCTS_EMPTY_ISTODAYDEAL(false,2105,"오늘의딜 여부를 입력해주세요."),

    POST_PRODUCTS_INVALID_DELIVERYTYPE(false,2110,"잘못된 배송유형이 입력되었습니다."),
    POST_PRODUCTS_INVALID_ISTODAYDEAL(false,2111,"잘못된 오늘의딜 여부가 입력되었습니다."),

    POST_PRODUCTS_PRE_EXIST_PRODUCT(false,2120,"이미 존재하는 상품의 이름이 입력되었습니다."),

    // [PATCH] /products
    PATCH_PRE_DELETED_PRODUCT(false,2130,"이미 지워진 상품입니다."),

    // [POST] /orders
    POST_ORDERS_EMPTY_USERIDX(false,2150,"UserIDX 값을 입력해주세요."),
    POST_ORDERS_EMPTY_PRODUCTIDX(false,2151,"ProductIDX 값을 입력해주세요."),
    POST_ORDERS_EMPTY_STATUS(false,2152,"Status 값을 입력해주세요."),
    POST_ORDERS_INVALID_STATUS(false,2153,"Status 형식을 확인해주세요."),

    // [PATCH] /orders
    PATCH_ORDERS_MODIFY_FAIL_CANCEL(false, 2200, "Status 를 바꾸지 못했습니다."),

    // [POST] /products/scraps
    POST_SCRAPS_PRODUCT_EMPTY_USERIDX(false, 2300, "UserIdx 를 입력해주세요."),
    POST_SCRAPS_PRODUCT_EMPTY_PRODUCTIDX(false, 2301, "ProductIdx 를 입력해주세요."),
    POST_SCRAPS_PRODUCT_DUPLICATED_SCRAP(false, 2302, "중복된 스크랩 값(유저, 상품)입니다."),

    // [PATCH] /products/scraps
    PATCH_PRODUCT_SCRAP_PRE_CANCELED(false, 2350, "이미 지워진 스크랩입니다."),

    // [POST] /products/reviews
    REVIEWS_EMPTY_PRODUCT_IDX(false, 2400, "상품 IDX 를 입력해주세요."),
    REVIEWS_EMPTY_USER_IDX(false, 2401, "유저 IDX 를 입력해주세요."),
    REVIEWS_EMPTY_REVIEW_TEXT(false, 2402, "리뷰내용을 입력해주세요."),
    REVIEWS_EMPTY_IS_TODAYHOUSE(false, 2403, "오늘의 집에서의 구매 여부를 입력해주세요."),
    REVIEWS_EMPTY_DURABILITYSTAR(false, 2404, "내구성 별점을 입력해주세요."),
    REVIEWS_EMPTY_PRICESTAR(false, 2405, "가격 별점을 입력해주세요."),
    REVIEWS_EMPTY_DESIGNSTAR(false, 2406, "디자인 별점을 입력해주세요."),
    REVIEWS_EMPTY_DELIVERYSTAR(false, 2407, "배송 별점을 입력해주세요."),

    REVIEWS_INVALID_STARS(false, 2408, "리뷰의 별점 형식에 맞게(0~5) 입력해주세요."),
    REVIEWS_INVALID_IS_TODAYHOUSE(false, 2409, "오늘의 집 구매 여부 형식에 맞게(Y, N) 입력해주세요."),
    REVIEWS_INVALID_REVIEW_TEXT(false, 2410, "리뷰 내용은 20자를 넘어야 합니다."),

    //[PATCH] /products/reviews/cancel
    PATCH_REVIEWS_PRE_CANCELED_REVIEW(false, 2430, "이미 삭제된 리뷰입니다."),

    //[POST] /products/todayDeals
    POST_TODAY_DEAL_EMPTY_PRODUCTIDX(false, 2500, "상품 IDX가 입력되지 않았습니다."),
    POST_TODAY_DEAL_EMPTY_STATUS(false, 2501, "상태 값이 입력되지 않았습니다."),
    POST_TODAY_DEAL_INVALID_STATUS(false, 2502, "잘못된 상태 값(0 또는 1 이외의 값)이 입력되었습니다."),

    //[PATCH] /products/todayDeals
    PATCH_TODAY_DEAL_PRE_DELETED(false, 2530, "이미 지워진 오늘의 딜 값입니다."),

    //[POST] /products/baskets
    POST_BASKET_EMPTY_USERIDX(false, 2600, "유저 IDX 값이 입력되지 않았습니다."),
    POST_BASKET_EMPTY_PRODUCTIDX(false, 2601, "상품 IDX 값이 입력되지 않았습니다."),
    POST_BASKET_DUPLICATED(false, 2602, "이미 해당 유저의 장바구니에 있는 상품이 입력되었습니다."),

    //[PATCH] /products/baskets
    PATCH_PRE_DELETED_BASKET(false, 2603, "이미 지워진 장바구니 정보가 입력되었습니다."),

    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),
    //2006 -> 3001
    RESPONSE_NULL_ERROR_BY_IDX(false,3001,"[NULL]입력된 IDX 값로 접근한 DB의 유효한 ROW가 존재하지 않습니다."),
    RESPONSE_NULL_ERROR(false,3002,"[NULL]접근한 데이터 중 유효한 ROW가 존재하지 않습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다."),



    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다."),

    MODIFY_FAIL_PRODUCT_TOTALSTAR(false,4015,"상품 별점 수정 실패"),
    MODIFY_FAIL_PRODUCT_TODAYDEAL(false,4016,"상품 오늘의 딜 여부 수정 실패"),


    // 5000 : 필요시 만들어서 쓰세요
    LOGOUT_JWT(false, 5000, "이미 로그아웃 된 JWT 입니다."),
    INVALID_ACCESS_TOKEN(false, 5001, "유효하지 않은 토큰입니다."),
    KAKAO_LOGIN_REQUEST_FAILED(false, 5002, "카카오 소셜 로그인 중 응답 받기에 실패했습니다."),
    INVALID_IDX(false, 5003, "잘못된 IDX 값입니다."),
    INVALID_OFFSET(false, 5004, "잘못된 OFFSET 값입니다.");

    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
