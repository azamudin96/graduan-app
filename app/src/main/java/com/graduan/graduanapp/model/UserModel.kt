package com.graduan.graduanapp.model

import com.google.gson.annotations.SerializedName

data class UserModel(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("email_verified_at") var emailVerifiedAt: String? = null,
    @SerializedName("avatar_path") var avatarPath: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("avatar_url") var avatarUrl: String? = null

)