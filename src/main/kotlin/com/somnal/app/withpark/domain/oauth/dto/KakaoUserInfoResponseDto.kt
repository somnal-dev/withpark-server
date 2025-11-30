package com.somnal.app.withpark.domain.oauth.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.Date

@JsonIgnoreProperties(ignoreUnknown = true)
data class KakaoUserInfoResponseDto(
    //회원 번호
    @JsonProperty("id")
    val id: Long,

    @JsonProperty("has_signed_up")
    val hasSignedUp: Boolean? = null,

    @JsonProperty("connected_at")
    val connectedAt: Date? = null,

    //카카오싱크 간편가입을 통해 로그인한 시각. UTC
    @JsonProperty("synched_at")
    val synchedAt: Date? = null,

    //사용자 프로퍼티
    @JsonProperty("properties")
    val properties: HashMap<String, String>? = null,

    //카카오 계정 정보
    @JsonProperty("kakao_account")
    val kakaoAccount: KakaoAccount,

    @JsonProperty("for_partner")
    val partner: Partner? = null,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class KakaoAccount(
    //프로필 정보 제공 동의 여부
    @JsonProperty("profile_needs_agreement")
    val isProfileAgree: Boolean? = null,

    //닉네임 제공 동의 여부
    @JsonProperty("profile_nickname_needs_agreement")
    val isNickNameAgree: Boolean? = null,

    //프로필 사진 제공 동의 여부
    @JsonProperty("profile_image_needs_agreement")
    val isProfileImageAgree: Boolean? = null,

    //사용자 프로필 정보
    @JsonProperty("profile")
    val profile: Profile,

    //이름 제공 동의 여부
    @JsonProperty("name_needs_agreement")
    val isNameAgree: Boolean? = null,

    //카카오계정 이름
    @JsonProperty("name")
    val name: String? = null,

    //이메일 제공 동의 여부
    @JsonProperty("email_needs_agreement")
    val isEmailAgree: String? = null,

    //이메일이 유효 여부
    // true : 유효한 이메일, false : 이메일이 다른 카카오 계정에 사용돼 만료
    @JsonProperty("is_email_valid")
    val isEmailValid: Boolean? = null,

    //이메일이 인증 여부
    //true : 인증된 이메일, false : 인증되지 않은 이메일
    @JsonProperty("is_email_verified")
    val isEmailVerified: Boolean? = null,

    //카카오계정 대표 이메일
    @JsonProperty("email")
    val email: String = "",

    //연령대 제공 동의 여부
    @JsonProperty("age_range_needs_agreement")
    val isAgeAgree: Boolean? = null,

    //연령대
    @JsonProperty("age_range")
    val ageRange: String? = null,

    //출생 연도 제공 동의 여부
    @JsonProperty("birthyear_needs_agreement")
    val isBirthYearAgree: Boolean? = null,

    //출생 연도 (YYYY 형식)
    @JsonProperty("birthyear")
    val birthYear: String? = null,

    //생일 제공 동의 여부
    @JsonProperty("birthday_needs_agreement")
    val isBirthDayAgree: Boolean? = null,

    //생일 (MMDD 형식)
    @JsonProperty("birthday")
    val birthDay: String? = null,

    //생일 타입
    // SOLAR(양력) 혹은 LUNAR(음력)
    @JsonProperty("birthday_type")
    val birthDayType: String? = null,

    //성별 제공 동의 여부
    @JsonProperty("gender_needs_agreement")
    val isGenderAgree: Boolean? = null,

    //성별
    @JsonProperty("gender")
    val gender: String? = null,

    //전화번호 제공 동의 여부
    @JsonProperty("phone_number_needs_agreement")
    val isPhoneNumberAgree: Boolean? = null,

    //전화번호
    //국내 번호인 경우 +82 00-0000-0000 형식
    @JsonProperty("phone_number")
    val phoneNumber: String? = null,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Profile(

    //닉네임
    @JsonProperty("nickname")
    val nickName: String = "",

    //프로필 미리보기 이미지 URL
    @JsonProperty("thumbnail_image_url")
    val thumbnailImageUrl: String? = null,

    //프로필 사진 URL
    @JsonProperty("profile_image_url")
    val profileImageUrl: String? = null,

    //프로필 사진 URL 기본 프로필인지 여부
    //true : 기본 프로필, false : 사용자 등록
    @JsonProperty("is_default_image")
    val isDefaultImage: String? = null,

    //닉네임이 기본 닉네임인지 여부
    //true : 기본 닉네임, false : 사용자 등록
    @JsonProperty("is_default_nickname")
    val isDefaultNickName: String? = null,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Partner(
    @JsonProperty("uuid")
    val uuid: String? = null,
)