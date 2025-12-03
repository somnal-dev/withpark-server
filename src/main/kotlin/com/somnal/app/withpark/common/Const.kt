package com.somnal.app.withpark.common

class Const {
    companion object {
        const val RESULT_MESSAGE_SUCCESS        = "정상적으로 처리되었습니다."

        // 카카오 Oauth 2.0 관련 상수값
        const val KAKAO_AUTHORIZE_URL           = "https://kauth.kakao.com/oauth/authorize"
        const val KAKAO_GET_ACCESS_TOKEN_URL    = "https://kauth.kakao.com/oauth/token"
        const val KAKAO_GET_USER_INFO_URL       = "https://kapi.kakao.com/v2/user/me"

        // 네이버 Oauth 2.0 관련 상수값
        const val NAVER_AUTHORIZE_URL           = "https://nid.naver.com/oauth2.0/authorize"
        const val NAVER_GET_ACCESS_TOKEN_URL    = "https://nid.naver.com/oauth2.0/token"
        const val NAVER_GET_USER_INFO_URL       = "https://openapi.naver.com/v1/nid/me"
    }
}