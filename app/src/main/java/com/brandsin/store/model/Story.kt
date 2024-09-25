package com.brandsin.store.model

data class Story(
    val created_at: String,
    val fav_count: Int,
    val id: Int,
    val in_offers_page: Int,
    val is_favorite: Boolean,
    val is_pinned: Int,
    val is_pinned_homepage: Int,
    val is_seen: Boolean,
    val marketing_request_id: Int,
    val media_url: String,
    val product: Any,
    val product_id: Any,
    val store: Store,
    val store_id: Int,
    val text: String,
    val title: Any,
    val video_img: Any,
    val views: Int,
    val x: Any,
    val y: Any,
    var isSelected: Boolean = false

)