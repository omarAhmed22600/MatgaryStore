package com.brandsin.store.model.main.products.delete

import com.google.gson.annotations.SerializedName

data class DeleteProductRequest (
        @SerializedName("product_id")
        var product_id: Int? = null
)