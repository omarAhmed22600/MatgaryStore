package com.brandsin.store.model.profile.addedstories.uploadstory

import com.google.gson.annotations.SerializedName
import java.io.File
import java.io.Serializable

data class UploadStoryRequest(
    @SerializedName("store_id")
    var storeId: Int? = null,

    var text: String? = null,

    @SerializedName("x")
    var x: String? = null,

    @SerializedName("y")
    var y: String? = null,

    @SerializedName("product_id")
    var productId: Int? = null,

    var file: File? = null,
) : Serializable