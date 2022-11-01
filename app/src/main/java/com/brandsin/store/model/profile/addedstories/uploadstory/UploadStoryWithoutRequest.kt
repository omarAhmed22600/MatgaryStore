package com.brandsin.store.model.profile.addedstories.uploadstory

import com.google.gson.annotations.SerializedName


data class UploadStoryWithoutRequest (

    @SerializedName("store_id")
    var storeId: Int? = null,

    @SerializedName("text")
    var text: String? = null
)