package com.brandsin.store.model.auth.setting.countryId

import com.google.gson.annotations.SerializedName

data class CountryIdRequest (
    @SerializedName("code") var code: String? = null,
    @SerializedName("lang") var lang: String? = null
)