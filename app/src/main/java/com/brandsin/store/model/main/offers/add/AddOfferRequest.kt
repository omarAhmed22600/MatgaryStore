package com.brandsin.store.model.main.offers.add

import com.google.gson.annotations.SerializedName
import java.io.File

data class AddOfferRequest(

    @SerializedName("locale")
    var locale: String? = null,

    @SerializedName("store_id")
    var storeId: Int? = null,

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("description")
    var description: String? = null,

    @field:SerializedName("name_en")
    var nameEn: String? = null,
    @field:SerializedName("description_en")
    var descriptionEn: String? = null,

    @SerializedName("price")
    var price: String? = null,

    @SerializedName("price_to")
    var priceTo: String? = null,

    @SerializedName("start_date")
    var startDate: String? = null,

    @SerializedName("end_date")
    var endDate: String? = null,

    @SerializedName("active")
    var active: Int? = null,

    @SerializedName("type")
    var type: String? = null,

    var productsIds: ArrayList<Int>? = null,

    var offerImage: File? = null,
    var offerVideo: File? = null

)