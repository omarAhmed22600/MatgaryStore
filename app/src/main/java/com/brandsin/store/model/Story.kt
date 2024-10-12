package com.brandsin.store.model

import android.os.Parcelable
import com.brandsin.store.model.profile.addedstories.liststories.ProductItem
import com.brandsin.store.ui.menu.storeStatistics.Product
import kotlinx.parcelize.Parcelize

import kotlinx.parcelize.RawValue

@Parcelize
data class Story(
    val created_at: String = "",
    val fav_count: Int = -1,
    val id: Int = -1,
    val in_offers_page: Int = -1,
    val is_favorite: Boolean = false,
    val is_pinned: Int = -1,
    val is_pinned_homepage: Int = -1,
    val is_seen: Boolean = false,
    val marketing_request_id: Int = -1,
    val media_url: String = "",
    val product: @RawValue ProductItem? = null,   // Use @RawValue for Any type
    val product_id: @RawValue Any? = null, // Use @RawValue for Any type
    var store: @RawValue Store? = null,    // Use @RawValue for Store if it doesn't implement Parcelable
    val store_id: Int = -1,
    val text: String = "",
    val title: @RawValue Any? = null,      // Use @RawValue for Any type
    val video_img: @RawValue Any? = null,  // Use @RawValue for Any type
    val views: Int = -1,
    val x: @RawValue Float? = null,          // Use @RawValue for Float type
    val y: @RawValue Float? = null,          // Use @RawValue for Any type
    var isSelected: Boolean = false
) : Parcelable
