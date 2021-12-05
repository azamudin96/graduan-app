package com.graduan.graduanapp.model

import com.google.gson.annotations.SerializedName

data class ErrorResponse(@SerializedName("message") var message: String? = null)
