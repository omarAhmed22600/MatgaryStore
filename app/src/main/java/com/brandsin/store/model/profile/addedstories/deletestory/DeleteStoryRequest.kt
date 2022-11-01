package com.brandsin.store.model.profile.addedstories.deletestory

import com.google.gson.annotations.SerializedName

data class DeleteStoryRequest (
    @SerializedName("story_id")
    var story_id: Int? = null
)