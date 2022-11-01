package com.brandsin.store.model.profile.updateprofile

import com.google.gson.annotations.SerializedName

data class UpdateProfileRequest (
        @SerializedName("user_id") var user_id: String? = null,
        @SerializedName("name") var name: String? = null,
        @SerializedName("last_name") var last_name: String? = null,
        @SerializedName("phone_number") var phone_number: String? = null,
        @SerializedName("email") var email: String? = null,
        @SerializedName("picture") var picture: String? = null,
        @SerializedName("picture_type") var picture_type: String? = null,
        @SerializedName("lang") var lang: String? = null
)