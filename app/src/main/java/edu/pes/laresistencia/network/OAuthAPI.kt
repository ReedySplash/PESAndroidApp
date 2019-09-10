package edu.pes.laresistencia.network

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface OAuthAPI {

    @FormUrlEncoded
    @POST("/oauth/token")
    fun login(@Header("Authorization") authorization: String,
              @Field("username") username: String,
              @Field("password") password: String,
              @Field("grant_type") grantType: String): Call<JWT>
}

data class JWT(
        @SerializedName("access_token") val accessToken: String,
        @SerializedName("token_type") val tokenType: String,
        @SerializedName("expires_in") val expiresIn: Int,
        @SerializedName("scope") val scope: String,
        @SerializedName("jti") val jti: String
)