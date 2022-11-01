package com.brandsin.store.model.main.products.update

import com.google.gson.annotations.SerializedName
import java.io.File
import java.io.Serializable
import java.util.ArrayList

data class UpdateProductRequest(

        @SerializedName("media_id[]")
        var mediaId: ArrayList<Int>? = ArrayList(),

        @SerializedName("delete_media_id[]")
        var deleteMediaId:  ArrayList<Int>? = ArrayList(),

        @SerializedName("locale")
        var locale: String? = null,

        @SerializedName("id")
        var id: Int? = null,

        @SerializedName("name")
        var name: String? = null,

        @SerializedName("description")
        var description: String? = null,

        @field:SerializedName("name_en")
        var nameEn: String? = null,
        @field:SerializedName("description_en")
        var descriptionEn: String? = null,

        @SerializedName("type")
        var type: String? = null,

        @SerializedName("status")
        var status: String = "active",

        @SerializedName("product_status")
        var productStatus: String? = "new",

        @SerializedName("store_id")
        var storeId: Int? = null,

        @SerializedName("skusList")
        var skusList: ArrayList<SkuUpdateProductItem>? = null,

        @SerializedName("skus")
        var skus: String? = null,

        @SerializedName("categories")
        var categoriesIds: ArrayList<Int>? = ArrayList<Int>(),

        @SerializedName("device")
        var device: String = "android",

        var productImage: File? = null

) : Serializable

data class SkuUpdateProductItem(

        @SerializedName("name")
        var name: String? = null,

        @SerializedName("regular_price")
        var regular_price: String? = null,

        @SerializedName("sale_price")
        var sale_price: String? = null,

        @SerializedName("code")
        var code: String? = null,

        @SerializedName("inventory")
        var inventory: String? = "finite",

        @SerializedName("inventory_value")
        var inventory_value: String? = null,

        @SerializedName("status")
        var status: String? = "active",

        @SerializedName("unit_id")
        var unitId: String? = null,

        var id: Int? = null
) : Serializable