package com.brandsin.store.model

data class ListStoriesResponse(
    val date: String,
    val stories: List<Story>
)