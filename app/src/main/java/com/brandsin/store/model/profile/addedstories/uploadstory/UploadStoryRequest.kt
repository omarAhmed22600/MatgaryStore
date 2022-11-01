package com.brandsin.store.model.profile.addedstories.uploadstory

import java.io.File
import java.io.Serializable

data class UploadStoryRequest (
    var storeId: Int? = null,
    var text: String? = null,
    var file: File? = null
) : Serializable