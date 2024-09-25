package com.brandsin.store.ui.main.marketingRequest

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import java.io.File

data class MarketingRequest(
    val context: RequestBody,
    val type: RequestBody,
    @SerializedName("start_date")
    val startDate: RequestBody,  // "2024-08-30 11:00:00"
    @SerializedName("end_date")
    val endDate: RequestBody,    // "2024-09-05 11:00:00"
    @SerializedName("number_of_shopping_days")
    val numberOfShoppingDays: RequestBody,
    @SerializedName("image_ar")
    val imageAr: MultipartBody.Part,
    @SerializedName("image_en")
    val imageEn: MultipartBody.Part,
    val price: RequestBody

)
