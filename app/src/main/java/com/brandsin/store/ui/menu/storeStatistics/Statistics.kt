package com.brandsin.store.ui.menu.storeStatistics

import com.google.gson.annotations.SerializedName

data class Statistics(
    @SerializedName("followers_count")
    val followersCount: Int = -1,
    @SerializedName("product_rating")
    val productRating: Double = 0.0,
    @SerializedName("store_rating")
    val storeRating: Int = -1,
    @SerializedName("story_views")
    val storyViews: Int = -1
) {
    val productRatingString = productRating.toString()
}