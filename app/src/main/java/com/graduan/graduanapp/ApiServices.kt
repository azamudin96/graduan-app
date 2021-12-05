package com.graduan.graduanapp

import android.content.Context
import android.text.TextUtils
import android.widget.Toast
import com.graduan.graduanapp.model.LoginModel
import com.graduan.graduanapp.model.UserModel
import com.graduan.graduanapp.rest.AccessToken
import com.graduan.graduanapp.rest.UserApi
import okhttp3.MultipartBody

class ApiServices {

    private var connectionErrorMsg: String? = null

    private val TAG = "Graduan"

    private var _instance: ApiServices? = null

    companion object {
        lateinit var appContext: Context
        val _instance: ApiServices by lazy { ApiServices() }
    }

    fun with(context: Context): ApiServices {
        appContext = context
        _instance = ApiServices()
        if (TextUtils.isEmpty(connectionErrorMsg)) connectionErrorMsg =
            context.getString(R.string.connectionError)
        return _instance!!
    }

    fun myToken(): String {
        return "${AccountManager.instance.getToken()!!.token_type} ${AccountManager.instance.getToken()!!.token}"
    }

    suspend fun login(context: Context?, loginModel: LoginModel?): AccessToken {
        val apiService = UserApi.getInstance()
        val response = apiService.signIn(loginModel!!.username, loginModel.password)
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            Toast.makeText(context, response.errorBody().toString(), Toast.LENGTH_SHORT).show()
        }
        return response.body()!!
    }

    suspend fun getUserInfo(context: Context?): UserModel? {
        val apiService = UserApi.getInstance()
        val response = apiService.getUserInfo(myToken())
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            Toast.makeText(context, response.errorBody().toString(), Toast.LENGTH_SHORT).show()
            return null
        }
    }

    suspend fun updateUserInfo(context: Context?, name: String): String? {
        val apiService = UserApi.getInstance()
        val response = apiService.updateUserInfo(name, myToken())
        if (response.code() == 200) {
            return "Success"
        } else {
            Toast.makeText(context, R.string.connectionError, Toast.LENGTH_SHORT).show()
            return ""
        }
    }

    suspend fun uploadUserImage(context: Context?, requestBody: MultipartBody.Part): String? {
        val apiService = UserApi.getInstance()
        val response = apiService.uploadUserAvatar(requestBody, myToken())
        if (response.code() == 200) {
            return "Success"
        } else {
            Toast.makeText(context, R.string.connectionError, Toast.LENGTH_SHORT).show()
            return ""
        }
    }


}