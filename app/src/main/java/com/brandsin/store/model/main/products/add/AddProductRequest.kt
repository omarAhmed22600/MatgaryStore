package com.brandsin.store.model.main.products.add

import com.google.gson.annotations.SerializedName
import java.io.File
import java.io.Serializable
import java.util.*

data class AddProductRequest(

        @SerializedName("media_id[]")
        var mediaId: ArrayList<Int>? = ArrayList(),

        @SerializedName("delete_media_id[]")
        var deleteMediaId:  ArrayList<Int>? = ArrayList(),

        @SerializedName("locale")
        var locale: String? = null,

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
        var skusList: ArrayList<SkuAddProductItem>? = null,

        @SerializedName("skus")
        var skus: String? = null,

        @SerializedName("categories")
        var categoriesIds: ArrayList<Int>? = null,

        @SerializedName("device")
        var device: String = "android",

        var productImage: File? = null

) : Serializable

data class SkuAddProductItem(

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