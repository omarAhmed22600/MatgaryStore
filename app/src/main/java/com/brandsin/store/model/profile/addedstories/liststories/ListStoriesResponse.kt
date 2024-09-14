package com.brandsin.store.model.profile.addedstories.liststories

import android.os.Parcelable
import com.brandsin.store.model.main.homepage.Store
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ListStoriesResponse(

    @field:SerializedName("data")
    val storiesItemByDate: List<StoriesItemByDate>? = null,

    @field:SerializedName("success")
    val success: Boolean? = null
) : Parcelable

@Parcelize
data class MediaItem(

    @field:SerializedName("order_column")
    val orderColumn: Int? = null,

    @field:SerializedName("file_name")
    val fileName: String? = null,

    @field:SerializedName("model_type")
    val modelType: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("model_id")
    val modelId: Int? = null,

    @field:SerializedName("disk")
    val disk: String? = null,

    @field:SerializedName("size")
    val size: Int? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("mime_type")
    val mimeType: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("collection_name")
    val collectionName: String? = null
) : Parcelable

@Parcelize
data class StoriesItemByDate(

    @field:SerializedName("date")
    val date: String? = null,

    @field:SerializedName("stories")
    val stories: List<StoriesItem?>? = null
) : Parcelable

@Parcelize
data class StoriesItem(

    @field:SerializedName("store_id")
    val storeId: Int? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("text")
    val text: String? = null,

    @field:SerializedName("x")
    val x: Double? = null,

    @field:SerializedName("y")
    val y: Double? = null,

    /*@field:SerializedName("media")
    val media: List<MediaItem?>? = null,*/

    @field:SerializedName("media_url")
    val mediaUrl: String? = null,

    @field:SerializedName("views")
    val views: Int? = null,

    @field:SerializedName("store")
    var store: Store? = null,

    @SerializedName("product")
    val product: ProductItem? = null,

    var isSelected: Boolean = false

) : Parcelable

@Parcelize
data class ProductItem(

    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("type")
    val type: String? = null,

    @SerializedName("image")
    val image: String? = null,

    @SerializedName("images")
    val images: List<String>? = null,

    // @SerializedName("images_ids") val imagesIds: List<Images_ids>? = null,
    @SerializedName("discount")
    val discount: Int? = null,

    @SerializedName("video")
    val video: String? = null,

    @SerializedName("files")
    val files: String? = null,

    @SerializedName("name_en")
    val nameEn: String? = null,

    @SerializedName("description_en")
    val descriptionEn: String? = null,

    @SerializedName("is_refundable")
    val isRefundable: Boolean? = null,

    @SerializedName("avg_rating")
    val avgRating: Double? = null,

    @SerializedName("is_favorite")
    val isFavorite: Boolean? = null,

    // @SerializedName("media") val media: List<Media>? = null,
    @SerializedName("skus") val skus: List<SkusItem>? = null
) : Parcelable

@Parcelize
data class SkusItem (

    @SerializedName("id")
    val id : Int? = null,

    @SerializedName("name")
    val name : String ? = null,

    @SerializedName("product_id")
    val productId : Int ? = null,

    @SerializedName("regular_price") val regularPrice : Double ? = null,
    @SerializedName("sale_price") val salePrice : Double? = null,
    @SerializedName("code") val code : String? = null,
    @SerializedName("inventory") val inventory : String? = null,
    @SerializedName("inventory_value") val inventoryValue : String? = null,
    @SerializedName("allowed_quantity") val allowedQuantity : Int? = null,
    @SerializedName("status") val status : String? = null,
    @SerializedName("shipping") val shipping : String? = null,
    @SerializedName("free_shipping") val freeShipping : Int? = null,
    @SerializedName("free_refunding") val freeRefunding : Int? = null,
    @SerializedName("secured") val secured : Int? = null,
    @SerializedName("unit_id") val unit_id : String? = null,
    @SerializedName("price") val price : Double? = null,
    @SerializedName("media") val media : List<String>? = null
) : Parcelable
