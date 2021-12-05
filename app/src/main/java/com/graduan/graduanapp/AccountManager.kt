package com.graduan.graduanapp

import android.accounts.Account
import android.content.Context
import android.widget.Toast
import com.google.gson.Gson
import com.graduan.graduanapp.model.LoginModel
import com.graduan.graduanapp.rest.AccessToken
import com.graduan.graduanapp.rest.UserApi

class AccountManager {
    interface onLoginListener {
        fun onLoginComplete(isSuccessful: Boolean, errorMessage: String?)
    }

    private var instance: AccountManager? = null

    private var token: AccessToken? = null
    private var account: Account? = null

    private val tokenData = "Token"
    private val singleSignOn = "SSO"

    companion object {
        val instance: AccountManager by lazy { AccountManager() }
    }

    fun clear() {
        instance = null
    }

    private fun AccountManager() {}


    fun getToken(): AccessToken? {
        instance = this
        return instance!!.token
    }

    fun getAccountDetails(): Account? {
        return account
    }

    fun isLogin(): Boolean {
        return account != null
    }

    fun setAccountDetails(account: Account?) {
        this.account = account
    }

    suspend fun login(context: Context?, loginModel: LoginModel?): Boolean {
        val response = ApiServices._instance.with(context!!).login(context, loginModel)
        var isSuccess = false
        if (response.token != null) {
            token = response
            account = token!!.user
            isSuccess = true
            return isSuccess
        }
        return isSuccess
    }


    fun getAccountDetails(context: Context?, callback: onLoginListener) {
        callback.onLoginComplete(true, "")
//        APIServices.getInstance(null).with(context).isLoading().getAccountDetails(new OnRetrofitResponse<ResponseModel<Account>>() {
//            @Override
//            public void onResponse(ResponseModel<Account> response) {
//                client = response.getData();
//                storeAccount();
//                if (callback != null) {
//                    callback.onLoginComplete(true, null);
//                }
//
//
//                FirebaseInstanceId.getInstance().getInstanceId()
//                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                                if (!task.isSuccessful()) {
//                                    Log.w(TAG, "getInstanceId failed", task.getException());
//                                    return;
//                                }
//
//                                // Get new Instance ID token
//                                String token = task.getResult().getToken();
//
//                                System.out.println("%%%% firebase token = "+token);
//                                if (response.getData() != null &&
//                                        (TextUtils.isEmpty(response.getData().getFcmtoken()) ||
//                                                !response.getData().getFcmtoken().equals(token))) {
////
////                                    InitApplication._instance.updateFirebaseToken(
////                                            InitApplication._instance.prefs.getString(FirebaseIDService.PREFS_FIREBASE_ID, ""),
////                                            true,//TODO: off notification from mobile side
////                                            false,
////                                            null,
////                                            context);
//
//                                    FcmTokenModel fcmTokenModel = new FcmTokenModel();
//                                    fcmTokenModel.setFcmToken(token);
//                                    try {
//                                        APIServices.getInstance(null).updateFcmToken(fcmTokenModel, new OnRetrofitResponse<ResponseModel<Account>>() {
//                                            @Override
//                                            public void onResponse(ResponseModel<Account> response) {
//                                                response.isSuccess();
//                                            }
//
//                                            @Override
//                                            public void onFailureHeader(int responseCode, ResponseBody errorBody) {
//                                                System.out.println(responseCode + " " + errorBody);
//                                            }
//
//                                            @Override
//                                            public void onFailure(int status, ResponseModel errorResponseModel) {
//                                                System.out.println(status + " " + errorResponseModel);
//                                            }
//                                        });
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//
//
//                            }
//                        });
//
//
//            }
//
//            @Override
//            public void onFailureHeader(int responseCode, ResponseBody errorBody) {
//                try {
//                    callback.onLoginComplete(false, errorBody.string());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//            @Override
//            public void onFailure(int status, ResponseModel errorResponseModel) {
//                Toast.makeText(context, R.string.connectionError, Toast.LENGTH_SHORT).show();
//                callback.onLoginComplete(false, null);
//            }
//        });
    }

    fun storeAccount() {
        val gson = Gson()
        val data = gson.toJson(token)
        MyApp.sharedPreferences!!.edit().putString(tokenData, data).apply()
    }

    fun setSingleSignOn(sso: Boolean) {
        MyApp.sharedPreferences!!.edit().putBoolean(singleSignOn, sso).apply()
    }


    fun isSingleSignOn(): Boolean {
        return MyApp.sharedPreferences!!.getBoolean(singleSignOn, false)
    }


    fun loadAccount() {
        if (token == null) {
            val gson = Gson()
            token = gson.fromJson(
                MyApp.sharedPreferences!!.getString(tokenData, gson.toJson(token)),
                AccessToken::class.java
            )
        }
    }


//    fun logout(context: Context?, callback: onLoginListener?) {
//        AccountManager.getInstance().setSingleSignOn(false)
//        if (callback != null) {
//            token = null
//            callback.onLoginComplete(true, null)
//        }
//    }
}