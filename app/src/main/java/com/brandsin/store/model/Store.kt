package com.brandsin.store.model

data class Store(
    val avg_rating: Int,
    val commercial_register: CommercialRegister,
    val covers: List<Cover>,
    val followers_count: Int,
    val id: Int,
    val image: String,
    val images: List<Image>,
    val is_followed: Boolean,
    val min_price_product: Int,
    val name: String,
    val thumbnail: String,
    val thumbnail_id: Int
)