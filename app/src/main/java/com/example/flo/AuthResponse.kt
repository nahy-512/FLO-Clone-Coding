package com.example.flo

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName(value = "isSuccess") val inSuccess: Boolean,
    @SerializedName(value = "code") val code: Int,
    @SerializedName(value = "message") val message: String,
    @SerializedName(value = "result") val result: Result? //회원가입 API 사용한 후 알아서 null 처리가 되어 같이 사용할 수 있음
    )

data class Result(
    @SerializedName(value = "userIdx")var userIdx : Int,
    @SerializedName(value = "jwt")var jwt : String
)
