package com.graduan.graduanapp.rest

import com.graduan.graduanapp.BuildConfig
import com.graduan.graduanapp.model.UserModel
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface UserApi {
    //=================== Get Method ===================

    @POST("login")
    @FormUrlEncoded
    suspend fun signIn(
        @Field("email") email: String?,
        @Field("password") password: String?
    ): Response<AccessToken>


    @GET("user")
    suspend fun getUserInfo(@Header("Authorization") auth: String): Response<UserModel>

    @POST("logout")
    suspend fun signOut(): Response<AccessToken>

    @POST("user")
    @FormUrlEncoded
    suspend fun updateUserInfo(
        @Field("name") name: String?,
        @Header("Authorization") auth: String
    ): Response<ResponseBody>

    @POST("user/avatar")
    @Multipart
    suspend fun uploadUserAvatar(
        @Part file: MultipartBody.Part,
        @Header("Authorization") auth: String
    ): Response<ResponseBody>

    companion object {
        var userApiService: UserApi? = null
        fun getInstance(): UserApi {
            if (userApiService == null) {
                userApiService = Retrofit.Builder()
                    .baseUrl(BuildConfig.ApiUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(UserApi::class.java)
            }
            return userApiService!!
        }
    }
}