package com.graduan.graduanapp.rest

import android.accounts.Account
import com.google.gson.annotations.SerializedName

data class AccessToken(
    val token: String? = null,
    val token_type: String = "Bearer",
    val user: Account? = null
)
